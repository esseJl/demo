package com.example.demo.model.user.email.verification.token;


import com.example.demo.model.user.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class VerificationToken {
    private static final int EXPIRATION = 10 * 60 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @CreationTimestamp
    private Date creationDate;

    @Transient
    private Date expiryDate;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }


    // standard constructors, getters and setters

    public VerificationToken() {

    }


    public static long getEXPIRATION() {
        return EXPIRATION;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date expiryDate) {
        this.creationDate = expiryDate;
    }

    public Date getExpiryDate() {
        if (creationDate == null)
            this.expiryDate = new Date(1000L);
        long l = creationDate.getTime() + EXPIRATION;
        //this.expiryDate.setTime(l);
        this.expiryDate = new Date(l);
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
