package com.example.myapplication.Buyer.Interest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;



public class InterestDialog extends DialogFragment {

    private long selectedDiningHall = 0;
    // Temp list for now. We will get this from the offer information
    private String[] diningHalls = new String[]{"B-Plate", "De Neve", "Covel", "Feast"};

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set messages and title
        builder.setTitle("Show Interest")
                .setSingleChoiceItems(diningHalls, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedDiningHall = which;
                        Log.d("Selected choice", Integer.toString(which));
                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Send the information to the other user
                        Log.d("Selected Choice", "Will send");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel the request
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

}
