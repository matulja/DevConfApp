package com.senacor.devconfapp.fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.models.Event;


/**
 * Created by saba on 25.12.16.
 */

public class EventRatingDialog extends DialogFragment {



    public EventRatingDialog() {

    }

    //needed for editing eventRating
    public static EventRatingDialog newInstance(Event event) {
        EventRatingDialog eventRatingDialog = new EventRatingDialog();
        Bundle args = new Bundle();

//            if (isBeingEdited) {
//                args.putString("speechId", speech.getSpeechId());
//            }
           /* args.putString("speechTitle", speech.getSpeechTitle());
            args.putString("speaker", speech.getSpeaker());
            args.putString("room", speech.getSpeechRoom());
            args.putString("speechStartTime", speech.getStartTime().toString());
            args.putString("speechEndTime", speech.getEndTime().toString());
            args.putBoolean("isBeingEdited", isBeingEdited);
            args.putBoolean("wasNotValidated", wasNotValidated);*/
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

       // tvSpeechId = (TextView) view.findViewById(R.id.speechPopUp_speechId);
       // etSpeechTitle = (EditText) view.findViewById(R.id.addSpeechTitle);


        if (getArguments() != null) {


        }
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


                    }
                })
                .setNegativeButton(R.string.cancelRating, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EventRatingDialog.this.getDialog().cancel();
                    }
                });


        return builder.create();

    }

}
