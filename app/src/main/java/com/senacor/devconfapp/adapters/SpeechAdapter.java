package com.senacor.devconfapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.senacor.devconfapp.R;
import com.senacor.devconfapp.fragments.SpeechDialog;
import com.senacor.devconfapp.handlers.SpeechHandler;
import com.senacor.devconfapp.models.Speech;

import java.util.ArrayList;

/**
 * Created by Veronika Babic on 14.11.2016.
 */

public class SpeechAdapter extends ArrayAdapter<Speech> {

    SpeechHandler speechHandler;

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
        Button submitRatingButton;

    }

    public SpeechAdapter(Context context, ArrayList<Speech> speeches) {
        super(context, R.layout.item_speech, speeches);
        speechHandler = new SpeechHandler((Activity) getContext());


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Speech speech = getItem(position);
        final SpeechAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new SpeechAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_speech, parent, false);
            viewHolder.speechId = (TextView) convertView.findViewById(R.id.value_speech_speechId);
            viewHolder.speechTitle= (TextView) convertView.findViewById(R.id.speechTitle);
            viewHolder.speechRoom = (TextView) convertView.findViewById(R.id.speechLocation);
            viewHolder.speaker = (TextView) convertView.findViewById(R.id.speakerName);
            viewHolder.speakerInfo = (TextView) convertView.findViewById(R.id.speakerInfo);
            viewHolder.speechSummary = (TextView) convertView.findViewById(R.id.speechSummary);
            viewHolder.startTime = (TextView) convertView.findViewById(R.id.value_startTime);
            viewHolder.endTime = (TextView) convertView.findViewById(R.id.value_endTime);
            viewHolder.deleteButton = (ImageView) convertView.findViewById(R.id.button_delete);
            viewHolder.editSpeechButton = (ImageView) convertView.findViewById(R.id.button_editSpeech);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingSpeechBar);
            viewHolder.submitRatingButton = (Button) convertView.findViewById(R.id.submitRating_Button);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SpeechAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.speechId.setText(speech.getSpeechId());
        viewHolder.speechTitle.setText(speech.getSpeechTitle());
        viewHolder.speechRoom.setText(speech.getSpeechRoom());
        viewHolder.speaker.setText(speech.getSpeaker());
        viewHolder.speakerInfo.setText(speech.getSpeakerInfo());
        viewHolder.speechSummary.setText(speech.getSpeechSummary());
        viewHolder.startTime.setText(speech.timeToString(speech.getStartTime()));
        viewHolder.endTime.setText(speech.timeToString(speech.getEndTime()));
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        // perform click event on Submitbutton
        viewHolder.ratingBar.setFocusable(false);
        viewHolder.ratingBar.setTag(position);
        /*viewHolder.ratingBar.setRating(getItem(position));*/
        viewHolder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("setRatingListener");
                String totalStars = "Total Stars:: " + viewHolder.ratingBar.getNumStars();
                String rating = "Rating :: " + viewHolder.ratingBar.getRating();
                Toast.makeText(getContext(), totalStars + "\n" + rating, Toast.LENGTH_LONG).show();
                return true;
            }
        });
       /* viewHolder.submitRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get values and then displayed in a toast
                Activity activity = (Activity) getContext();
                String totalStars = "Total Stars:: " + viewHolder.ratingBar.getNumStars();
                String rating = "Rating :: " + viewHolder.ratingBar.getRating();
                Toast.makeText(activity, totalStars + "\n" + rating, Toast.LENGTH_LONG).show();

            }
        });*/
        final String role = sharedPref.getString("role", "role");
        final boolean isInFuture = sharedPref.getBoolean("isInFuture", true);
        if (role.equals("ADMIN") && isInFuture) {
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
            //
                    //speechHandler.deleteSpeech(speech.getUrl());
           //     }});

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

