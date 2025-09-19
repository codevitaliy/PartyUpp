package com.codevitaliy.partyupp.gallery;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.codevitaliy.partyupp.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GalleryGameDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to be fullscreen:
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_detail);

        // Retrieve data from the intent
        Intent intent = getIntent();
        int gameId = intent.getIntExtra("gameId", 0);
        int startPhotoId = intent.getIntExtra("startPhotoId", -1);
        int endPhotoId = intent.getIntExtra("endPhotoId", -1);
        ArrayList<String> players = intent.getStringArrayListExtra("players");
        Date startTime = (Date) intent.getSerializableExtra("startTime");
        Date endTime = (Date) intent.getSerializableExtra("endTime");

        // Initialize UI elements
        ImageView imageViewDetailPhoto = findViewById(R.id.imageViewDetailPhoto);
        TextView textViewDetailPlayers = findViewById(R.id.textViewDetailPlayers);
        TextView textDetailViewDateTime = findViewById(R.id.textDetailViewDateTime);
        ImageView imageViewDetailPhoto2 = findViewById(R.id.imageViewDetailPhoto2);
        TextView textViewInitialPhoto = findViewById(R.id.textViewInitialPhoto);
        TextView textViewFinalPhoto = findViewById(R.id.textViewFinalPhoto);
        CardView cardViewInitialPhoto = findViewById(R.id.cardViewInitialPhoto);
        CardView cardViewFinalPhoto = findViewById(R.id.cardViewFinalPhoto);

        // Set data to UI elements
        if (startPhotoId != -1) {
            ImageManager.getInstance().getImage(startPhotoId, image -> {
                runOnUiThread(() -> {
                    imageViewDetailPhoto.setImageDrawable(image);
                    textViewInitialPhoto.setVisibility(View.VISIBLE);
                });
            });
        } else {
            textViewInitialPhoto.setVisibility(View.GONE);
            imageViewDetailPhoto.setVisibility(View.GONE);
        }

        textViewDetailPlayers.setText(String.join(", ", players));
        // Assuming game.getStartTime() returns a java.util.Date
        Date dateStartTime = startTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(dateStartTime);
        textDetailViewDateTime.setText(formattedDate);

        if (endPhotoId != -1) {
            ImageManager.getInstance().getImage(endPhotoId, image -> {
                runOnUiThread(() -> {
                    imageViewDetailPhoto2.setImageDrawable(image);
                    textViewFinalPhoto.setVisibility(View.VISIBLE);
                });
            });
        } else {
            textViewFinalPhoto.setVisibility(View.GONE);
            imageViewDetailPhoto2.setVisibility(View.GONE);

        }
    }

    private void adjustCardViewDimensions(CardView cardView, ImageView imageView, Drawable image) {
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                int imageWidth = image.getIntrinsicWidth();
                int imageHeight = image.getIntrinsicHeight();
                float imageRatio = (float) imageWidth / imageHeight;

                // Calculate maximum allowed dimensions for the CardView
                int maxWidth = getResources().getDisplayMetrics().widthPixels;
                int maxHeight = getResources().getDisplayMetrics().heightPixels;

                // Calculate CardView dimensions based on image ratio
                int cardWidth = imageView.getWidth();
                int cardHeight = Math.round(cardWidth / imageRatio);

                // Limit CardView dimensions to maximum allowed dimensions
                if (cardWidth > maxWidth) {
                    cardWidth = maxWidth;
                    cardHeight = Math.round(cardWidth / imageRatio);
                }
                if (cardHeight > maxHeight) {
                    cardHeight = maxHeight;
                    cardWidth = Math.round(cardHeight * imageRatio);
                }

                // Set CardView dimensions
                cardView.getLayoutParams().width = cardWidth;
                cardView.getLayoutParams().height = cardHeight;
                cardView.requestLayout();
                return true;
            }
        });
    }

}

