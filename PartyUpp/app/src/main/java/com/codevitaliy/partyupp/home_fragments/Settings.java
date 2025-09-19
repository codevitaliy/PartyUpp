package com.codevitaliy.partyupp.home_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.codevitaliy.partyupp.App;
import com.codevitaliy.partyupp.R;

public class Settings {
    private static Settings settingsInstance;
    public boolean volume;
    public boolean vibration;
    public boolean music;

    private MediaPlayer mediaPlayer;
    private MediaPlayer musicPlayer;



    private Settings(){
        SharedPreferences preferences  = App.getContext()
                .getSharedPreferences("settings", Context.MODE_PRIVATE);
        this.volume = preferences.getBoolean("volume", false);
        this.vibration = preferences.getBoolean("vibration", false);
        this.music = preferences.getBoolean("music", false);
        //...
    }

    public static Settings getInstance(){
        if(settingsInstance == null){
            settingsInstance = new Settings();
        }
        return settingsInstance;
    }

    public void playClickSound(Context context) {
        if(volume) {

            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(context, R.raw.click_sound);
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.release();
                mediaPlayer = null;
            });
            mediaPlayer.start();
        }
    }

    public void activateVibration(Context context) {
        if (vibration) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
    }

    public void playMusic(Context context) {
        if (music) {
            // Initializes music player if it's null or releases it if it's already initialized
            if (musicPlayer == null) {
                musicPlayer = MediaPlayer.create(context, R.raw.background_music);
                musicPlayer.setLooping(true);
                musicPlayer.start();
            } else if (!musicPlayer.isPlaying()) {
                musicPlayer.start();
            }
        } else {
            if (musicPlayer != null && musicPlayer.isPlaying()) {
                musicPlayer.pause(); // Pauses the music instead of stopping and releasing
            }
        }
    }

    // Revised toggleMusic method
    public void toggleMusic(Context context) {
        setMusic(!music); // Toggles the music state
        playMusic(context); // Calls playMusic to handle the current state of music
    }

    public void setVolume(boolean volume) {
        SharedPreferences preferences  = App.getContext()
                .getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("volume", volume);
        editor.apply();
        this.volume = volume;
    }

    public void setMusic(boolean music) {
        SharedPreferences preferences  = App.getContext()
                .getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("music", music);
        editor.apply();
        this.music = music;
    }
    public void setVibration(boolean vibration) {
        SharedPreferences preferences  = App.getContext()
                .getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("vibration", vibration);
        editor.apply();
        this.vibration = vibration;
    }
}


