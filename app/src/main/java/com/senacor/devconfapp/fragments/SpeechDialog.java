package com.senacor.devconfapp.fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.SpeechHandler;


/**
 * Created by saba on 25.12.16.
 */

public class SpeechDialog extends DialogFragment {


    public SpeechDialog() {

    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {



        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //speechTitle.setOnEditorActionListener(this);
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();


                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.speech_popup_layout, null))
                        // Add action buttons
                        .setPositiveButton(R.string.saveSpeech, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                final EditText etSpeechTitle = (EditText) getDialog().findViewById(R.id.addSpeechTitle);
                                final EditText etSpeakerName= (EditText) getDialog().findViewById(R.id.addSpeakerName);
                                final EditText etRoom = (EditText) getDialog().findViewById(R.id.addRoom);
                                //final TimePicker tpStartTime = (TimePicker) getDialog().findViewById(R.id.startTime);


                                final String speechTitle = etSpeechTitle.getText().toString();
                                final String speechSpeaker = etSpeakerName.getText().toString();
                                final String room = etRoom.getText().toString();
                               // final String startTime = tpStartTime.getHour() + ":" + tpStartTime.getMinute();



                                RequestParams params = new RequestParams();
                                params.put("speechTitle", speechTitle);
                                params.put("speechRoom", room);
                                params.put("speaker", speechSpeaker);
                                params.setUseJsonStreamer(true);

                                SpeechHandler speechHandler = new SpeechHandler(getActivity());
                                speechHandler.addSpeech(params);
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
