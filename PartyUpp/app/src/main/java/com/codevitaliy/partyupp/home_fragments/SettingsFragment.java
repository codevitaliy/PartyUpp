package com.codevitaliy.partyupp.home_fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.api.ApiClient;
import com.codevitaliy.partyupp.api.Authentication;
import com.codevitaliy.partyupp.gallery.ImageManager;
import com.codevitaliy.partyupp.login.LoginActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {

  public SettingsFragment() {
    // Required empty public constructor
  }

  private ActivityResultLauncher<Intent> cameraLauncher;
  private ActivityResultLauncher<Intent> imagePickerLauncher;

  private static final int REQUEST_CAMARA_PERMISSION = 1;
  private static final int REQUEST_POST_NOTIFICATION_PERMISSION = 2;
  TextView textViewProfileUserName;
  Switch switchVolume, switchVibration, switchMusic ;
  ImageView settingsPfpImageView;
  Button buttonLogInOut;
  final String EMPTY_NAME = "";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_settings, container, false);
    textViewProfileUserName = view.findViewById(R.id.textViewProfileUserName);
    switchVolume = view.findViewById(R.id.switchSounds);
    switchMusic = view.findViewById(R.id.switchMusic);
    switchVibration = view.findViewById(R.id.switchVibration);
    buttonLogInOut = view.findViewById(R.id.buttonLogInOut);
    settingsPfpImageView = view.findViewById(R.id.settingsPfpImageView);
    Settings settings = Settings.getInstance();
    switchVolume.setChecked(settings.volume);
    switchVibration.setChecked(settings.vibration);
    switchMusic.setChecked(settings.music); // Ensure the switch reflects the current setting


    if(Authentication.getInstance().isLoggedIn()){

      textViewProfileUserName.setText(Authentication.getInstance().getUserName());
      Integer pfpId = Authentication.getInstance().getProfilePicId();
      if(pfpId != null) {
        ImageManager.getInstance().getImage(pfpId, image -> {
          settingsPfpImageView.post(() -> {
            settingsPfpImageView.setImageDrawable(image);
          });
        });
      }

      buttonLogInOut.setText(getString(R.string.log_out));
      buttonLogInOut.setOnClickListener(v -> {
        Authentication.getInstance().logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        Authentication.getInstance().setToken(null);
        startActivity(intent);
        getActivity().finish();
      });

      imagePickerLauncher = registerForActivityResult(
              new ActivityResultContracts.StartActivityForResult(),
              result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                  Uri uri = result.getData().getData();
                  settingsPfpImageView.setImageURI(uri);

                  // Upload the selected image to the server
                  uploadImageToServer(uri);
                }
              }
      );

    } else {
      textViewProfileUserName.setText(EMPTY_NAME);
      buttonLogInOut.setText(getString(R.string.log_in));
      buttonLogInOut.setOnClickListener(v -> {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
      });
    }

    settingsPfpImageView.setOnClickListener(v -> {
      if (Authentication.getInstance().isLoggedIn()){
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent(intent -> {
                  imagePickerLauncher.launch(intent);
                  return null;
                });
//          if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMARA_PERMISSION);
//          } else {
//            startCamera();
//          }
      } else {
        view.post(() -> {
          showLoginDialog(getString(R.string.profile_pic_ttl), getString(R.string.profile_pic_msg));
        });

      }
    });

    switchVolume.setOnClickListener(v -> {

      boolean isVolumeEnabled = switchVolume.isChecked();
      settings.setVolume(isVolumeEnabled);

      if(!isVolumeEnabled){

        //Toast.makeText(getContext(), "App sounds disabled", Toast.LENGTH_LONG).show();
      } else {
        //Toast.makeText(getContext(), "App sounds activated", Toast.LENGTH_LONG).show();
        settings.playClickSound(getContext());
        //real action here
      }
      if(settings.vibration) settings.activateVibration(getContext());
    });

    switchVibration.setOnClickListener(v -> {
      settings.setVibration(switchVibration.isChecked());
      if(settings.vibration) {
        //Toast.makeText(getContext(), "Vibration activated", Toast.LENGTH_LONG).show();
        settings.activateVibration(getContext()); // Trigger vibration as feedback
      } else {
        //Toast.makeText(getContext(), "Vibration disabled", Toast.LENGTH_LONG).show();
      }
      if(settings.volume) settings.playClickSound(getContext());
    });

    switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
      settings.toggleMusic(getContext()); // Update the music setting based on the switch state
      if (isChecked) {
        //Toast.makeText(getContext(), "Music enabled", Toast.LENGTH_SHORT).show();
      } else {
        //Toast.makeText(getContext(), "Music disabled", Toast.LENGTH_SHORT).show();
      }
      if(settings.vibration) settings.activateVibration(getContext());
    });

    return view;
  }

  private void startCamera() {
    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    cameraLauncher.launch(cameraIntent);
  }

  private void uploadImageToServer(Uri uri) {
    try {
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
      ApiClient.getInstance().postProfilePic(bitmap, new Callback<Integer>() {
        @Override
        public void onResponse(Call<Integer> call, Response<Integer> response) {
          if (response.isSuccessful()) {
            Integer pfpId = Authentication.getInstance().getProfilePicId();
            if (pfpId != null) {
              ImageManager.getInstance().getImage(pfpId, image -> {
                settingsPfpImageView.post(() -> {
                  settingsPfpImageView.setImageDrawable(image);
                });
              });
            }
          } else {
            // Handle the error
            Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
          }
        }

        @Override
        public void onFailure(Call<Integer> call, Throwable t) {
          // Handle the failure
          Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void showLoginDialog(String title, String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
    builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.dialog_login_button), new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish(); // Finish the current activity
              }
            })
            .setNegativeButton(getString(R.string.dialog_accept_button), new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            })
            .setCancelable(false);

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
      positiveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
    }

    Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
    if (negativeButton != null) {
      negativeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
    }
  }



}