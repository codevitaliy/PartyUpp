package com.codevitaliy.partyupp.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.home_fragments.Settings;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GalleryGameAdapter extends RecyclerView.Adapter<GalleryGameAdapter.ViewHolder> {
    private ArrayList<GalleryGameModel> gameList;
    private Context context; // Add context to access resources and start activities
    private ProgressBar progressBar;

    public GalleryGameAdapter(ArrayList<GalleryGameModel> gameList, Context context) {

        this.gameList = gameList;
        this.context = context;
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define your UI elements here
        public ImageView imageViewGame;
        TextView textViewPlayers;
        TextView textViewDateTime;
        ProgressBar progressBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize UI elements
            imageViewGame = itemView.findViewById(R.id.imageViewGamePicture);
            // You should replace R.id.imageViewGame with the actual ID used in your item layout
            textViewPlayers = itemView.findViewById(R.id.textViewPlayers);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            progressBar = itemView.findViewById(R.id.itemProgressBar);

            // Set default values or placeholders
            imageViewGame.setImageResource(R.color.white); // Set a placeholder image
            textViewPlayers.setText(""); // Set a loading placeholder text
            textViewDateTime.setText(""); // Set an empty string for the date and time

            // Set ProgressBar initially visible
            progressBar.setVisibility(View.VISIBLE);
            // Hide TextViews initially
            textViewPlayers.setVisibility(View.INVISIBLE);
            textViewDateTime.setVisibility(View.INVISIBLE);

        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_game, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to UI elements
        GalleryGameModel game = gameList.get(position);

        // Set ProgressBar initially visible
        holder.progressBar.setVisibility(View.VISIBLE);

        // Load image into ImageView
        if (game.getStartPhotoId() != null) {
            ImageManager.getInstance().getImage(game.getStartPhotoId(), image -> {
                holder.imageViewGame.post(() -> {
                    BitmapDrawable drawable = (BitmapDrawable) image;
                    Bitmap bitmap = drawable.getBitmap();
                    holder.imageViewGame.setImageBitmap(bitmap);

                    // Set dimensions of the ImageView to maintain 1:1 aspect ratio
                    holder.imageViewGame.getLayoutParams().width = 150; // Set the desired width
                    holder.imageViewGame.getLayoutParams().height = 150; // Set the desired height

                    // Hide ProgressBar once image is loaded
                    holder.progressBar.setVisibility(View.GONE);

                    // Show TextViews after image is loaded
                    holder.textViewPlayers.setVisibility(View.VISIBLE);
                    holder.textViewDateTime.setVisibility(View.VISIBLE);
                });
            });
        } else if (game.getEndPhotoId() != null) {
            ImageManager.getInstance().getImage(game.getEndPhotoId(), image -> {
                holder.imageViewGame.post(() -> {
                    BitmapDrawable drawable = (BitmapDrawable) image;
                    Bitmap bitmap = drawable.getBitmap();
                    holder.imageViewGame.setImageBitmap(bitmap);

                    // Set dimensions of the ImageView to maintain 1:1 aspect ratio
                    holder.imageViewGame.getLayoutParams().width = 150; // Set the desired width
                    holder.imageViewGame.getLayoutParams().height = 150; // Set the desired height


                    // Hide ProgressBar once image is loaded
                    holder.progressBar.setVisibility(View.GONE);

                    // Show TextViews after image is loaded
                    holder.textViewPlayers.setVisibility(View.VISIBLE);
                    holder.textViewDateTime.setVisibility(View.VISIBLE);
                });
            });
        } else {
            // Set default image drawable
            holder.imageViewGame.setImageResource(R.drawable.splash);

            // Set dimensions of the ImageView to maintain 1:1 aspect ratio
            holder.imageViewGame.getLayoutParams().width = 150; // Set the desired width
            holder.imageViewGame.getLayoutParams().height = 150; // Set the desired height


            // Hide ProgressBar once image is loaded
            holder.progressBar.setVisibility(View.GONE);

            // Show TextViews after image is loaded
            holder.textViewPlayers.setVisibility(View.VISIBLE);
            holder.textViewDateTime.setVisibility(View.VISIBLE);
        }

        holder.textViewPlayers.setText(TextUtils.join(", ", game.getPlayers()));

        // Formatting Date
        Date startDate = game.getStartTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(startDate);
        holder.textViewDateTime.setText(formattedDate);

        // Set OnClickListener to start GameDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Settings settings = Settings.getInstance();
            if (settings.volume) {
                settings.playClickSound(context);
            }
            if (settings.vibration) {
                settings.activateVibration(context);
            }

            // Create an Intent to start GameDetailActivity
            Intent intent = new Intent(v.getContext(), GalleryGameDetailActivity.class);

            // Pass data as extras to the intent
            intent.putExtra("gameId", game.getGameId());
            intent.putExtra("players", game.getPlayers());
            intent.putExtra("startPhotoId", game.getStartPhotoId() != null ? game.getStartPhotoId() : -1);
            intent.putExtra("endPhotoId", game.getEndPhotoId() != null ? game.getEndPhotoId() : -1);
            intent.putExtra("startTime", game.getStartTime());
            intent.putExtra("endTime", game.getEndTime());

            // Start the activity
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public void setData(ArrayList<GalleryGameModel> data) {
        this.gameList = data;
        notifyDataSetChanged();
    }
}

