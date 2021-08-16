package com.example.demo.model.user.token.email.verification.token;


import com.example.demo.model.user.User;
import com.example.demo.model.user.token.TokenAbstract;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class EmailVerificationToken extends TokenAbstract {

    public EmailVerificationToken(String token, User user) {
        super(token, user);
    }


    public EmailVerificationToken() {
        super();
    }
}
