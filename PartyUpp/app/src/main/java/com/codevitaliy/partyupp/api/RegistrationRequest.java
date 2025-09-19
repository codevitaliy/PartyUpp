package com.codevitaliy.partyupp.api;

public class RegistrationRequest {

    public static final String PSSWD_REGEX = "^[\\S]{8,24}$";

    public static final String EMAIL_REGEX = "^\\S+@\\S+\\.\\S+$";

    public static final String USERN_REGEX = "^[A-Za-z0-9_]{2,16}$";

    public enum CheckRegisterResult {
        Valid,
        InvalidName,
        InvalidEmail,
        InvalidPassword,
    }

    public String name;
    public String email;
    public String password;

    public RegistrationRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public CheckRegisterResult check() {
        if (name == null || !name.matches(USERN_REGEX)) {
            return CheckRegisterResult.InvalidName;
        }
        else if (email == null || !email.matches(EMAIL_REGEX)) {
            return CheckRegisterResult.InvalidEmail;
        } else if (password == null || !password.matches(PSSWD_REGEX)) {
            return CheckRegisterResult.InvalidPassword;
        }
        return CheckRegisterResult.Valid;
    }
}
