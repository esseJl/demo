package com.example.demo.model.user.reset.password.listener;


import com.example.demo.model.user.reset.password.complete.event.OnResetPasswordEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
//TODO imp resetPassword
    }
}
