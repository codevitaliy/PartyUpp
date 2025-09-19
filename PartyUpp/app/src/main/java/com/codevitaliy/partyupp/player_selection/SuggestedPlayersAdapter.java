package com.codevitaliy.partyupp.player_selection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.home_fragments.Settings;

import java.util.ArrayList;

public class SuggestedPlayersAdapter extends RecyclerView.Adapter<SuggestedPlayersAdapter.ViewHolder> {

    public static ArrayList<String> suggestedPlayers;
    private Context context;

    public void addPlayers(ArrayList<String> players) {
        suggestedPlayers.addAll(players);
        notifyDataSetChanged();
    }

    public SuggestedPlayersAdapter(ArrayList<String> suggestedPlayers, Context context) {
        this.context = context;
        this.suggestedPlayers = suggestedPlayers; // Generate random player names
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggested_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String player = suggestedPlayers.get(position);
        holder.buttonSuggestedPlayer.setText(player);
    }

    @Override
    public int getItemCount() {
        return suggestedPlayers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView buttonSuggestedPlayer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonSuggestedPlayer = itemView.findViewById(R.id.buttonSuggestedPlayer);

            buttonSuggestedPlayer.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position == -1) {
                    return;
                }
                Settings settings = Settings.getInstance();
                settings.playClickSound(v.getContext());
                settings.activateVibration(v.getContext());

                if (PlayerSelectionActivity.playersList.size() < PlayerSelectionActivity.MAX_NUMBER_OF_PLAYERS) {
                    PlayerSelectionActivity.playersList.add(new PlayerSelectionItem(buttonSuggestedPlayer.getText().toString())); // Corrected parentheses
                    PlayerSelectionActivity.playerSelectionAdapter.notifyItemInserted(PlayerSelectionActivity.playersList.size() - 1); // Notify adapter of the insertion
                    PlayerSelectionActivity.playersCounter++;
                    //buttonSuggestedPlayer.setEnabled(false);
                    suggestedPlayers.remove(position); // Remove the item from the dataset
                    PlayerSelectionActivity.suggestedPlayersAdapter.notifyItemRemoved(position); // Notify adapter of the removal
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(itemView.getContext().getString(R.string.too_many_players_ttl))
                            .setMessage(itemView.getContext().getString(R.string.too_many_players_msg) + PlayerSelectionActivity.MAX_NUMBER_OF_PLAYERS)
                            .setPositiveButton(itemView.getContext().getString(R.string.dialog_ok_button), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel(); //cerrar el diálogo al pulsar el botón
                                }
                            })
                            .show();
                }
            });
        }
    }

    // Method to generate random player names
    public static ArrayList<String> generateRandomPlayers() {
        ArrayList<String> players = new ArrayList<>();
        String[] names = {"Sofía", "David", "Paula", "Álvaro", "William"};
        for (int i = 0; i < 5; i++) {
            players.add(names[i]);
        }
        return players;
    }
}
