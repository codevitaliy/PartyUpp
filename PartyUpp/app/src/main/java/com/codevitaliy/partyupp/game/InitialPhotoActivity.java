package com.codevitaliy.partyupp.game;

import static com.codevitaliy.partyupp.App.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.api.ApiClient;
import com.codevitaliy.partyupp.home_fragments.Settings;
import com.codevitaliy.partyupp.player_selection.PlayerSelectionActivity;

import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitialPhotoActivity extends AppCompatActivity {

    ImageButton imageButtonCamera;
    Button buttonLaunchGame;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private static final int REQUEST_CAMARA_PERMISSION = 1;
    private static final int REQUEST_POST_NOTIFICATION_PERMISSION = 2;
    private Uri imageUri;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_initial_photo);
        Intent intent = getIntent();
        ArrayList<String> receivedPlayersNameList = intent.getStringArrayListExtra(PlayerSelectionActivity.INFO_PLAYERS_LIST);

        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        buttonLaunchGame = findViewById(R.id.buttonLaunchGame);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        imageButtonCamera.setImageURI(uri);
                        imageUri = uri;
                    }
                }
        );

        imageButtonCamera.setOnClickListener(v -> {
            Settings settings = Settings.getInstance();
            settings.playClickSound(v.getContext());
            settings.activateVibration(v.getContext());

            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent(i -> {
                        imagePickerLauncher.launch(i);
                        return null;
                    });

        });

        buttonLaunchGame.setOnClickListener(v -> {
            Settings settings = Settings.getInstance();
            if(settings.volume) {
                settings.playClickSound(getContext());
            }

            if(settings.vibration) {
                settings.activateVibration(getContext());
            }

            uploadGame(imageUri, receivedPlayersNameList, new Date());

            Intent i = new Intent(InitialPhotoActivity.this, GameActivity.class);
            i.putExtra(PlayerSelectionActivity.INFO_PLAYERS_LIST, receivedPlayersNameList);
            startActivity(i);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMARA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                showDialog(null, getString(R.string.camera_perm_required));
            }
        } else if (requestCode == REQUEST_POST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendLaunchNotification(); // Permission granted, send the notification
            } else {
                showDialog(null, getString(R.string.notif_perm_required));
            }
        }
    }

    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    //NOTIFICATIONS

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name); // Define the channel name.
            String description = getString(R.string.channel_description); // Define the channel description.
            String channelId = getString(R.string.channel_id); // Define the channel ID.
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void sendLaunchNotification() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            createNotificationChannel(); // Make sure this is called before sending a notification.

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Welcome to PartyUpp!") // Custom title
                    .setContentText("We hope you have a great time, remember drink responsibly :D") // Custom text
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_POST_NOTIFICATION_PERMISSION);
        }
    }

    private void showDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(InitialPhotoActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_ok_button), null)
                .setCancelable(true);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        TextView titleView = alertDialog.findViewById(getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setTextColor(Color.WHITE);
        }

        TextView messageView = alertDialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setTextColor(Color.WHITE);
        }

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(ContextCompat.getColor(InitialPhotoActivity.this, R.color.blue));
        }

        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(ContextCompat.getColor(InitialPhotoActivity.this, R.color.blue));
        }
    }

    private void uploadGame(Uri imageUri, List<String> playerList, Date startTime) {
        try {
            Bitmap bitmap;
            if(imageUri == null){
                bitmap = null;
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            }
            ApiClient.getInstance().startGame(playerList, startTime , bitmap, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Handle the failure
                    // TODO: Fix malformed json error
                    //t.printStackTrace();
                    //Toast.makeText(InitialPhotoActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
