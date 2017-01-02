package com.senacor.devconfapp.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.activities.EventActivity;
import com.senacor.devconfapp.handlers.SpeechHandler;
import com.senacor.devconfapp.handlers.ValidationHandler;
import com.senacor.devconfapp.models.Speech;

import org.joda.time.LocalTime;


/**
 * Created by saba on 25.12.16.
 */

public class SpeechDialog extends DialogFragment {

    SpeechHandler speechHandler;
    ValidationHandler validationHandler;
    TextView headline, labelStartTime, labelEndTime, tvSpeechId, tvSpeechColliding, tvStartEndTime;
    EditText etSpeechTitle, etSpeaker, etRoom;
    TimePicker tpStartTime, tpEndTime;
    boolean editing = false;
    boolean needsValidation = false;

    public SpeechDialog() {

    }

    //needed for editing existing speech
    public static SpeechDialog newInstance(Speech speech, boolean isBeingEdited, boolean wasNotValidated, String validationError) {
        SpeechDialog speechDialog = new SpeechDialog();
        Bundle args = new Bundle();
        if (speech != null) {
            if (isBeingEdited) {
                args.putString("speechId", speech.getSpeechId());
            }
            if (wasNotValidated) {
                args.putString("validationError", validationError);
            }
            args.putString("speechTitle", speech.getSpeechTitle());
            args.putString("speaker", speech.getSpeaker());
            args.putString("room", speech.getSpeechRoom());
            args.putString("speechStartTime", speech.getStartTime().toString());
            args.putString("speechEndTime", speech.getEndTime().toString());
            args.putBoolean("isBeingEdited", isBeingEdited);
            args.putBoolean("wasNotValidated", wasNotValidated);
            speechDialog.setArguments(args);
        }

        return speechDialog;
    }


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        speechHandler = new SpeechHandler(getActivity());
        validationHandler = new ValidationHandler();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.speech_popup_layout, null);

        tvSpeechId = (TextView) view.findViewById(R.id.speechPopUp_speechId);
        headline = (TextView) view.findViewById(R.id.createSpeechHeadline);
        labelStartTime = (TextView) view.findViewById(R.id.label_startTime);
        labelEndTime = (TextView) view.findViewById(R.id.label_endTime);
        tvSpeechColliding = (TextView) view.findViewById(R.id.warning_speechValidation);
        tvStartEndTime = (TextView) view.findViewById(R.id.warning_startEndTime);


        etSpeechTitle = (EditText) view.findViewById(R.id.addSpeechTitle);
        etSpeaker = (EditText) view.findViewById(R.id.addSpeakerName);
        etRoom = (EditText) view.findViewById(R.id.addRoom);


        tpStartTime = (TimePicker) view.findViewById(R.id.time_picker_start);
        tpStartTime.setIs24HourView(true);
        tpEndTime = (TimePicker) view.findViewById(R.id.time_picker_end);
        tpEndTime.setIs24HourView(true);

        if (getArguments() != null) {
            editing = getArguments().getBoolean("isBeingEdited");
            needsValidation = getArguments().getBoolean("wasNotValidated");

        }

        if (editing || needsValidation) {
            etSpeechTitle.setText(getArguments().getString("speechTitle"));
            etSpeaker.setText(getArguments().getString("speaker"));
            etRoom.setText(getArguments().getString("room"));

            String startTime = getArguments().getString("speechStartTime");
            String[] partsStart = startTime.split(":");
            tpStartTime.setHour(Integer.parseInt(partsStart[0]));
            tpStartTime.setMinute(Integer.parseInt(partsStart[1]));

            String endTime = getArguments().getString("speechEndTime");
            String[] partsEnd = endTime.split(":");
            tpEndTime.setHour(Integer.parseInt(partsEnd[0]));
            tpEndTime.setMinute(Integer.parseInt(partsEnd[1]));

            if (editing) {
                headline.setText("Edit Speech");
                tvSpeechId.setText(getArguments().getString("speechId"));
            }

            // if this dialogue opens because of wrong validation the labels for times are highlighted in red
            if (needsValidation) {
                if (getArguments().getString("validationError").equals("colliding")) {
                    tvSpeechColliding.setVisibility(View.VISIBLE);
                }else{
                    tvStartEndTime.setVisibility(View.VISIBLE);
                }
            }

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


                        int startHour = tpStartTime.getHour();
                        int startMin = tpStartTime.getMinute();
                        final LocalTime startTime = new LocalTime(startHour, startMin);

                        int endHour = tpEndTime.getHour();
                        int endMin = tpEndTime.getMinute();
                        final LocalTime endTime = new LocalTime(endHour, endMin);

                        //check if times were entered in correct order (starttime < endtime)
                        if (validationHandler.isInRightOrder(startTime, endTime)) {
                            System.out.println("times are in right order");
                            RequestParams params = new RequestParams();
                            params.put("speechTitle", speechTitle);
                            params.put("speechRoom", room);
                            params.put("speaker", speechSpeaker);
                            params.put("startTime", startTime);
                            params.put("endTime", endTime);

                            if (editing) {
                                String speechId = tvSpeechId.getText().toString();
                                params.put("speechId", speechId);
                                String url = EventActivity.URL + "/speeches/" + speechId;
                                System.out.println(url);

                                speechHandler.editSpeech(url, params);
                            } else {
                                speechHandler.addSpeech(params);
                            }
                        }else{
                            //if endtime < starttime the dialogue will be opened again. Data is prefilled
                            System.out.println("endtime before starttime");
                            Speech speech = new Speech();
                            if (editing) {
                                speech.setSpeechId(tvSpeechId.getText().toString());
                            }
                            speech.setSpeechTitle(speechTitle);
                            speech.setSpeaker(speechSpeaker);
                            speech.setSpeechRoom(room);
                            speech.setStartTime(startTime);
                            speech.setEndTime(endTime);
                            DialogFragment speechFragment = SpeechDialog.newInstance(speech, editing, true, "startEndTime");
                            Activity activity = (Activity) getContext();
                            speechFragment.show(activity.getFragmentManager(), "SpeechDialog");
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
