package com.his.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.his.client.EdApiClient;
import com.his.dto.CoNoticeDTO;
import com.his.dto.EligDetermineDTO;
import com.his.entity.CoNotice;
import com.his.repository.CoNoticeRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class CoNoticeServiceImpl implements CoNoticeService {

	@Autowired
	CoNoticeRepository coRepository;
	
	@Autowired
	EdApiClient edClient;
	
	
	@Override
	public CoNotice saveCoNotice(CoNoticeDTO coNoticeDto) {
		CoNotice coNotice=new CoNotice();
		BeanUtils.copyProperties(coNoticeDto, coNotice);
		CoNotice save = coRepository.save(coNotice);
		return save;
	}

	@Override
	public CoNoticeDTO printCoNotice(Integer appNumber) throws FileNotFoundException, DocumentException {
		// TODO Auto-generated method stub
		
		EligDetermineDTO edDetail = edClient.getEdDetalilByAppNumber(appNumber);
		CoNotice coNotice= coRepository.findByAppNumber(appNumber);
		coNotice.setCoNoticeStatus("History");
		coNotice.setBenefitAmount(edDetail.getBenefitAmount());
		coNotice.setCoGenDate(LocalDate.now());
		coNotice.setCoStartDate(edDetail.getEligStartdate());
		coNotice.setCoEndDate(edDetail.getEligEndDate());
		coNotice.setIsGenerated("Y");
		
		// generates the pdf and save to the aws s3 bucket and also sends email to the customer
		String url=generatePdf(edDetail);
		coNotice.setS3Url(url);
		CoNotice savedCoNotice= coRepository.save(coNotice);
		CoNoticeDTO coDto= new CoNoticeDTO();
		BeanUtils.copyProperties(savedCoNotice,coDto);
		return coDto;
	}

	private String generatePdf(EligDetermineDTO edDetail) throws DocumentException, FileNotFoundException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("./CoNotice"+edDetail.getAppNumber()+".pdf"));
		document.open();

		PdfPTable table = new PdfPTable(2);
		
		if(edDetail.getEligStatus().equalsIgnoreCase("Approved")) {
		// Adding header
		 Stream.of("Citizen Eligibility Approved Notice")
	      .forEach(columnTitle -> {
	        PdfPCell header = new PdfPCell();
	        header.setBackgroundColor(BaseColor.BLUE);
	        header.setColspan(2);
	        header.setBorderWidth(1);
	        header.setPhrase(new Phrase(columnTitle));
	        header.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(header);
	    });
		// Adding rows		 
		 	table.addCell("App Number");
		    table.addCell(edDetail.getAppNumber().toString());
		    
		    table.addCell("Plan Name");
		    table.addCell(edDetail.getPlanName());
		    
		    table.addCell("Plan Status");
		    table.addCell(edDetail.getEligStatus());
		    
		    table.addCell("Eligibility Start Date");
		    table.addCell(edDetail.getEligStartdate().toString());
		    
		    table.addCell("Eligibility End Date");
		    table.addCell(edDetail.getEligEndDate().toString());


		    table.addCell("Benefit Amount");
		    table.addCell(edDetail.getBenefitAmount().toString());
		  
		}else {
			Stream.of("Citizen Eligibility Denied Notice")
		      .forEach(columnTitle -> {
		        PdfPCell header = new PdfPCell();
		        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        header.setColspan(2);
		        header.setBorderWidth(1);
		        header.setHorizontalAlignment(Element.ALIGN_CENTER);
		        header.setPhrase(new Phrase(columnTitle));
		        table.addCell(header);
		    });
			
			table.addCell("App Number");
		    table.addCell(edDetail.getAppNumber().toString());
		    
		    table.addCell("Plan Name");
		    table.addCell(edDetail.getPlanName());
		    
		    table.addCell("Plan Status");
		    table.addCell(edDetail.getEligStatus());
		    
		    table.addCell("Deniel Reason");
		    table.addCell(edDetail.getDenialReason());

		}
		   
		   
		
		 
		document.add(table);
		document.close();
		
		return "CoNotice"+edDetail.getAppNumber()+".pdf";
	}
	
	
	
	
}
