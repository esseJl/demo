package com.example.demo.model.user.password;

import com.example.demo.annotation.valid.password.ValidPassword;


public class Password {

    @ValidPassword
    private String password;
    
    private String matchingPassword;

    public Password(String password) {
        this.password = password;
    }

    public Password() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
