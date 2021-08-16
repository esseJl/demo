package com.example.demo.service.token.reset.password;

import com.example.demo.model.user.User;
import com.example.demo.model.user.token.reset.password.token.PasswordResetToken;
import com.example.demo.repository.token.reset.password.PasswordResetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PasswordResetService {

    private PasswordResetRepository passwordResetRepository;

    @Autowired
    public PasswordResetService(PasswordResetRepository passwordResetRepository) {
        this.passwordResetRepository = passwordResetRepository;
    }

    public void CreateResetPasswordForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetRepository.save(passwordResetToken);
    }

    public boolean UserHasResetPasswordToken(User user) {
        Optional<PasswordResetToken> tokenByUser = passwordResetRepository.findByUser(user);
        if (tokenByUser.isEmpty()) {
            return false;
        }
        if (tokenByUser.get().getExpiryDate().before(new Date())) {
            passwordResetRepository.deleteById(tokenByUser.get().getId());
            return false;
        }
        return true;
    }


    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetRepository.findByToken(token);
    }


    public void deleteById(Long id) {
        passwordResetRepository.deleteById(id);
    }
}
