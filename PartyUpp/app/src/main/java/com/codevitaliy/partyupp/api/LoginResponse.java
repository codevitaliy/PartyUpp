package com.codevitaliy.partyupp.api;

public class LoginResponse {
    public enum Result {
        Ok,
        WrongEmail,
        WrongPassword,
    }
    public Result result;
    public String token;
    public String userName;
    public Integer profilePicId;
}
