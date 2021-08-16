package com.example.demo.service.user;

import com.example.demo.model.role.Role;
import com.example.demo.model.user.User;
import com.example.demo.model.user.token.email.verification.complete.event.OnRegistrationCompleteEvent;
import com.example.demo.model.user.token.email.verification.register.listener.IUserService;
import com.example.demo.model.user.token.email.verification.token.EmailVerificationToken;
import com.example.demo.model.user.token.reset.password.complete.event.OnResetPasswordEvent;
import com.example.demo.model.user.token.reset.password.listener.IUserResetPassword;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.repository.token.email.verification.EmailVerificationRepository;
import com.example.demo.service.token.reset.password.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserService implements IUserService, IUserResetPassword {

    private UserRepository userRepository;
    //TODO imp service for emailTokenRepository
    private EmailVerificationRepository emailTokenRepository;
    private PasswordResetService passwordResetService;
    private PasswordEncoder encoder;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder,
                       EmailVerificationRepository emailTokenRepository,
                       ApplicationEventPublisher eventPublisher, PasswordResetService passwordResetService) {
        this.userRepository = userRepository;
        this.emailTokenRepository = emailTokenRepository;
        this.encoder = encoder;
        this.eventPublisher = eventPublisher;
        this.passwordResetService = passwordResetService;
    }


    public boolean isUserPresentWithUserName(String userName) {
        Optional<User> byUserName = userRepository.findByUserName(userName);
        return byUserName.isPresent();
    }

    public boolean isUserPresentWithEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.isPresent();
    }

    public User findUserByEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.orElseThrow();
    }


    public User saveNewUser(User user) {
        return userRepository.save(user);
    }

    //--------imp IUserResetPassword


    @Override
    public void createResetPassword(User user, String token) {
        passwordResetService.CreateResetPasswordForUser(user, token);
    }

    @Override
    public boolean doesUserHaveResetPasswordToken(User user) {
        return passwordResetService.UserHasResetPasswordToken(user);
    }

    @Override
    public User sendResetPasswordEmail(User user, String appUrl) {
        //passwordResetService.CreateResetPasswordForUser(user);
        eventPublisher.publishEvent(new OnResetPasswordEvent(user, appUrl));
        return user;
    }

    //--------imp
    @Override
    public void createVerificationToken(User user, String token) {
        EmailVerificationToken myToken = new EmailVerificationToken(token, user);
        emailTokenRepository.save(myToken);
    }

    @Override
    public boolean doesUserHaveEmailToken(User user) {
        Optional<EmailVerificationToken> tokenByUser = emailTokenRepository.findByUser(user);
        if (tokenByUser.isEmpty()) {
            return false;
        }
        if (tokenByUser.get().getExpiryDate().before(new Date())) {
            emailTokenRepository.deleteById(tokenByUser.get().getId());
            return false;
        }
        return true;
    }

    public User sendVerifyEmail(User user, HttpServletRequest request) {
        //createVerificationToken(user, UUID.randomUUID().toString());
        String appUrl =
                "http://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));
        return user;
    }

    @Override
    public User registerNewUserAccount(User user, HttpServletRequest request) {
        User newUser = new User();

        Role role = new Role("USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        newUser.setRoles(roles);

        newUser.setUserUUID(UUID.randomUUID().toString());

        newUser.setUserName(user.getUserName());
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(encoder.encode(user.getPassword()));

        newUser.setEnable(false);

        User registered = saveNewUser(newUser);
        //important appUrl
        String appUrl =
                "http://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
        return registered;
    }

    @Override
    public User getUser(String verificationToken) {
        User user = emailTokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    @Override
    public void saveRegisteredUser(User user) {
        saveNewUser(user);
    }

    @Override
    public EmailVerificationToken getVerificationToken(String VerificationToken) {
        return emailTokenRepository.findByToken(VerificationToken);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
