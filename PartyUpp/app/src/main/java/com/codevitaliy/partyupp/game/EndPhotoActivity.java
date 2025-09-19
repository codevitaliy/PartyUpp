package com.codevitaliy.partyupp.game;

import static com.codevitaliy.partyupp.App.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.codevitaliy.partyupp.MainActivity;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.api.ApiClient;
import com.codevitaliy.partyupp.home_fragments.Settings;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EndPhotoActivity extends AppCompatActivity {

    ImageButton imageButtonEndCamera;
    Button buttonSaveAndFinish;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private ActivityResultLauncher<Intent> cameraLauncher;

    private Uri imageUri;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_photo);

        imageButtonEndCamera = findViewById(R.id.imageButtonEndCamera);
        buttonSaveAndFinish = findViewById(R.id.buttonSaveAndFinish);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        imageButtonEndCamera.setImageURI(uri);
                        imageUri = uri;
                    }
                }
        );

        imageButtonEndCamera.setOnClickListener(v -> {
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

        buttonSaveAndFinish.setOnClickListener(v -> {
            Settings settings = Settings.getInstance();
            if(settings.volume) {
                settings.playClickSound(getContext());
            }

            if(settings.vibration) {
                settings.activateVibration(getContext());
            }
            uploadGame(imageUri, new Date());
            Intent intent = new Intent(EndPhotoActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack and start MainActivity as a new task
            startActivity(intent);
            finish();
        });
    }

    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    @Override
    public void onBackPressed() {
        // Override onBackPressed to prevent returning to this activity when pressing the back button from MainActivity
        Intent intent = new Intent(EndPhotoActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EndPhotoActivity.this);
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
            positiveButton.setTextColor(ContextCompat.getColor(EndPhotoActivity.this, R.color.blue));
        }

        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(ContextCompat.getColor(EndPhotoActivity.this, R.color.blue));
        }
    }

    private void uploadGame(Uri imageUri, Date endTime) {
        try {
            Bitmap bitmap;
            if(imageUri == null){
                bitmap = null;
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            }

            ApiClient.getInstance().endGame(endTime, bitmap, new Callback<String>() {
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
