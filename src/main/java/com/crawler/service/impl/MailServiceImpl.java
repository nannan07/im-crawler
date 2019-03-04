package com.crawler.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.crawler.service.MailService;

@Component
public class MailServiceImpl implements MailService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JavaMailSender mailSender;

	@Value("${mail.fromMail.addr}")
	private String from;
	
	@Value("${mail.fromMail.to}")
	private String to;

	@Override
	public void sendSimpleMail(String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to.split(";"));
		message.setSubject(subject);
		message.setText(content);

		try {
			mailSender.send(message);
			logger.info("邮件已经发送。");
		} catch (Exception e) {
			logger.error("发送简单邮件时发生异常！", e);
		}

	}

}
