
package com.senacor.devconfapp.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.SpeechHandler;
import com.senacor.devconfapp.handlers.SpeechRatingHandler;
import com.senacor.devconfapp.models.SpeechRating;

import org.joda.time.LocalTime;



/**
 * Created by saba on 25.12.16.
 */


public class SpeechRatingDialog extends DialogFragment {

    RatingBar speechRating;
    //EditText comment;
    SpeechRatingHandler speechRatingHandler;
    SpeechHandler speechHandler;


    public SpeechRatingDialog() {

    }

    //needed for editing eventRating
    public static SpeechRatingDialog newInstance(SpeechRating speechRating, boolean ratingExists) {
        SpeechRatingDialog speechRatingDialog = new SpeechRatingDialog();
        Bundle args = new Bundle();
        if (ratingExists) {
            args.putString("url", speechRating.getUrl());
        }
        args.putInt("rating", speechRating.getRating());
        args.putString("speechId", speechRating.getSpeechId());
        args.putString("userId", speechRating.getUserId());
        args.putBoolean("ratingExists", ratingExists);
        speechRatingDialog.setArguments(args);
        return speechRatingDialog;
    }


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.speechrating_popup, null);
        //comment = (EditText) view.findViewById(R.id.textCommentEventRating);
        speechRating = (RatingBar) view.findViewById(R.id.editRatingSpeechBar);
        speechRating.setRating(getArguments().getInt("rating"));
        //comment.setText(getArguments().getString("comment"));

        speechRating.setOnRatingBarChangeListener(onRatingChangedListener());
        final String userId = getArguments().getString("userId");
        final String speechId = getArguments().getString("speechId");
        final boolean ratingExists = getArguments().getBoolean("ratingExists");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(view)


                // Add action buttons
                .setPositiveButton(R.string.submitRating, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        System.out.println("rated values are set");
                        Activity activity = (Activity) getContext();
                        speechRatingHandler = new SpeechRatingHandler(activity);
                        speechHandler = new SpeechHandler(activity);
                        int roundoff_rating = (int) Math.round(speechRating.getRating());
                        String rating = "Your submitted rating : " + roundoff_rating;
                        Toast.makeText(activity, rating, Toast.LENGTH_LONG).show();
                        RequestParams params = new RequestParams();
                        params.put("rating", roundoff_rating);
                        params.put("userId", userId);
                        params.put("speechId", speechId);
                        params.put("timestamp", new LocalTime().now());
                        if (ratingExists) {
                            String url = getArguments().getString("url");
                            speechRatingHandler.putSpeechRating(url + "/edit", params);
                        }else{
                            speechRatingHandler.postSpeechRating(IPAddress.IPrating + "/speeches/" + speechId + "/" +userId + "/add", params);
                        }

                    }
                })
                .setNegativeButton(R.string.cancelRating, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SpeechRatingDialog.this.getDialog().cancel();
                    }
                });


        return builder.create();

    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener() {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                int roundoff_rating = (int) Math.round(rating);
                speechRating.setRating(roundoff_rating);
            }
        };
    }
}

