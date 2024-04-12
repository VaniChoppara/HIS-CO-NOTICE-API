package com.his.utils;

import java.io.File;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	JavaMailSender javaMailSender;
	
	public void sendEmail(String subject, String to, String body, File noticePdf) throws MessagingException{
		
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		//MimeMessageHelper msgHelper= new MimeMessageHelper(mimeMessage);
		
		MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
		msgHelper.setTo(to);
		msgHelper.setSubject(subject);
		msgHelper.setText(body, true);	
		//FileSystemResource file = new FileSystemResource(noticePdf);
		msgHelper.addAttachment(noticePdf.getName(),noticePdf);
		javaMailSender.send(mimeMessage);
	}
}
