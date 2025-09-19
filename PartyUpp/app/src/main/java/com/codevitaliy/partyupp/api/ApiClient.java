package com.codevitaliy.partyupp.api;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.codevitaliy.partyupp.gallery.GalleryGameModel;

public class ApiClient {

    private static ApiClient instance;

    private ApiClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Authentication auth = Authentication.getInstance();
                    if(auth.isLoggedIn()){
                        String token = auth.getToken();
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();
                        return chain.proceed(request);
                    } else {
                        return chain.proceed(chain.request());
                    }

                })
                .build();

//      baseURL = "http://10.0.2.2:8080";
        baseURL = "http://PartyUpp.ddns.net:8080";


        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build();
        apiService = retrofit.create(ApiService.class);

        Retrofit noAuthRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build();
        noAuthApiService = noAuthRetrofit.create(ApiService.class);

        auth = Authentication.getInstance();
    }
    public static ApiClient getInstance(){
        if(instance == null){
            instance = new ApiClient();
        }
        return instance;
    }

    private String baseURL;
    private Retrofit retrofit;
    private ApiService apiService;
    private ApiService noAuthApiService;
    private Authentication auth;

    public void login(LoginRequest req, Callback<LoginResponse> callback) {
        auth.setCredentials(req.email, req.password);
        Call<LoginResponse> call = noAuthApiService.login(req);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.code() == 200){
                    auth.setToken(response.body().token);
                    auth.setUserName(response.body().userName);
                    auth.setProfilePicId(response.body().profilePicId);
                    System.out.println(response.body().profilePicId);
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void register(RegistrationRequest req, Callback<RegistrationResponse> callback) {
        auth.setCredentials(req.email, req.password);
        Call<RegistrationResponse> call = noAuthApiService.register(req);
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                if(response.code() == 200){
                    auth.setToken(response.body().token);
                    auth.setUserName(response.body().userName);
                    auth.setProfilePicId(response.body().profilePicId);
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

    public String getBaseURL() { return baseURL; }

    public Response<LoginResponse> refreshToken() throws IOException {
        LoginRequest req = new LoginRequest(auth.getEmail(), auth.getPassword());
        Call<LoginResponse> call = noAuthApiService.login(req);
        Response<LoginResponse> response = call.execute();
        return response;
    }

    public void getUsername(Callback<String> callback) {
        Call<String> call = apiService.getUsername();
        call.enqueue(callback);
    }

    public void getChallenges(Callback<ArrayList<Challenge>> callback) {
        Call<ArrayList<Challenge>> call = noAuthApiService.getChallenges();
        call.enqueue(callback);
    }

    public void getHistory(int page, int pageSize, Callback<ArrayList<GalleryGameModel>> callback) {
        Call<ArrayList<GalleryGameModel>> call = apiService.getHistory(page, pageSize);
        call.enqueue(callback);
    }

    public void getImage(int imageId, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = apiService.getImage(imageId);
        call.enqueue(callback);
    }

    public void startGame(List<String> players,
                            Date startTime,
                            Bitmap startPhoto,
                            Callback<String> callback) {

        MultipartBody.Part playersPart = MultipartBody.Part.createFormData("players", String.join(",", players));
        MultipartBody.Part startTimePart = MultipartBody.Part.createFormData("startTime", Long.toString(startTime.getTime()));

        MultipartBody.Part startPhotoPart = null;
        if(startPhoto != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            startPhoto.compress(Bitmap.CompressFormat.PNG, 100, bos);
            RequestBody initialImageBody = RequestBody.create(MediaType.parse("image/png"), bos.toByteArray());
            startPhotoPart = MultipartBody.Part.createFormData("startPhoto", "startPhoto.png", initialImageBody);
        }

        Call<String> call = apiService.startGame(playersPart, startTimePart, startPhotoPart);
        call.enqueue(callback);
    }

    public void endGame(Date endTime,
                            Bitmap endPhoto,
                            Callback<String> callback) {

        MultipartBody.Part endTimePart = MultipartBody.Part.createFormData("endTime", Long.toString(endTime.getTime()));

        MultipartBody.Part endPhotoPart = null;
        if(endPhoto != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            endPhoto.compress(Bitmap.CompressFormat.PNG, 100, bos);
            RequestBody endPhotoBody = RequestBody.create(MediaType.parse("image/png"), bos.toByteArray());
            endPhotoPart = MultipartBody.Part.createFormData("endPhoto", "endPhoto.png", endPhotoBody);
        }
        Call<String> call = apiService.endGame(endTimePart, endPhotoPart);
        call.enqueue(callback);
    }

    public void postProfilePic(Bitmap profilePic, Callback<Integer> callback) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        profilePic.compress(Bitmap.CompressFormat.PNG, 100, bos);
        RequestBody profilePicBody = RequestBody.create(MediaType.parse("image/png"), bos.toByteArray());
        MultipartBody.Part profilePicPart = MultipartBody.Part.createFormData("profilePic", "profilePic.png", profilePicBody);
        apiService.postProfilePic(profilePicPart).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Authentication.getInstance().setProfilePicId(response.body());
                callback.onResponse(call, response);
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
    public void getSuggestedPlayeres(Callback<ArrayList<String>> callback) {
        apiService.getSuggestedPlayers().enqueue(callback);
    }
}
