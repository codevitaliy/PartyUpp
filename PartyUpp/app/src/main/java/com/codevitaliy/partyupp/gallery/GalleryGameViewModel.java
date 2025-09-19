package com.codevitaliy.partyupp.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codevitaliy.partyupp.api.ApiClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryGameViewModel extends ViewModel {
    private static final int PAGE_SIZE = 10;
    private int currentPage = 0;

    private MutableLiveData<ArrayList<GalleryGameModel>> gameList;

    public LiveData<ArrayList<GalleryGameModel>> getGameList(){
        if(gameList == null) {
            gameList = new MutableLiveData<>();
            gameList.setValue(new ArrayList<>());
        }
        refresh();
        return gameList;
    }

    public void loadNextPage() {
        currentPage++;
        loadPage();
    }

    private void refresh() {
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getHistory(currentPage, PAGE_SIZE, new Callback<ArrayList<GalleryGameModel>>() {
            @Override
            public void onResponse(Call<ArrayList<GalleryGameModel>> call, Response<ArrayList<GalleryGameModel>> response) {
                if(response.isSuccessful()){
                    ArrayList<GalleryGameModel> currentList = gameList.getValue();

                    gameList.postValue(response.body());
                } else {
                    // Código de estado no es 200
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GalleryGameModel>> call, Throwable t) {
                // Error de conexión / Error de deserialización
            }
        });
    }

    private void loadPage() {
        ApiClient apiClient = ApiClient.getInstance();
        apiClient.getHistory(currentPage, PAGE_SIZE, new Callback<ArrayList<GalleryGameModel>>() {
            @Override
            public void onResponse(Call<ArrayList<GalleryGameModel>> call, Response<ArrayList<GalleryGameModel>> response) {
                if(response.isSuccessful()){
                    ArrayList<GalleryGameModel> currentList = gameList.getValue();
                    //System.out.println(response);
                    currentList.addAll(response.body());
                    gameList.postValue(currentList);
                } else {
                    // Código de estado no es 200
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GalleryGameModel>> call, Throwable t) {
                // Error de conexión / Error de deserialización
            }
        });
    }
}
