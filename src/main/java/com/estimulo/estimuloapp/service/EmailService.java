/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

public interface EmailService {

  /**
   * Call the SMTP server to send an email
   *
   * @param toEmail The receiver's email address
   * @param subject The email subject
   * @param body The email body
   * @param formatHtml True whether the email should be formatted as html
   */
  void sendEmail(String toEmail, String subject, String body, Boolean formatHtml);
}
