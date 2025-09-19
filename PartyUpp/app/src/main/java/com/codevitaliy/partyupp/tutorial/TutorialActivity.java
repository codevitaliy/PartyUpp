package com.codevitaliy.partyupp.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.databinding.FragmentTutorialFirstPageBinding;

public class TutorialActivity extends AppCompatActivity {

    TutorialFirstPageFragment fragmentTutorialPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        fragmentTutorialPage = new TutorialFirstPageFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentTutorialPage, fragmentTutorialPage, null)
//                .replace(R.id.fragmentTutorialPage, fragmentTutorialPage, null)
                .commit();
    }


}