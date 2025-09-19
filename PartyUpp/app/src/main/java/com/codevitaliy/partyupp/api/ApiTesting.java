package com.codevitaliy.partyupp.api;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.codevitaliy.partyupp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ApiTesting {
    public static void testPostGameLog(Resources resources){
        Bitmap bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.profile);
        Bitmap bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.profile);

        ArrayList<String> players = new ArrayList<>();
        players.add("Paco");
        players.add("Manolo");
        players.add("Juan");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2024);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 20);
        Date startTime = cal.getTime();
        cal.set(Calendar.HOUR, 6);
        Date endTime = cal.getTime();

        /*ApiClient.getInstance().postGameLog(players, startTime, endTime, bitmap1, bitmap2, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        */
    }
}
