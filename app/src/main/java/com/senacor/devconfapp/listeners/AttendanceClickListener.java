package com.senacor.devconfapp.listeners;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.activities.EventActivity;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.handlers.AttendanceHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saba on 18.12.16.
 */

public class AttendanceClickListener implements View.OnClickListener {

    Button clickedButton;
    private SharedPreferences sharedPref;
    AttendanceHandler attendanceHandler;
    Activity activity;

    public AttendanceClickListener(Activity activity) {
        this.activity = activity;

    }


    @Override
    public void onClick(View v) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(v.getContext());

        //checks if the pressed button is the join button
        if (v.getId() == R.id.joinButton) {

            clickedButton = (Button) v;
            //Opens dialog when Button "Join" get clicked to register/unregister for the conference
            AlertDialog.Builder builder = new AlertDialog.Builder(clickedButton.getContext());
            builder.setTitle("Conference Registration");
            if(clickedButton.getText().equals("Join")){
                builder.setMessage("You successfully registered for the conference.");
            }else{
                builder.setMessage("You successfully unregistered for the conference.");
            }
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(final DialogInterface dialog, int id) {
                    String url = EventActivity.URL + "/attendees/" + sharedPref.getString("userId", "userId");
                    AsynchRestClient.put(clickedButton.getContext(), url, null, new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            System.out.println("putting attendance successful");
                            String url = EventActivity.URL + "/attendees/" + sharedPref.getString("userId", "userId");
                            attendanceHandler = new AttendanceHandler(activity);
                            attendanceHandler.setAttendanceButton(url);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("Failed: ", "" + statusCode);
                            Log.d("Error : ", "" + throwable);
                            System.out.println("putting attendance not successful");
                        }
                    });

                }})
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            builder.create()
                    .show();
        }
    }
}
