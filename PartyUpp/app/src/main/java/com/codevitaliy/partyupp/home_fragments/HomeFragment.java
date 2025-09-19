package com.codevitaliy.partyupp.home_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.codevitaliy.partyupp.api.Authentication;
import com.codevitaliy.partyupp.gallery.ImageManager;
import com.codevitaliy.partyupp.player_selection.PlayerSelectionActivity;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.tutorial.TutorialActivity;

public class HomeFragment extends Fragment {

  Button buttonPlay, buttonTutorial;
  ImageView imgViewProfile;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_home, container, false);

    buttonPlay = view.findViewById(R.id.buttonPlay);
    buttonTutorial = view.findViewById(R.id.buttonTutorial);
    imgViewProfile = view.findViewById(R.id.imgViewProfile);


    if (Authentication.getInstance().isLoggedIn()) {

      if (Authentication.getInstance().getProfilePicId() != null) {
        int profilePicId = Authentication.getInstance().getProfilePicId();
        fetchProfileImage(profilePicId);
      } else {
        imgViewProfile.setImageResource(R.drawable.profile);
      }
    } else {
      imgViewProfile.setImageResource(R.drawable.profile);
    }


      imgViewProfile.setOnClickListener(v -> {
        BottomNavigationView navigationView = getActivity().findViewById(R.id.navigationViewMain);
        navigationView.setSelectedItemId(R.id.settingsFragment);
        //View headerView = navigationView.getHeaderView(3);
      });

    buttonTutorial.setEnabled(true);

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
      @Override
      public void onActivityResult(ActivityResult result) {
        if(result.getResultCode() == PlayerSelectionActivity.RESULT_OK){
          Intent data = result.getData();
          //utilizar los extras devueltos si fuese necesario
          //String stringReceived = data.getStringExtra(PlayerSelectionActivity.[CONSTANT]);
          //hacer algo con el objeto recibido
          // ...
        } else if (result.getResultCode() == PlayerSelectionActivity.RESULT_CANCELED){

        } else {
          //otro error
        }

      }
    });

    //Click listener for playing sound

    View.OnClickListener buttonClickListener = v -> {
      // Check if volume is enabled and play
      Settings settings = Settings.getInstance();
      if(settings.volume) {
        settings.playClickSound(getContext());
      }

      if(settings.vibration) {
        settings.activateVibration(getContext());
      }

      if(v.getId() == R.id.buttonPlay) {
        Intent intent = new Intent(requireContext(), PlayerSelectionActivity.class);
        launcher.launch(intent);
      }

      if (v.getId() == R.id.buttonTutorial){
        Intent intent = new Intent(requireContext(), TutorialActivity.class);
        launcher.launch(intent);
      }
    };

    buttonPlay.setOnClickListener(buttonClickListener);
    buttonTutorial.setOnClickListener(buttonClickListener);

    return view;
  }

  private void fetchProfileImage(int imageId) {

    Integer pfpId = Authentication.getInstance().getProfilePicId();
    if(pfpId != null) {
      ImageManager.getInstance().getImage(pfpId, image -> {
        imgViewProfile.post(() -> {
          imgViewProfile.setImageDrawable(image);
        });
      });
    }
  }
}