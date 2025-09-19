package com.codevitaliy.partyupp.api;

public class RegistrationResponse {
    public enum Result {
        Ok,
        EmailInUse,
    }
    public Result result;
    public String token;
    public String userName;
    public Integer profilePicId;
}
