package com.example.demo.repository.token.email.verification;

import com.example.demo.model.user.User;
import com.example.demo.model.user.token.email.verification.token.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationToken, Long> {

    EmailVerificationToken findByToken(String token);

    Optional<EmailVerificationToken> findByUser(User user);

    void deleteById(Long id);
}
