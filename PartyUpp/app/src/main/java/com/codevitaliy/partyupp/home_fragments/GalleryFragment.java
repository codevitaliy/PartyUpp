package com.codevitaliy.partyupp.home_fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.codevitaliy.partyupp.api.Authentication;
import com.codevitaliy.partyupp.gallery.GalleryGameAdapter;
import com.codevitaliy.partyupp.gallery.GalleryGameModel;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.gallery.GalleryGameViewModel;
import com.codevitaliy.partyupp.gallery.ImageManager;
import com.codevitaliy.partyupp.login.LoginActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

public class GalleryFragment extends Fragment {

  private RecyclerView recyclerView;
  private GalleryGameAdapter galleryGameAdapter;
  private GalleryGameViewModel gamesViewModel;
  private TextView textViewGalleryTitle, textViewNoGamesPlayed;
  private ImageView imageViewUserPhoto;
  private Handler handler = new Handler();
  private boolean isGameListEmpty = true;



  public GalleryFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_gallery, container, false);

    textViewNoGamesPlayed = view.findViewById(R.id.textViewNoGamesPlayed);
    textViewGalleryTitle = view.findViewById(R.id.textViewGalleryTitle);
    imageViewUserPhoto = view.findViewById(R.id.imageViewUserPhoto);  // Make sure you have an ImageView in your layout
    textViewNoGamesPlayed.setVisibility(View.INVISIBLE);

    if (Authentication.getInstance().isLoggedIn()) {
      recyclerView = view.findViewById(R.id.recyclerView);
      gamesViewModel = new ViewModelProvider(this).get(GalleryGameViewModel.class);
      galleryGameAdapter = new GalleryGameAdapter(new ArrayList<>(), getActivity());
      recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      recyclerView.setAdapter(galleryGameAdapter);
      if (Authentication.getInstance().getProfilePicId() != null){
        int profilePicId = Authentication.getInstance().getProfilePicId();
        fetchProfileImage(profilePicId);
      } else {
        imageViewUserPhoto.setImageResource(R.drawable.profile);
      }

      textViewGalleryTitle.setText(R.string.gallery_menu_ttl);

      imageViewUserPhoto.setOnClickListener(v -> {
        BottomNavigationView navigationView = getActivity().findViewById(R.id.navigationViewMain);
        navigationView.setSelectedItemId(R.id.settingsFragment);
        //View headerView = navigationView.getHeaderView(3);
      });

      // Update the observer to set a delay before updating the visibility
      gamesViewModel.getGameList().observe(getViewLifecycleOwner(), gameList -> {
        if (gameList == null || gameList.isEmpty()) {
          isGameListEmpty = true;
        } else {
          isGameListEmpty = false;
          galleryGameAdapter.setData(gameList);
        }
        // Update visibility with a delay
        handler.postDelayed(this::updateNoGamesPlayedVisibility, 500); // Delay of 500 milliseconds
      });

    } else {
      showDialogRestrictingAccess(getString(R.string.registr_locked_ttl), getString(R.string.registr_locked_msg));
    }

    return view;
  }

  private void fetchProfileImage(int imageId) {

    Integer pfpId = Authentication.getInstance().getProfilePicId();
    if(pfpId != null) {
      ImageManager.getInstance().getImage(pfpId, image -> {
        imageViewUserPhoto.post(() -> {
          imageViewUserPhoto.setImageDrawable(image);
        });
      });
    }
  }

  @Override
  public void onResume()
  {
    if(Authentication.getInstance().isLoggedIn() && gamesViewModel != null){
      gamesViewModel.getGameList();
    }

    super.onResume();
  }

  private ArrayList<GalleryGameModel> getSampleData() {
    // Simulate data fetched from an API

    ArrayList<GalleryGameModel> sampleData = new ArrayList<>();
    /*
    sampleData.add(new GalleryGameModel(1, R.drawable.game1photo, "Alejandro\nMaría\nJavichu", "2024-03-01 12:00 PM"));
    sampleData.add(new GalleryGameModel(2, R.drawable.game2photo, "María Rodríguez\nLope\nChechu\ny 1 más...", "2023-06-02 3:30 PM"));
    sampleData.add(new GalleryGameModel(3, R.drawable.game3photo, "Diana\nMiriam\nVane", "2023-05-02 3:30 PM"));
    sampleData.add(new GalleryGameModel(4, R.drawable.game4photo, "Laura\nJohn\nAlex\ny 2 más...", "2023-03-02 3:30 PM"));
    sampleData.add(new GalleryGameModel(5, R.drawable.game5photo, "Carl\nAnne\nJohn\ny 2 más...", "2023-02-02 3:30 PM"));
    sampleData.add(new GalleryGameModel(6, R.drawable.game6photo, "Isabel\nMaría\nJuana", "2023-01-02 3:30 PM"));
    */

    return sampleData;
  }

  private void showDialogRestrictingAccess(String title, String message) {
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
                NavController navController = ((NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentMain)).getNavController();
                navController.navigate(R.id.homeFragment);
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

  private void updateNoGamesPlayedVisibility() {
    if (isGameListEmpty) {
      textViewNoGamesPlayed.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.GONE); // Hide RecyclerView if no games
    } else {
      textViewNoGamesPlayed.setVisibility(View.GONE);
      recyclerView.setVisibility(View.VISIBLE);
    }
  }
}