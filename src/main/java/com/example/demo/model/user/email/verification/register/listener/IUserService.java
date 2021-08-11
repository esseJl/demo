package com.example.demo.model.user.email.verification.register.listener;

import com.example.demo.model.user.User;
import com.example.demo.model.user.email.verification.token.VerificationToken;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {
    void createVerificationToken(User user, String token);

    User registerNewUserAccount(User user, HttpServletRequest request);

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    VerificationToken getVerificationToken(String VerificationToken);

    boolean doesUserHaveToken(User user);
}
