package com.todo.app.servicesImp;

import com.todo.app.constants.MailConstants;
import com.todo.app.services.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailServiceImp implements MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    @Async
    public void sendSimpleMail(String to, String subject, String text) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(MailConstants .MAIN_MAIL);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        mailSender.send(simpleMailMessage);

    }

    @Override
    @Async
    public void sendHtmlMail(String to, String subject, String htmlTemplateName, Context contextVariables) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(MailConstants.MAIN_MAIL);
        helper.setTo(to);
        helper.setSubject(subject);

        String htmlTemplate = templateEngine.process(htmlTemplateName, contextVariables);

        helper.setText(htmlTemplate, true);

        mailSender.send(message);

    }

    @Override
    @Async
    public void sendHtmlMailWithAttachment(String to, String subject, String htmlTemplateName, Context contextVariables, List<MultipartFile> attachments) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(MailConstants.MAIN_MAIL);
        helper.setTo(to);
        helper.setSubject(subject);

        String htmlTemplate = templateEngine.process(htmlTemplateName, contextVariables);

        helper.setText(htmlTemplate, true);

        for (MultipartFile attachment: attachments){
            helper.addAttachment(attachment.getOriginalFilename(), attachment);
        }

        mailSender.send(message);

    }

}
