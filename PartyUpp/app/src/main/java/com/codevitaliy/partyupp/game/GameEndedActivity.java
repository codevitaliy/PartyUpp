package com.codevitaliy.partyupp.game;

import static com.codevitaliy.partyupp.App.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.codevitaliy.partyupp.MainActivity;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.api.Authentication;
import com.codevitaliy.partyupp.home_fragments.Settings;

public class GameEndedActivity extends AppCompatActivity {
    Button buttonEndGameContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_ended);

        buttonEndGameContinue = findViewById(R.id.buttonEndGameContinue);

        buttonEndGameContinue.setOnClickListener(v -> {

            Settings settings = Settings.getInstance();
            if(settings.volume) {
                settings.playClickSound(getContext());
            }

            if(settings.vibration) {
                settings.activateVibration(getContext());
            }

            if(Authentication.getInstance().isLoggedIn()){
                Intent i = new Intent(GameEndedActivity.this, EndPhotoActivity.class);
                startActivity(i);
            } else {
                Intent intent = new Intent(GameEndedActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack and start MainActivity as a new task
                startActivity(intent);
                finish();
            }
        });
    }
}