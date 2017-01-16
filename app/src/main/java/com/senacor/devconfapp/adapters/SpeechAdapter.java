package com.senacor.devconfapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.senacor.devconfapp.R;
import com.senacor.devconfapp.fragments.SpeechDialog;
import com.senacor.devconfapp.handlers.SpeechHandler;
import com.senacor.devconfapp.models.Speech;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Veronika Babic on 14.11.2016.
 */

public class SpeechAdapter extends ArrayAdapter<Speech> {

    SpeechHandler speechHandler;
    private AppCompatActivity activity;
    private List<Speech>speeches;


    private static class ViewHolder {
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

        public ViewHolder(View view) {
            speechId = (TextView) view.findViewById(R.id.value_speech_speechId);
            speechTitle = (TextView) view.findViewById(R.id.speechTitle);
            speechRoom = (TextView) view.findViewById(R.id.speechLocation);
            speaker = (TextView) view.findViewById(R.id.speakerName);
            speakerInfo = (TextView) view.findViewById(R.id.speakerInfo);
            speechSummary = (TextView) view.findViewById(R.id.speechSummary);
            startTime = (TextView) view.findViewById(R.id.value_startTime);
            endTime = (TextView) view.findViewById(R.id.value_endTime);
            deleteButton = (ImageView) view.findViewById(R.id.button_delete);
            editSpeechButton = (ImageView) view.findViewById(R.id.button_editSpeech);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        }
    }

    public SpeechAdapter(AppCompatActivity context, ArrayList<Speech> speeches) {
        super(context, R.layout.item_speech, speeches);
        this.activity = context;
        this.speeches = speeches;
        speechHandler = new SpeechHandler(activity);


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Speech speech = getItem(position);
        ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_speech, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
           // LayoutInflater inflater = LayoutInflater.from(getContext());
           /* viewHolder.speechId = (TextView) convertView.findViewById(R.id.value_speech_speechId);
            viewHolder.speechTitle= (TextView) convertView.findViewById(R.id.speechTitle);
            viewHolder.speechRoom = (TextView) convertView.findViewById(R.id.speechLocation);
            viewHolder.speaker = (TextView) convertView.findViewById(R.id.speakerName);
            viewHolder.speakerInfo = (TextView) convertView.findViewById(R.id.speakerInfo);
            viewHolder.speechSummary = (TextView) convertView.findViewById(R.id.speechSummary);
            viewHolder.startTime = (TextView) convertView.findViewById(R.id.value_startTime);
            viewHolder.endTime = (TextView) convertView.findViewById(R.id.value_endTime);
            viewHolder.deleteButton = (ImageView) convertView.findViewById(R.id.button_delete);
            viewHolder.editSpeechButton = (ImageView) convertView.findViewById(R.id.button_editSpeech);*/
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.speechId.setText(speech.getSpeechId());
        viewHolder.speechTitle.setText(speech.getSpeechTitle());
        viewHolder.speechRoom.setText(speech.getSpeechRoom());
        viewHolder.speaker.setText(speech.getSpeaker());
        viewHolder.speakerInfo.setText(speech.getSpeakerInfo());
        viewHolder.speechSummary.setText(speech.getSpeechSummary());
        viewHolder.startTime.setText(speech.timeToString(speech.getStartTime()));
        viewHolder.endTime.setText(speech.timeToString(speech.getEndTime()));

        viewHolder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(viewHolder, position));
        viewHolder.ratingBar.setTag(position);
        viewHolder.ratingBar.setRating(2);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String role = sharedPref.getString("role", "role");
        final boolean isInFuture = sharedPref.getBoolean("isInFuture", true);
        if (role.equals("ADMIN") && isInFuture) {
            viewHolder.deleteButton.setVisibility(View.VISIBLE);
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //Alert Dialog
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    System.out.println("Delete Speech " + speech.getUrl());
                                    speechHandler.deleteSpeech(speech.getUrl());
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    System.out.println("Speech is not deleted");
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
            //
            //speechHandler.deleteSpeech(speech.getUrl());
            //     }});

            viewHolder.editSpeechButton.setVisibility(View.VISIBLE);
            viewHolder.editSpeechButton.setOnClickListener(new View.OnClickListener() {

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

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder viewHolder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                /*Speech speech = getItem(position);
                speech.setRating((int)rating);*/
                viewHolder.ratingBar.setRating((int) rating);
                Log.i("Adapter", "star: " + rating);
            }
        };
    }
}

