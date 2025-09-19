package com.codevitaliy.partyupp.api;

import com.codevitaliy.partyupp.gallery.GalleryGameModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/api/v1/noAuth/authenticate")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/v1/noAuth/register")
    Call<RegistrationResponse> register(@Body RegistrationRequest registerRequest);

    @GET("/api/v1/noAuth/getChallenges")
    Call<ArrayList<Challenge>> getChallenges();

    // To test authentication
    @GET("/api/v1/user/test/getUsername")
    Call<String> getUsername();

    @GET("/api/v1/user/getHistory")
    Call<ArrayList<GalleryGameModel>> getHistory(@Query("page") int page, @Query("pageSize") int pageSize);

    @GET("/api/v1/user/getImage/{imageId}")
    Call<ResponseBody> getImage(@Path("imageId") int imageId);

    @Multipart
    @POST("/api/v1/user/startGame")
    Call<String> startGame(
            @Part MultipartBody.Part players,
            @Part MultipartBody.Part startTime,
            @Part(encoding = "binary") MultipartBody.Part startPhoto
    );

    @Multipart
    @POST("/api/v1/user/endGame")
    Call<String> endGame(
            @Part MultipartBody.Part endTime,
            @Part(encoding = "binary") MultipartBody.Part endPhoto
    );

    @Multipart
    @POST("/api/v1/user/postProfilePic")
    Call<Integer> postProfilePic(
            @Part(encoding = "binary") MultipartBody.Part profilePic
    );

    @GET("/api/v1/user/suggestedPlayers")
    Call<ArrayList<String>> getSuggestedPlayers();


}
