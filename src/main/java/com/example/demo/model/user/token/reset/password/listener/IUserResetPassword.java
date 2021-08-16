package com.example.demo.model.user.token.reset.password.listener;

import com.example.demo.model.user.User;
import com.example.demo.model.user.token.reset.password.token.PasswordResetToken;

import java.util.Optional;

public interface IUserResetPassword {


    boolean doesUserHaveResetPasswordToken(User user);

    User sendResetPasswordEmail(User user, String appUrl);

    void createResetPassword(User user, String token);
    
}
