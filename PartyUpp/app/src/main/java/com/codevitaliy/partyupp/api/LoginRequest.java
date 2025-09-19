package com.codevitaliy.partyupp.api;

public class LoginRequest {
    public enum CheckLoginResult {
        Valid,
        InvalidEmail,
        InvalidPassword,
    }

    public String email;
    public String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public CheckLoginResult check() {
        if(!email.matches("^\\S+@\\S+\\.\\S+$")){
            return CheckLoginResult.InvalidEmail;
        } else if(password.isEmpty()) {
            return CheckLoginResult.InvalidPassword;
        }
        return CheckLoginResult.Valid;
    }
}
