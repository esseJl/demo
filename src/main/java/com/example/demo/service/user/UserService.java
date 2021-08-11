package com.example.demo.service.user;

import com.example.demo.model.role.Role;
import com.example.demo.model.user.User;
import com.example.demo.model.user.email.verification.complete.event.OnRegistrationCompleteEvent;
import com.example.demo.model.user.email.verification.register.listener.IUserService;
import com.example.demo.model.user.email.verification.token.VerificationToken;
import com.example.demo.model.user.reset.password.listener.IUserResetPassword;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.repository.verification.token.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserService implements IUserService, IUserResetPassword {

    private UserRepository userRepository;
    private VerificationTokenRepository tokenRepository;
    private PasswordEncoder encoder;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder,
                       VerificationTokenRepository tokenRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.encoder = encoder;
        this.eventPublisher = eventPublisher;
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

    //--------imp
    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public boolean doesUserHaveToken(User user) {
        Optional<VerificationToken> tokenByUser = tokenRepository.findByUser(user);
        if (!tokenByUser.isEmpty()) {
            if (!tokenByUser.get().getExpiryDate().before(new Date())) {
                return true;
            } else {
                tokenRepository.deleteById(tokenByUser.get().getId());
                return false;
            }
        } else {
            return false;
        }
    }

    public User sendVerifyEmail(User user, HttpServletRequest request) {
        createVerificationToken(user, UUID.randomUUID().toString());
        String appUrl = request.getContextPath();
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
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
        return registered;
    }

    @Override
    public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    @Override
    public void saveRegisteredUser(User user) {
        saveNewUser(user);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
}
