package com.example.demo.model.user.token.reset.password.token;

import com.example.demo.model.user.User;
import com.example.demo.model.user.token.TokenAbstract;

import javax.persistence.*;

@Entity
public class PasswordResetToken extends TokenAbstract {
    public PasswordResetToken(String token, User user) {
        super(token, user);
    }
    
    public PasswordResetToken() {
        super();
    }
}
