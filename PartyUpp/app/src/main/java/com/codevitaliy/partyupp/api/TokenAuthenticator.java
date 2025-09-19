package com.codevitaliy.partyupp.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {

    private ApiClient client;

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) throws IOException {
        // Refresh your access_token using a synchronous api request

        try{
            retrofit2.Response<LoginResponse> refreshResponse = client.refreshToken();
            if(refreshResponse.isSuccessful()){
                Authentication.getInstance().setToken(refreshResponse.body().token);
                // Añadir un header nuevo a la peticion rechazada y reintentarlo
                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + Authentication.getInstance().getToken())
                        .build();
            } else {
                // TODO: Handle authentication error
            }
        } catch(IOException e) {
            // TODO: Handle IO error

            throw e;
        }
        return response.request();
    }
}
