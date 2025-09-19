package com.codevitaliy.partyupp.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.codevitaliy.partyupp.MainActivity;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.api.ApiClient;
import com.codevitaliy.partyupp.api.Authentication;
import com.codevitaliy.partyupp.api.LoginRequest;
import com.codevitaliy.partyupp.api.LoginResponse;
import com.codevitaliy.partyupp.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private void launchLogin() {
        Authentication.getInstance().logOut();
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 750);
    }
    private void launchMain() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 750);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Authentication authentication = Authentication.getInstance();

        String email = authentication.getEmail();
        String password = authentication.getPassword();

        if(email == null || password == null){
            launchLogin();
        } else {
            LoginRequest loginRequest = new LoginRequest(email, password);
            Context context = this;
            ApiClient.getInstance().login(loginRequest, new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.code() != 200) {
                        launchLogin();
                    }
                    LoginResponse.Result result = response.body().result;
                    if(result == LoginResponse.Result.Ok){
                        launchMain();
                    } else if(result == LoginResponse.Result.WrongEmail){
                        launchLogin();
                    } else if(result == LoginResponse.Result.WrongPassword){
                         launchLogin();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    launchLogin();
                }
            });
        }
    }
}