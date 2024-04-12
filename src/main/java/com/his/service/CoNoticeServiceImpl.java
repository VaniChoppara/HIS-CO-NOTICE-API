package com.his.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.his.client.ApplicationApiClient;
import com.his.client.CitizenApiClient;
import com.his.client.EdApiClient;
import com.his.dto.ApplicationRegDTO;
import com.his.dto.CitizenDTO;
import com.his.dto.CoNoticeDTO;
import com.his.dto.EligDetermineDTO;
import com.his.entity.CoNotice;
import com.his.repository.CoNoticeRepository;
import com.his.utils.EmailUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.mail.MessagingException;

@Service
public class CoNoticeServiceImpl implements CoNoticeService {

	@Autowired
	CoNoticeRepository coRepository;

	@Autowired
	EdApiClient edClient;

	@Autowired
	CitizenApiClient citizenClient;

	@Autowired
	ApplicationApiClient appClient;
	
	@Autowired
	S3Service s3Service;

	@Autowired
	EmailUtils emailUtils;

	@Override
	public CoNotice saveCoNotice(CoNoticeDTO coNoticeDto) {
		CoNotice coNotice = new CoNotice();
		BeanUtils.copyProperties(coNoticeDto, coNotice);
		CoNotice save = coRepository.save(coNotice);
		return save;
	}

	@Override
	public boolean printCoNotice(Integer noticeId) throws FileNotFoundException, DocumentException, MessagingException {
		
		CoNotice coNotice = coRepository.findById(noticeId).orElseThrow();
		
		EligDetermineDTO edDetail = edClient.getEdDetalilByAppNumber(coNotice.getAppNumber());
		
		ApplicationRegDTO application = appClient.getApplication(coNotice.getAppNumber());
		
		CitizenDTO citizen = citizenClient.getCitizen(application.getCitizenId());

		File noticePdf = new File(noticeId + ".pdf");
		String subject = "Notice Generated";
		String to = citizen.getEmail();
		String body = "Please find the notice";

		// 1. Generate pdf
		noticePdf=generatePdf(edDetail, noticePdf,citizen);

		// 2. Store pdf in s3
		//coNotice.setS3Url(url);
		String s3Url = s3Service.saveFile(noticePdf);
		System.out.println("-----------------------------------------" + s3Url);

		//uploadNoticeToS3(noticePdf);
		// 3. Send an email
		emailUtils.sendEmail(subject, to, body, noticePdf);
		
		

		coNotice.setCoNoticeStatus("History");
		coNotice.setBenefitAmount(edDetail.getBenefitAmount());
		coNotice.setCoGenDate(LocalDate.now());
		coNotice.setCoStartDate(edDetail.getEligStartdate());
		coNotice.setCoEndDate(edDetail.getEligEndDate());
		coNotice.setIsGenerated("Y");
		coNotice.setS3Url(s3Url);

		CoNotice savedCoNotice = coRepository.save(coNotice);
		CoNoticeDTO coDto = new CoNoticeDTO();
		BeanUtils.copyProperties(savedCoNotice, coDto);
		return true;
	}


	private File generatePdf(EligDetermineDTO edDetail,File noticePdf, CitizenDTO citizen) throws DocumentException, FileNotFoundException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(noticePdf));
		document.open();

		PdfPTable table = new PdfPTable(2);

		if (edDetail.getEligStatus().equalsIgnoreCase("Approved")) {
			generateApprovedNotice(edDetail, table, citizen);

		} else {
			generateDeniedNotice(edDetail, table, citizen);
		}

		document.add(table);
		document.close();
		return noticePdf;

	}

	private void generateDeniedNotice(EligDetermineDTO edDetail, PdfPTable table, CitizenDTO citizen) {
		Stream.of("Citizen Eligibility Denied Notice").forEach(columnTitle -> {
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
		
		table.addCell("Name");
		table.addCell(citizen.getName());

		table.addCell("Plan Name");
		table.addCell(edDetail.getPlanName());

		table.addCell("Plan Status");
		table.addCell(edDetail.getEligStatus());

		table.addCell("Deniel Reason");
		table.addCell(edDetail.getDenialReason());
	}

	private void generateApprovedNotice(EligDetermineDTO edDetail, PdfPTable table, CitizenDTO citizen) {
		Stream.of("Citizen Eligibility Approved Notice").forEach(columnTitle -> {
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
		
		table.addCell("Name");
		table.addCell(citizen.getName());


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
	}

	@Override
	public List<CoNotice> getCoNotices() {
		// TODO Auto-generated method stub
		return coRepository.findAll();
	}

}
