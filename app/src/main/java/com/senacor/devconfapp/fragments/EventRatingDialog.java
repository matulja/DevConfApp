package com.senacor.devconfapp.fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.EventRatingHandler;
import com.senacor.devconfapp.models.EventRating;


/**
 * Created by saba on 25.12.16.
 */

public class EventRatingDialog extends DialogFragment {

    RatingBar contentRating, informationRating, caterRating, locationRating;
    EditText comment;
    EventRatingHandler eventRatingHandler;


    public EventRatingDialog() {

    }

    //needed for editing eventRating
    public static EventRatingDialog newInstance(EventRating eventRating, boolean ratingExists) {
        EventRatingDialog eventRatingDialog = new EventRatingDialog();
        Bundle args = new Bundle();
        args.putInt("caterRating", eventRating.getCaterRating());
        args.putInt("informationRating", eventRating.getInformationRating());
        args.putInt("contentRating", eventRating.getContentRating());
        args.putInt("locationRating", eventRating.getLocationRating());
        args.putString("comment", eventRating.getSuggestions());
        args.putBoolean("ratingExists", ratingExists);
        args.putString("url", eventRating.getUrl());
        eventRatingDialog.setArguments(args);
        return eventRatingDialog;
    }


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.rating_popup, null);
        eventRatingHandler = new EventRatingHandler(getActivity());


        comment = (EditText) view.findViewById(R.id.textCommentEventRating);
        contentRating = (RatingBar) view.findViewById(R.id.ratingBarTopicSelection);
        informationRating = (RatingBar) view.findViewById(R.id.ratingBarInformation);
        caterRating = (RatingBar) view.findViewById(R.id.ratingBarCatering);
        locationRating = (RatingBar) view.findViewById(R.id.ratingBarLocation);

        final boolean ratingExists = getArguments().getBoolean("ratingExists");

        contentRating.setRating(getArguments().getInt("contentRating"));
        informationRating.setRating(getArguments().getInt("informationRating"));
        caterRating.setRating(getArguments().getInt("caterRating"));
        locationRating.setRating(getArguments().getInt("locationRating"));
        comment.setText(getArguments().getString("comment"));



        contentRating.setOnRatingBarChangeListener(onRatingChangedListener());
        informationRating.setOnRatingBarChangeListener(onRatingChangedListener());
        caterRating.setOnRatingBarChangeListener(onRatingChangedListener());
        locationRating.setOnRatingBarChangeListener(onRatingChangedListener());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(view)


                // Add action buttons
                .setPositiveButton(R.string.submitRating, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        System.out.println("rated values are set");
                        RequestParams params = new RequestParams();
                        // params.put("speechTitle", speechTitle);
                        params.put("caterRating", (int)caterRating.getRating());
                        params.put("locationRating", (int)locationRating.getRating());
                        params.put("informationRating",(int) informationRating.getRating());
                        params.put("contentRating", (int) contentRating.getRating());
                        params.put("suggestions", comment.getText());
                        Log.i("url", getArguments().getString("url"));
                        if (ratingExists) {
                            eventRatingHandler.putEventRating(getArguments().getString("url"), params);
                        } else{
                            eventRatingHandler.postEventRating(getArguments().getString("url"), params);
                        }

                    }
                })
                .setNegativeButton(R.string.cancelRating, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EventRatingDialog.this.getDialog().cancel();
                    }
                });


        return builder.create();

    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener() {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                int roundoff_rating = (int) Math.round(rating);
                ratingBar.setRating(roundoff_rating);
            }
        };
    }

}
