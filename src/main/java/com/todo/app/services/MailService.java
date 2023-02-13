package com.todo.app.services;

import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.List;

public interface MailService {

    void sendSimpleMail(String to, String subject, String text);

    void sendHtmlMail(String to, String subject, String htmlTemplateName, Context contextVariables) throws MessagingException;

    void sendHtmlMailWithAttachment(String to, String subject, String htmlTemplateName, Context contextVariables, List<MultipartFile> attachments) throws MessagingException;

}
