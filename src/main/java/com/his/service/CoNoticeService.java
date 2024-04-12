package com.his.service;

import java.io.FileNotFoundException;
import java.util.List;

import com.his.dto.CoNoticeDTO;
import com.his.entity.CoNotice;
import com.itextpdf.text.DocumentException;

import jakarta.mail.MessagingException;

public interface CoNoticeService {

	CoNotice saveCoNotice(CoNoticeDTO coNoticeDto);

	boolean printCoNotice(Integer appNumber) throws FileNotFoundException, DocumentException, MessagingException;
	
	List<CoNotice> getCoNotices();

}
