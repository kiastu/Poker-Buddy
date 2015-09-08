package com.kiastu.pokerbuddy.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.kiastu.pokerbuddy.R;

import java.util.ArrayList;

//TODO: Add multi selection buttons to select winners.
public class ChooseWinnerDialogFragment extends DialogFragment {
    private ArrayList<String> winnerNames;
    private ArrayList<String> potentialWinners;
    public static ChooseWinnerDialogFragment newInstance(ArrayList<String> potentialWinners) {
        ChooseWinnerDialogFragment f = new ChooseWinnerDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("potentialWinners", potentialWinners);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        winnerNames = new ArrayList<>();
        potentialWinners = getArguments().getStringArrayList("potentialWinners");
        String[] potentialWinnersArray = new String[potentialWinners.size()];
        potentialWinners.toArray(potentialWinnersArray);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_choose_winner)
                .setMultiChoiceItems(potentialWinnersArray, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int index,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    winnerNames.add(potentialWinners.get(index));
                                } else if (winnerNames.contains(index)) {
                                    // Else, if the item is already in the array, remove it
                                    winnerNames.remove(Integer.valueOf(index));
                                }
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
