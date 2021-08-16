package com.example.demo.model.user.token.email.verification.register.listener;

import com.example.demo.model.user.User;
import com.example.demo.model.user.token.email.verification.token.EmailVerificationToken;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {
    void createVerificationToken(User user, String token);

    User registerNewUserAccount(User user, HttpServletRequest request);

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    EmailVerificationToken getVerificationToken(String VerificationToken);

    boolean doesUserHaveEmailToken(User user);
}
