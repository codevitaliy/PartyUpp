package com.codevitaliy.partyupp.tutorial;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import com.codevitaliy.partyupp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TutorialFirstPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialFirstPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    Button buttonNextTutorialPage;
    ImageView imageViewPlayerSelectionMockup;
    TextView textViewTutorialPage1Title, textViewTutorialPage1Content;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TutorialFirstPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TutorialFirstPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorialFirstPageFragment newInstance(String param1, String param2) {
        TutorialFirstPageFragment fragment = new TutorialFirstPageFragment();
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
        View layoutView = inflater.inflate(R.layout.fragment_tutorial_first_page, container, false);

        buttonNextTutorialPage = layoutView.findViewById(R.id.buttonNextTutorialPage);
        imageViewPlayerSelectionMockup = layoutView.findViewById(R.id.imageViewPlayerSelectionMockup);

        textViewTutorialPage1Title = layoutView.findViewById(R.id.textViewTutorialPage1Title);
        textViewTutorialPage1Content = layoutView.findViewById(R.id.textViewTutorialPage1Content);

        buttonNextTutorialPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the next fragment
                TutorialSecondPageFragment nextFragment = new TutorialSecondPageFragment();

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