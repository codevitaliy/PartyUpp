package com.codevitaliy.partyupp.game;

import static com.codevitaliy.partyupp.App.getContext;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codevitaliy.partyupp.MainActivity;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.api.ApiClient;
import com.codevitaliy.partyupp.api.Authentication;
import com.codevitaliy.partyupp.api.Challenge;
import com.codevitaliy.partyupp.home_fragments.Settings;
import com.codevitaliy.partyupp.player_selection.PlayerSelectionActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {

    //private final int GAME_MAX_DURATION = 20;

    ProgressBar progressBar;
    ChallengeFragment challengeFragment;
    View uiBackground;
    TextView tvTimerDescription;
    TextView tvTimerRemaining;

    ArrayList<String> playerNames;
    ArrayList<Challenge> challenges;

    int timerRemaining;
    int currentChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // To be fullscreen:
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        currentChallenge = 0;

        timerRemaining = 0;

        playerNames = (ArrayList<String>) getIntent().getSerializableExtra(PlayerSelectionActivity.INFO_PLAYERS_LIST);

        progressBar = findViewById(R.id.gameActivityProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        uiBackground = findViewById(R.id.gameUiBackground);
        tvTimerDescription = findViewById(R.id.gameUiTimerDescription);
        tvTimerRemaining = findViewById(R.id.gameUiTimerRemaining);

        uiBackground.setVisibility(View.INVISIBLE);
        // Handle back button press
        OnBackPressedDispatcher dispatcher = this.getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {
                Settings settings = Settings.getInstance();
                if(settings.volume) {
                    settings.playClickSound(getContext());
                }

                if(settings.vibration) {
                    settings.activateVibration(getContext());
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle(getString(R.string.exit_game_confirmation_ttl))
                        .setMessage(getString(R.string.exit_game_confirmation_msg))
                        .setNegativeButton(getString(R.string.dialog_exit_button), (dialog, which) -> {
                            setEnabled(false);
                            GameActivity.super.onBackPressed();
                        })
                        .setPositiveButton(getString(R.string.dialog_cancel_button), null)
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
                    positiveButton.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.blue));
                }

                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (negativeButton != null) {
                    negativeButton.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.red));
                }

                negativeButton.setOnClickListener(v -> {
                    Intent i;
                    if(currentChallenge > 10 && Authentication.getInstance().isLoggedIn()){
                        i = new Intent(GameActivity.this, EndPhotoActivity.class);
                    } else{
                        i = new Intent(GameActivity.this, MainActivity.class);
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();
                });
            }
        });


        ApiClient.getInstance().getChallenges(new Callback<ArrayList<Challenge>>() {
            @Override
            public void onResponse(Call<ArrayList<Challenge>> call, Response<ArrayList<Challenge>> response) {

                challenges = response.body();
                Collections.shuffle(challenges);
                progressBar.setVisibility(View.INVISIBLE);
                loadChallenge(challenges.get(currentChallenge));
            }

            @Override
            public void onFailure(Call<ArrayList<Challenge>> call, Throwable t) {

            }
        });
    }


    void loadChallenge(Challenge challenge){

        Bundle args = new Bundle();
        args.putSerializable(ChallengeFragment.ARG_CHALLENGE, challenge);
        args.putSerializable(ChallengeFragment.ARG_PLAYER_LIST, playerNames);

        ChallengeFragment fragment;

        if(challenge.category == Challenge.Category.Simple){
            fragment = new SimpleChallengeFragment();
            fragment.addListener(new ChallengeFragment.ChallengeFragmentListener() {
                @Override
                public void nextChallenge(Integer duration, SpannableString timerDescription) {

                    if ((currentChallenge + 1) < challenges.size()) {
                        loadNext();
                    } else {
                        Intent intent = new Intent(GameActivity.this, GameEndedActivity.class);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(GameActivity.this);
                        stackBuilder.addNextIntentWithParentStack(intent);
                        stackBuilder.startActivities();
                        finish();
                    }

                }
            });
        } else if(challenge.category == Challenge.Category.Timer) {
            fragment = new TimerChallengeFragment();
            fragment.addListener(new ChallengeFragment.ChallengeFragmentListener() {
                @Override
                public void nextChallenge(Integer duration, SpannableString timerDescription) {
                    if ((currentChallenge + 1) < challenges.size()) {
                        //TODO: Refactor (I add 1 because loadNext subtracts 1)
                        initTimer(duration + 1, timerDescription);
                        loadNext();
                    } else {
                        Intent intent = new Intent(GameActivity.this, GameEndedActivity.class);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(GameActivity.this);
                        stackBuilder.addNextIntentWithParentStack(intent);
                        stackBuilder.startActivities();
                        finish();
                    }

                }
            });
        } else {
            // No more categories exist currently
            return;
        }

        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                //.add(R.id.fragmentChallenge, fragment, null)
                .replace(R.id.fragmentChallenge, fragment, null)
                .commit();
    }

    void loadNext() {
        currentChallenge++;
        Challenge newChallenge = challenges.get(currentChallenge);
        loadChallenge(newChallenge);
        if(timerRemaining > 0){
            setTimer(timerRemaining - 1);
        }
    }

    void setTimer(int duration) {
        timerRemaining = duration;
        if(duration == 0){
            tvTimerRemaining.setText("");
            tvTimerDescription.setText("");
            uiBackground.setVisibility(View.INVISIBLE);

        } else {
            tvTimerRemaining.setText(String.format(Locale.getDefault(), getResources().getString(R.string.timer_remaining),timerRemaining));
            uiBackground.setVisibility(View.VISIBLE);
        }
    }

    void initTimer(int duration, SpannableString description) {
        timerRemaining = duration;
        tvTimerRemaining.setText(String.format(Locale.getDefault(), getResources().getString(R.string.timer_remaining),timerRemaining));
        tvTimerDescription.setText(description);
    }
}