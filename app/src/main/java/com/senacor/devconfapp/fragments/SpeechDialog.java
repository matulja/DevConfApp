package com.senacor.devconfapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.senacor.devconfapp.R;




/**
 * Created by saba on 25.12.16.
 */

public class SpeechDialog extends DialogFragment {

    public SpeechDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.speech_popup_layout, null))
                // Add action buttons
                .setPositiveButton(R.string.saveSpeech, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.cancelSpeech, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SpeechDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }



}
