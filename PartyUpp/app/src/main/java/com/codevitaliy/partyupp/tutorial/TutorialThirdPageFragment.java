package com.codevitaliy.partyupp.tutorial;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codevitaliy.partyupp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TutorialThirdPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialThirdPageFragment extends Fragment {

    Button buttonNextTutorialPage3;
    ImageView imageViewChallengePhoto;
    TextView textViewTutorialPage3Title, textViewTutorialPage3Content;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TutorialThirdPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TutorialThirdPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorialThirdPageFragment newInstance(String param1, String param2) {
        TutorialThirdPageFragment fragment = new TutorialThirdPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_tutorial_third_page, container, false);
        buttonNextTutorialPage3 = layoutView.findViewById(R.id.buttonNextTutorialPage3);
//        imageViewPlayerSelectionMockup = layoutView.findViewById(R.id.imageViewPlayerSelectionMockup);

        textViewTutorialPage3Title = layoutView.findViewById(R.id.textViewTutorialPage3Title);
        textViewTutorialPage3Content = layoutView.findViewById(R.id.textViewTutorialPage3Content);


        // Set the button click listener to finish the TutorialActivity
        buttonNextTutorialPage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        return layoutView;
    }
}