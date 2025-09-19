package com.codevitaliy.partyupp.player_selection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.home_fragments.Settings;

import java.util.ArrayList;

public class PlayerSelectionAdapter extends RecyclerView.Adapter<PlayerSelectionAdapter.ViewHolder> {

    private RecyclerView recyclerView;
    private ArrayList<PlayerSelectionItem> items;
    private OnRemovePlayerClickListener removePlayerClickListener;



    public PlayerSelectionAdapter(ArrayList<PlayerSelectionItem> items, Context context, RecyclerView recyclerView, OnRemovePlayerClickListener removePlayerClickListener) {
        this.items = items;
        this.context = context;
        this.recyclerView = recyclerView;
        this.removePlayerClickListener = removePlayerClickListener;
        Settings settings = Settings.getInstance();
        settings.playClickSound(context);
        settings.activateVibration(context);
    }

    Context context;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editTextPlayerName;
        ImageButton imageButtonRemovePlayer;




        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            editTextPlayerName = (EditText) view.findViewById(R.id.editTextPlayerItem);
            imageButtonRemovePlayer = (ImageButton) view.findViewById(R.id.imageButtonRemovePlayer);
        }

    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_player_selection, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.editTextPlayerName.setText(items.get(position).getName());

        // Declare a final variable to capture the position
        int currentPosition = viewHolder.getAdapterPosition();

        if (position >= items.size()) {
            return; // Prevent accessing an invalid index
        }

        // Get the item at the current position
        PlayerSelectionItem playerItem = items.get(position);

        // Set the name in the EditText
        viewHolder.editTextPlayerName.setText(playerItem.getName());

        // Set up listeners, etc.
        viewHolder.editTextPlayerName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus || !viewHolder.editTextPlayerName.hasFocus()) { // If it loses focus
                String newName = viewHolder.editTextPlayerName.getText().toString();
                items.get(position).setName(newName);
            }
        });





        viewHolder.editTextPlayerName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus(); // Clear focus to trigger the onFocusChange event
                    return true;
                }
                return false;
            }
        });





        viewHolder.imageButtonRemovePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocusFromAdapter();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Remove focus from the EditText
                viewHolder.editTextPlayerName.clearFocus();

                int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    removePlayerClickListener.onRemovePlayerClicked(adapterPosition);
                }

                // Make sure to clear focus after notifying adapter
                clearFocusFromAdapter();
            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    private void clearFocusFromAdapter() {
        for (int i = 0; i < getItemCount(); i++) {
            ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                viewHolder.editTextPlayerName.clearFocus();
            }
        }
    }

    public interface OnRemovePlayerClickListener {
        void onRemovePlayerClicked(int position);
    }
}


