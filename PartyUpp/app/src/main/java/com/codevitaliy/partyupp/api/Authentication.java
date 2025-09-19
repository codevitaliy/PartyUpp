package com.codevitaliy.partyupp.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.codevitaliy.partyupp.App;

public class Authentication {

    private static Authentication instance;

    private Authentication() {
        SharedPreferences preferences  = App.getContext()
                .getSharedPreferences("auth", Context.MODE_PRIVATE);

        String savedEmail = preferences.getString("email", null);
        if(savedEmail != null) {
            email = savedEmail;
        }
        String savedPassword = preferences.getString("password", null);
        if(savedPassword != null) {
            password = savedPassword;
        }
    }

    public static Authentication getInstance() {
        if(instance == null){
            instance = new Authentication();
        }
        return instance;
    }

    private String email;
    private String password;
    private String userName;
    private String token;
    private Integer profilePicId;

    public boolean isLoggedIn(){
        //return email != null && password == null;
        return token != null;
    }

    public void setCredentials(String email, String password) {

        SharedPreferences preferences  = App.getContext()
                .getSharedPreferences("auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("password",password);
        editor.apply();

        this.email = email;
        this.password = password;
    }

    public void logOut() {
        token = null;
        email = null;
        password = null;
        userName = null;
        SharedPreferences preferences  = App.getContext()
                .getSharedPreferences("auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.apply();

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public Integer getProfilePicId() { return profilePicId; }

    public void setProfilePicId(Integer profilePicId) { this.profilePicId = profilePicId; }
    synchronized public String getToken() {
        return token;
    }
    synchronized public void setToken(String token) {
        this.token = token;
    }
}
