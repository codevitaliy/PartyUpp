package com.codevitaliy.partyupp.gallery;

import android.graphics.drawable.Drawable;

import com.codevitaliy.partyupp.api.ApiClient;
import com.codevitaliy.partyupp.api.Authentication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

// TODO: Store images in internal storage instead
public class ImageManager {

    public interface FetchImageListener {
        void imageFetched(Drawable image);
    }

    private static ImageManager instance;

    private HashMap<Integer, Drawable> images;

    private ImageManager() {
        images = new HashMap<>();
    }

    public static ImageManager getInstance() {
        if(instance == null){
            instance = new ImageManager();
        }
        return instance;
    }

    private void fetchImage(int id, FetchImageListener listener) throws IOException {
        // Can't access network from main thread
        new Thread(() -> {
            Drawable image;
            String baseURL = ApiClient.getInstance().getBaseURL();
            System.out.println(baseURL+"/api/v1/user/getImage/"+id);
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(baseURL+"/api/v1/user/getImage/"+id)
                        .openConnection();
                connection.setRequestProperty("Authorization", "Bearer " + Authentication
                        .getInstance()
                        .getToken());
                connection.connect();

                image = Drawable.createFromStream(connection.getInputStream(),"photo_"+ id);
            } catch(IOException e) {
                e.printStackTrace();
                image = null;
            }
            listener.imageFetched(image);
        }).start();
    }

    public void getImage(int id, FetchImageListener listener) {
        Drawable cachedImage =  images.get(id);
        if(cachedImage == null){
            try {
                fetchImage(id, fetchedImage -> {
                    setImage(id, fetchedImage);
                    listener.imageFetched(fetchedImage);
                });
                return;
            } catch (IOException e) {
                return;
            }
        }
        listener.imageFetched(cachedImage);
    }
    public void setImage(int id, Drawable image) {
        images.put(id, image);
    }

}
