package com.his.controller;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.his.dto.CoNoticeDTO;
import com.his.service.CoNoticeService;
import com.itextpdf.text.DocumentException;

@RestController
public class CoNoticeController {
	@Autowired
    CoNoticeService coService;
	
	
	
	@PostMapping("/createCo")
	public ResponseEntity<String> generateCorrespondence(@RequestBody CoNoticeDTO coNoticeDto){
		coService.saveCoNotice(coNoticeDto);
		return new ResponseEntity<>("Co Notice Generated", HttpStatus.CREATED);
	}
	
	@GetMapping("/printCo/{appNumber}")
	public ResponseEntity<CoNoticeDTO> printCoNotice(@PathVariable("appNumber") Integer appNumber) throws FileNotFoundException, DocumentException{
		
		
		CoNoticeDTO coNoticeDto= new CoNoticeDTO();
		
		coNoticeDto=coService.printCoNotice(appNumber);
		return new ResponseEntity<>(coNoticeDto, HttpStatus.CREATED);
		
	}
}
