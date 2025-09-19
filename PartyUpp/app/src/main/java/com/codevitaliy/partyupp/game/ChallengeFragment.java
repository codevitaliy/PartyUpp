package com.codevitaliy.partyupp.game;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ChallengeFragment extends Fragment {
    public static final String ARG_CHALLENGE = "CHALLENGE";
    public static final String ARG_PLAYER_LIST = "PLAYER_LIST";

    public interface ChallengeFragmentListener {
        // null arguments if not a timer challenge
        void nextChallenge(Integer timerDuration, SpannableString timerDescription);
    }
    ArrayList<ChallengeFragmentListener> listeners;

    public ChallengeFragment() {
        listeners = new ArrayList<>();
    }

    public void addListener(ChallengeFragmentListener listener){
        listeners.add(listener);
    }

    public SpannableString parseChallengeText(String challengeText, ArrayList<String> playerList) {
        SpannableStringBuilder tempString = new SpannableStringBuilder("");
        String originalText = challengeText;
        SpannableString currentText = new SpannableString(challengeText);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(challengeText);

        Pattern pattern = Pattern.compile("\\{player(\\d+)\\}");

        int lastMatchEnd = 0;
        Matcher matcher = pattern.matcher(currentText);
        while(matcher.find()){
                int start = matcher.start();
                tempString.append(originalText.substring(lastMatchEnd,start));

                int numPlayer = Integer.parseInt(matcher.group(1));
                int spanStart = tempString.length();
                tempString.append(playerList.get(numPlayer-1));
                tempString.setSpan(new StyleSpan(Typeface.BOLD), spanStart, tempString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                lastMatchEnd = matcher.end();
        }
        tempString.append(originalText.substring(lastMatchEnd));

        return new SpannableString(tempString);

    }
}
