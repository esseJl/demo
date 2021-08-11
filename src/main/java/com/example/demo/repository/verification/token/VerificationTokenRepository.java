package com.example.demo.repository.verification.token;

import com.example.demo.model.user.User;
import com.example.demo.model.user.email.verification.token.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    Optional<VerificationToken> findByUser(User user);

    void deleteById(Long id);
}
