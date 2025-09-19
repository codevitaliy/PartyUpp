package com.codevitaliy.partyupp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.codevitaliy.partyupp.home_fragments.Settings;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private View decorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to be fullscreen:
        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationViewMain);
        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMain))
                .getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        ColorStateList colorStateList = new ColorStateList(
                new int[][] {
                        new int[] { android.R.attr.state_checked }, // checked state
                        new int[] { -android.R.attr.state_checked } // unchecked state
                },
                new int[] {
                        ContextCompat.getColor(this, R.color.blue),
                        Color.WHITE
                }
        );

        bottomNavigationView.setItemIconTintList(colorStateList);

        Settings.getInstance().playMusic(MainActivity.this);
    }
}