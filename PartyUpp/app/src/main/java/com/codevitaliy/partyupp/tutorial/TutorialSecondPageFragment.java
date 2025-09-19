package com.codevitaliy.partyupp.tutorial;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codevitaliy.partyupp.R;

public class TutorialSecondPageFragment extends Fragment {

    Button buttonNextTutorialPage2;
    ImageView imageViewInitialPhoto;
    TextView textViewTutorialPage2Title, textViewTutorialPage2Content;

    public TutorialSecondPageFragment() {
        // Required empty public constructor
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_tutorial_second_page, container, false);
        buttonNextTutorialPage2 = layoutView.findViewById(R.id.buttonNextTutorialPage2);
//        imageViewPlayerSelectionMockup = layoutView.findViewById(R.id.imageViewPlayerSelectionMockup);

        textViewTutorialPage2Title = layoutView.findViewById(R.id.textViewTutorialPage2Title);
        textViewTutorialPage2Content = layoutView.findViewById(R.id.textViewTutorialPage2Content);

        buttonNextTutorialPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the next fragment
                TutorialThirdPageFragment nextFragment = new TutorialThirdPageFragment();

                // Get the FragmentManager and start a transaction
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Replace the current fragment with the next fragment
                fragmentTransaction.replace(R.id.fragmentTutorialPage, nextFragment);
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });

        return layoutView;
    }
}