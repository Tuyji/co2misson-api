package com.co2nsensus.co2mission.service.impl;

import com.co2nsensus.co2mission.logging.Loggable;
import com.co2nsensus.co2mission.model.entity.ResetPasswordToken;
import com.co2nsensus.co2mission.service.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {

    private final MessageSource messageSource;

    private final JavaMailSenderImpl javaMailSender;

    public EmailSenderServiceImpl(MessageSource messageSource, JavaMailSenderImpl javaMailSender) {
        this.messageSource = messageSource;
        this.javaMailSender = javaMailSender;
    }

    @Value("${support.email}")
    private String supportEmail;


    @Override
    public void sendEmail(HttpServletRequest servletRequest, ResetPasswordToken resetPasswordToken) {
        log.info("EmailSenderServiceImpl.sendEmail started");
        constructResetTokenEmail(servletRequest, resetPasswordToken.getToken(),
                resetPasswordToken.getAffiliate().getEmail());
        log.info("EmailSenderServiceImpl.sendEmail finished");
    }


    private SimpleMailMessage constructResetTokenEmail(HttpServletRequest servletRequest, String token,
                                                       String email) {
        final String url = getAppUrl(servletRequest) + "/affiliate/changePassword?token=" + token;
        final String message = messageSource.getMessage("message.resetPassword",
                null, servletRequest.getLocale());
        return constructEmail("Reset Password", message + " \r\n" + url, email);
    }

    private SimpleMailMessage constructEmail(String subject, String body, String email) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(supportEmail);
        return simpleMailMessage;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath();
    }
}
