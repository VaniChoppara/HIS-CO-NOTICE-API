package com.his.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.his.dto.CoNoticeDTO;
import com.his.entity.CoNotice;
import com.his.service.CoNoticeService;
import com.itextpdf.text.DocumentException;

import jakarta.mail.MessagingException;

@RestController
public class CoNoticeController {
	@Autowired
    CoNoticeService coService;
	
	
	
	@PostMapping("/createCo")
	public ResponseEntity<String> generateCorrespondence(@RequestBody CoNoticeDTO coNoticeDto){
		coService.saveCoNotice(coNoticeDto);
		return new ResponseEntity<>("Co Notice Generated", HttpStatus.CREATED);
	}
	
	@PostMapping("/printCo/{coNoticeId}")
	public ResponseEntity<Boolean> printCoNotice(@PathVariable("coNoticeId") Integer coNoticeId) throws FileNotFoundException, DocumentException, MessagingException{
		
		boolean status=coService.printCoNotice(coNoticeId);
		return new ResponseEntity<>(status, HttpStatus.CREATED);
		
	}
	
	public ResponseEntity<List<CoNotice>> getCoNotices(){
		List<CoNotice> coNotices = coService.getCoNotices();
		return new ResponseEntity<>(coNotices, HttpStatus.CREATED);
	}
}
