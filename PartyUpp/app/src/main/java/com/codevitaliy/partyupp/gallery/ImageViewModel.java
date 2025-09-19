package com.codevitaliy.partyupp.gallery;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codevitaliy.partyupp.api.ApiClient;
import com.codevitaliy.partyupp.api.Authentication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageViewModel extends ViewModel {

    private MutableLiveData<Drawable> image;

    public LiveData<Drawable> getImage(int imageId) {
        if(image == null){
            image = new MutableLiveData<>();
            fetchImage(imageId);
        }
        return image;
    }

    private void fetchImage(int imageId) {
        new Thread(() -> {
            String baseURL = ApiClient.getInstance().getBaseURL();
            Drawable drawable;
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(baseURL + "/api/v1/user/getImage/" + imageId)
                        .openConnection();
                connection.setRequestProperty("Authorization", "Bearer " + Authentication
                        .getInstance()
                        .getToken());
                connection.connect();
                drawable = Drawable.createFromStream(connection.getInputStream(), "photo_" + imageId);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                drawable = null;
            }
            image.postValue(drawable);
        }).start();
    }
}


/*


 */