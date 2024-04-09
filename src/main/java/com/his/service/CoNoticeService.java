package com.his.service;

import java.io.FileNotFoundException;

import com.his.dto.CoNoticeDTO;
import com.his.entity.CoNotice;
import com.itextpdf.text.DocumentException;

public interface CoNoticeService {

	CoNotice saveCoNotice(CoNoticeDTO coNoticeDto);

	CoNoticeDTO printCoNotice(Integer appNumber) throws FileNotFoundException, DocumentException;

}
