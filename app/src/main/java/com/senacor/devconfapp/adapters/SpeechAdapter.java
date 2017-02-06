package com.senacor.devconfapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.senacor.devconfapp.R;
import com.senacor.devconfapp.fragments.SpeechDialog;
import com.senacor.devconfapp.handlers.SpeechHandler;
import com.senacor.devconfapp.handlers.SpeechRatingHandler;
import com.senacor.devconfapp.models.Speech;

import java.util.ArrayList;

/**
 * Created by Veronika Babic on 14.11.2016.
 */

public class SpeechAdapter extends ArrayAdapter<Speech> {

    SpeechHandler speechHandler;
    SpeechRatingHandler speechRatingHandler;
    private AppCompatActivity activity;
    boolean isInFuture, isToday;
    String role, userId;

    public static class ViewHolder{
        TextView speechId;
        TextView speechTitle;
        TextView startTime;
        TextView endTime;
        TextView speechRoom;
        TextView speaker;
        TextView speakerInfo;
        TextView speechSummary;
        ImageView deleteButton;
        ImageView editSpeechButton;
        RatingBar ratingBar;
        TextView rateNow;
        Button submitButton;


        public ViewHolder(View view){
            speechId = (TextView) view.findViewById(R.id.value_speech_speechId);
            speechTitle= (TextView) view.findViewById(R.id.speechTitle);
            speechRoom = (TextView) view.findViewById(R.id.speechLocation);
            speaker = (TextView) view.findViewById(R.id.speakerName);
            speakerInfo = (TextView) view.findViewById(R.id.speakerInfo);
            speechSummary = (TextView) view.findViewById(R.id.speechSummary);
            startTime = (TextView) view.findViewById(R.id.value_startTime);
            endTime = (TextView) view.findViewById(R.id.value_endTime);
            deleteButton = (ImageView) view.findViewById(R.id.button_delete);
            editSpeechButton = (ImageView) view.findViewById(R.id.button_editSpeech);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingSpeechBar);
            submitButton = (Button) view.findViewById(R.id.submitRating_Button);
            rateNow = (TextView) view.findViewById(R.id.stringRate);
        }

        public Button getSubmitButton() {
            return submitButton;
        }

        public void setSubmitButton(Button submitButton) {
            this.submitButton = submitButton;
        }

        public TextView getRateNow() {
            return rateNow;
        }

        public void setRateNow(TextView rateNow) {
            this.rateNow = rateNow;
        }

        public RatingBar getRatingBar() {
            return ratingBar;
        }

        public void setRatingBar(RatingBar ratingBar) {
            this.ratingBar = ratingBar;
        }
    }

    public SpeechAdapter(AppCompatActivity context, ArrayList<Speech> speeches) {
        super(context, R.layout.item_speech, speeches);
        this.activity=context;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        role = sharedPref.getString("role", "role");
        userId = sharedPref.getString("userId", "userId");
        isInFuture = sharedPref.getBoolean("isInFuture", true);
        isToday = sharedPref.getBoolean("isToday", true);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Speech speech = getItem(position);

        final ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_speech, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SpeechAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.speechTitle.setText(speech.getSpeechTitle());
        viewHolder.speechRoom.setText(speech.getSpeechRoom());
        viewHolder.speaker.setText(speech.getSpeaker());
        viewHolder.speakerInfo.setText(speech.getSpeakerInfo());
        viewHolder.speechSummary.setText(speech.getSpeechSummary());
        viewHolder.startTime.setText(speech.timeToString(speech.getStartTime()));
        viewHolder.endTime.setText(speech.timeToString(speech.getEndTime()));
        speechHandler = new SpeechHandler((Activity)getContext());
        speechRatingHandler = new SpeechRatingHandler((Activity)getContext());
        if (!isInFuture) {
            speechRatingHandler.getSpeechRating(userId, speech, viewHolder);
        }
        else{
            viewHolder.ratingBar.setVisibility(View.GONE);
            viewHolder.submitButton.setVisibility(View.GONE);
            viewHolder.rateNow.setVisibility(View.GONE);
        }

        if (role.equals("ADMIN") && (isInFuture || isToday)) {
            viewHolder.deleteButton.setVisibility(View.VISIBLE);
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    //Alert Dialog
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    System.out.println("Delete Speech " + speech.getUrl());
                                    speechHandler.deleteSpeech(speech.getUrl());
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    System.out.println("Speech is not deleted" );
                                    dialog.cancel();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Do you want delete this speech?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
                            .show();
                }
            });

            viewHolder.editSpeechButton.setVisibility(View.VISIBLE);
            viewHolder.editSpeechButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    System.out.println("clicklistener edit speech button: speech id = " + speech.getSpeechId());
                    DialogFragment speechFragment = SpeechDialog.newInstance(speech, true, false);
                    Activity activity = (Activity) getContext();
                    speechFragment.show(activity.getFragmentManager(), "SpeechDialog");
                }
            });

        }
        return convertView;
    }




}

