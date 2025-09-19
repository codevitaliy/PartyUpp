package com.codevitaliy.partyupp.game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.home_fragments.Settings;
import com.codevitaliy.partyupp.api.Challenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SimpleChallengeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimpleChallengeFragment extends ChallengeFragment {


    private static final String LANG_CODE_ES = "es";
    Button btNext;
    TextView tvChallengeText;

    private static final String ARG_CHALLENGE = "CHALLENGE";

    private static final String ARG_PLAYER_LIST = "PLAYER_LIST";

    // TODO: Rename and change types of parameters
    private Challenge challenge;
    private ArrayList<String> playerList;

    public SimpleChallengeFragment() {
    }

    public static SimpleChallengeFragment newInstance(Challenge challenge, ArrayList<String> playerList) {
        SimpleChallengeFragment fragment = new SimpleChallengeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHALLENGE, challenge);
        args.putSerializable(ARG_PLAYER_LIST, playerList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            challenge = (Challenge) getArguments().getSerializable(ARG_CHALLENGE);
            playerList = (ArrayList<String>)((ArrayList<String>)getArguments().getSerializable(ARG_PLAYER_LIST)).clone();
            Collections.shuffle(playerList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_simple_challenge, container, false);

        tvChallengeText = layoutView.findViewById(R.id.textViewSimpleChallenge);
        btNext = layoutView.findViewById(R.id.buttonNextSimpleChallenge);

        return layoutView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SpannableString parsedText;

        if(Locale.getDefault().getLanguage().equals(LANG_CODE_ES)){
            parsedText = parseChallengeText(challenge.data.get("text_es"), playerList);
        } else {
            parsedText = parseChallengeText(challenge.data.get("text_en"), playerList);
        }


        tvChallengeText.setText(parsedText);

        btNext.setOnClickListener(v -> {
            Settings settings = Settings.getInstance();
            settings.playClickSound(v.getContext());
            settings.activateVibration(v.getContext());
            for(ChallengeFragmentListener listener : listeners) {
                listener.nextChallenge(null, null);
            }
        });
    }


}