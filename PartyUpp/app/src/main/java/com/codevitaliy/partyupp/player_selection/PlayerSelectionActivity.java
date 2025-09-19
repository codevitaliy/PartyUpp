package com.codevitaliy.partyupp.player_selection;

import static com.codevitaliy.partyupp.App.getContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.codevitaliy.partyupp.api.ApiClient;
import com.codevitaliy.partyupp.api.Authentication;
import com.codevitaliy.partyupp.game.GameActivity;
import com.codevitaliy.partyupp.game.InitialPhotoActivity;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.home_fragments.Settings;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerSelectionActivity extends AppCompatActivity implements PlayerSelectionAdapter.OnRemovePlayerClickListener {
    public static final String INFO_PLAYERS_LIST = "PlayerSelectionActivity.playersList";
    public RecyclerView recyclerViewPlayerSelection;
    RecyclerView recyclerViewSuggestedPlayers;
    FloatingActionButton buttonAddPlayers;
    Button buttonStartGame;
    static Integer playersCounter = 1;
    protected static final int MIN_NUMBER_OF_PLAYERS = 3;
    protected static final int MAX_NUMBER_OF_PLAYERS = 8;
    protected static final int MIN_NAME_LEN = 2;
    public static PlayerSelectionAdapter playerSelectionAdapter;
    public static SuggestedPlayersAdapter suggestedPlayersAdapter;
    public static ArrayList<PlayerSelectionItem> playersList;
    public static ArrayList<String> suggestedPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_player_selection);

        recyclerViewPlayerSelection = findViewById(R.id.recyclerViewPlayerSelection);
        recyclerViewSuggestedPlayers = findViewById(R.id.recyclerViewSuggestedPlayers);

        recyclerViewPlayerSelection.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideKeyboard(v);
                    clearFocusFromActivity(findViewById(android.R.id.content));
                }
                return false;
            }
        });

        playersList = new ArrayList<>();

        String defaultPlayerName = Authentication.getInstance()
                .getUserName();

        if(defaultPlayerName == null) {
            defaultPlayerName = getResources().getString(R.string.guest);
        } else {
            defaultPlayerName = defaultPlayerName.substring(0, 1).toUpperCase() + defaultPlayerName.substring(1);
        }

        playersList.add(new PlayerSelectionItem(defaultPlayerName));
        suggestedPlayers = SuggestedPlayersAdapter.generateRandomPlayers();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerViewPlayerSelection.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManagerSuggested = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSuggestedPlayers.setLayoutManager(layoutManagerSuggested);

        playerSelectionAdapter = new PlayerSelectionAdapter(playersList, getApplicationContext(), recyclerViewPlayerSelection, this);
        recyclerViewPlayerSelection.setAdapter(playerSelectionAdapter);

        suggestedPlayersAdapter = new SuggestedPlayersAdapter(new ArrayList<>(),getApplicationContext());
        recyclerViewSuggestedPlayers.setAdapter(suggestedPlayersAdapter);

        ApiClient.getInstance().getSuggestedPlayeres(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.isSuccessful()){
                    suggestedPlayersAdapter.addPlayers(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });


        buttonAddPlayers = findViewById(R.id.floatingActionButtonAddPlayer);

        buttonAddPlayers.setOnClickListener(v -> {
            Settings settings = Settings.getInstance();
            if(settings.volume) {
                settings.playClickSound(getContext());
            }

            if(settings.vibration) {
                settings.activateVibration(getContext());
            }
            if (playersList.size() < MAX_NUMBER_OF_PLAYERS) {
                playersList.add(new PlayerSelectionItem(""));
                playerSelectionAdapter.notifyItemInserted(playersList.size() - 1);
                playersCounter++;
                recyclerViewPlayerSelection.scrollToPosition(playersList.size() - 1);

                recyclerViewPlayerSelection.post(() -> {
                    RecyclerView.ViewHolder viewHolder = recyclerViewPlayerSelection.findViewHolderForAdapterPosition(playersList.size() - 1);

                    if (viewHolder instanceof PlayerSelectionAdapter.ViewHolder) {
                        EditText editTextPlayerName = ((PlayerSelectionAdapter.ViewHolder) viewHolder).editTextPlayerName;
                        editTextPlayerName.requestFocus();
                        editTextPlayerName.setText("");

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editTextPlayerName, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            } else {
                showDialog(getString(R.string.max_players_ttl), getString(R.string.max_players_msg) + MAX_NUMBER_OF_PLAYERS);
            }
        });


        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == PlayerSelectionActivity.RESULT_OK){
                    Intent data = result.getData();

                } else if (result.getResultCode() == PlayerSelectionActivity.RESULT_CANCELED){

                } else {

                }

            }
        });

        buttonStartGame = findViewById(R.id.buttonStartGame);
        buttonStartGame.setOnClickListener((v) -> {
            Settings settings = Settings.getInstance();
            settings.playClickSound(getApplicationContext());
            settings.activateVibration(getApplicationContext());

            if(playersList.size() < MIN_NUMBER_OF_PLAYERS){
                showDialog(getString(R.string.insufficient_players_ttl),
                        (getString(R.string.insufficient_players_msg) + MIN_NUMBER_OF_PLAYERS));
                return;

            }

            for (int i = 0 ; i < playersList.size() ; i++) {
                for (int j = i + 1; j < playersList.size() ; j++) {
                    if (playersList.get(i).getName().trim().toLowerCase().equals(playersList.get(j).getName().trim().toLowerCase())){
                        showDialog(getString(R.string.names_cant_repeat_ttl)
                                , getString(R.string.names_cant_repeat_msg));
                        return;
                    }
                }
            }
            for (PlayerSelectionItem player : playersList) {
                String playerName = player.getName().trim();
                if (playerName.isEmpty()) {
                    showDialog(getString(R.string.player_name_empty), getString(R.string.player_name_empty_msg));
                    return;
                }
            }

            for (PlayerSelectionItem player : playersList) {
                String playerName = player.getName().replaceAll("[\\s\\p{Punct}]", ""); // Removing whitespace and special characters
                if (playerName.length() < MIN_NAME_LEN) {
                    showDialog(getString(R.string.player_name_too_short), getString(R.string.player_name_too_short_msg) + MIN_NAME_LEN);
                    return;
                }
            }

            ArrayList<String> playersNameList = new ArrayList<>();
            for(PlayerSelectionItem playerItem : playersList){
                playerItem.setName(Character.toUpperCase(playerItem.getName().charAt(0)) + playerItem.getName().substring(1));

                playersNameList.add(playerItem.getName());
            }

            if(Authentication.getInstance().isLoggedIn()){
                Intent intent = new Intent(PlayerSelectionActivity.this, InitialPhotoActivity.class);

                intent.putStringArrayListExtra(INFO_PLAYERS_LIST, playersNameList);
                launcher.launch(intent);
            } else {
                Intent i = new Intent(PlayerSelectionActivity.this, GameActivity.class);
                i.putExtra(PlayerSelectionActivity.INFO_PLAYERS_LIST, playersNameList);
                startActivity(i);
            }

        });
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerSelectionActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_ok_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void clearFocusFromActivity(View view) {
        if (view instanceof EditText) {
            view.clearFocus();
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                clearFocusFromActivity(viewGroup.getChildAt(i));
            }
        }
    }

    @Override
    public void onRemovePlayerClicked(int position) {
        playersList.remove(position);
        playerSelectionAdapter.notifyItemRemoved(position);
    }

}
