package com.example.demo.model.user.token.email.verification.register.listener;

import com.example.demo.model.user.User;
import com.example.demo.model.user.token.email.verification.complete.event.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {


    private IUserService userService;

    private MessageSource messages;

    private JavaMailSender mailSender;

    @Autowired
    public RegistrationListener(IUserService service, MessageSource messages, JavaMailSender mailSender) {
        this.userService = service;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    @Async
    protected void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";

        String confirmationUrl
                = event.getAppUrl() + "/verificationRegisterConfirm?token=" + token;
        //String message = messages.getMessage("", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("message" + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }
}
