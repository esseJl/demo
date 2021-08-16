package com.example.demo.model.user.token.reset.password.listener;


import com.example.demo.model.user.User;
import com.example.demo.model.user.token.reset.password.complete.event.OnResetPasswordEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResetPasswordEventListener implements ApplicationListener<OnResetPasswordEvent> {

    private IUserResetPassword userService;

    private MessageSource messages;

    private JavaMailSender mailSender;

    @Autowired
    public ResetPasswordEventListener(IUserResetPassword userService, MessageSource messages, JavaMailSender mailSender) {
        this.userService = userService;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnResetPasswordEvent event) {
        this.resetPassword(event);
    }

    @Async
    public void resetPassword(OnResetPasswordEvent event) {

        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createResetPassword(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Password Reset Request :";

        String resetPasswordUrl
                = event.getAppUrl() + "/resetPasswordConfirm/" + token;
        //String message = messages.getMessage("", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("message" + "\r\n" + resetPasswordUrl);
        mailSender.send(email);

    }
}
