/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.exception.InternalServerErrorException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@Log4j2
public class EmailServiceImpl implements EmailService {

  private final String emailFromName;
  private final String mailFromName;
  private JavaMailSender mailSender;

  public EmailServiceImpl(
      @Value("${mail.email.fromName}") String emailFromName,
      @Value("${spring.mail.username}") String mailFromName,
      JavaMailSender mailSender) {
    this.mailSender = mailSender;
    this.emailFromName = emailFromName;
    this.mailFromName = mailFromName;
  }

  /** {@inheritDoc} */
  public void sendEmail(String toEmail, String subject, String body, Boolean formatHtml) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(mimeMessage, true);
      helper.setFrom(mailFromName, emailFromName);
      helper.setTo(toEmail);
      helper.setText(body, formatHtml);
      helper.setSubject(subject);
      mailSender.send(mimeMessage);
    } catch (MessagingException e) {
      log.error("Error while sending the email", e);
      throw new InternalServerErrorException();
    } catch (UnsupportedEncodingException e) {
      log.error("Unsupported encoding exception", e);
      throw new InternalServerErrorException();
    }
  }
}
