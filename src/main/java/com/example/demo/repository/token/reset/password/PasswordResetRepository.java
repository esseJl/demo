package com.example.demo.repository.token.reset.password;

import com.example.demo.model.user.User;
import com.example.demo.model.user.token.reset.password.token.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);


    Optional<PasswordResetToken> findByUser(User user);

}
