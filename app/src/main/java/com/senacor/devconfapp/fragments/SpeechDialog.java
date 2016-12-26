package com.senacor.devconfapp.fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.SpeechHandler;
import com.senacor.devconfapp.models.Speech;


/**
 * Created by saba on 25.12.16.
 */

public class SpeechDialog extends DialogFragment{

    SpeechHandler speechHandler;
    TextView headline;
    EditText etSpeechTitle, etSpeaker, etRoom;
    TextView tvSpeechId;
    //Timepicker tpStartTime, tpEndTime;
    String speechUrl;
    static boolean editingExistingSpeech = false;

    public SpeechDialog() {

    }

    public static SpeechDialog newInstance(Speech speech) {
        SpeechDialog speechDialog = new SpeechDialog();
        Bundle args = new Bundle();
        if (speech != null) {
            args.putString("speechId", speech.getSpeechId());
            args.putString("speechTitle", speech.getSpeechTitle());
            args.putString("speaker", speech.getSpeaker());
            args.putString("room", speech.getSpeechRoom());
            //TODO start and end time
/*          args.putString("speechStartTime", speech.getStartTime());
            args.putString("speechEndTime", speech.getEndTime());*/
            args.putString("speechUrl", speech.getUrl());
            speechDialog.setArguments(args);
            editingExistingSpeech = true;
        }

        return speechDialog;
    }


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        speechHandler = new SpeechHandler(getActivity());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.speech_popup_layout, null);
        tvSpeechId = (TextView) view.findViewById(R.id.speechPopUp_speechId);
        etSpeechTitle = (EditText) view.findViewById(R.id.addSpeechTitle);
        etSpeaker = (EditText) view.findViewById(R.id.addSpeakerName);
        etRoom = (EditText) view.findViewById(R.id.addRoom);

        //tpStartTime = (TimePicker) view.findViewById(R.id.startTime);


        if (editingExistingSpeech) {
            tvSpeechId.setText(getArguments().getString("speechId"));
            etSpeechTitle.setText(getArguments().getString("speechTitle"));
            etSpeaker.setText(getArguments().getString("speaker"));
            etRoom.setText(getArguments().getString("room"));

        }
        // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(view)

                        // Add action buttons
                        .setPositiveButton(R.string.saveSpeech, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {



                                final String speechTitle = etSpeechTitle.getText().toString();
                                final String speechSpeaker = etSpeaker.getText().toString();
                                final String room = etRoom.getText().toString();
                               // final String startTime = tpStartTime.getHour() + ":" + tpStartTime.getMinute();

                                RequestParams params = new RequestParams();
                                params.put("speechTitle", speechTitle);
                                params.put("speechRoom", room);
                                params.put("speaker", speechSpeaker);
                                //TODO put start and end time

                                if (editingExistingSpeech) {
                                    params.put("speechId", tvSpeechId.getText().toString());
                                    speechHandler.editSpeech(getArguments().getString("speechUrl"), params);
                                }else{
                                    speechHandler.addSpeech(params);
                                }

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
