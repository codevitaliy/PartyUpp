package com.codevitaliy.partyupp;

import android.app.Application;
import android.content.Context;

// This class is used to get the application context for DataStore
public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
