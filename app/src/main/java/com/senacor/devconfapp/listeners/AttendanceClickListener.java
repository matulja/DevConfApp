package com.senacor.devconfapp.listeners;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.activities.EventActivity;
import com.senacor.devconfapp.clients.AsynchRestClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saba on 18.12.16.
 */

public class AttendanceClickListener implements View.OnClickListener {

    Button clickedButton;
    private SharedPreferences sharedPref;
    //AttendanceHandler attendanceHandler;
    Activity activity;
    EventActivity eventActivity;

    public AttendanceClickListener(Activity activity) {
        this.activity = activity;

    }


    @Override
    public void onClick(View v) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(v.getContext());

        //checks if the pressed button is the join button
        if (v.getId() == R.id.joinButton) {

            clickedButton = (Button) v;
            String url = EventActivity.URL + "/attendees/" + sharedPref.getString("userId", "userId");
            AsynchRestClient.put(clickedButton.getContext(), url, null, new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    System.out.println("putting attendance successful");
                    String url = EventActivity.URL + "/attendees/" + sharedPref.getString("userId", "userId");
                    Intent intent = new Intent(activity, EventActivity.class);
                    intent.putExtra("url", EventActivity.URL);
                    activity.startActivity(intent);
                    activity.finish();
                  /*  attendanceHandler = new AttendanceHandler(activity);
                    attendanceHandler.setAttendanceButton(url);*/

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("Failed: ", "" + statusCode);
                    Log.d("Error : ", "" + throwable);
                    System.out.println("putting attendance not successful");
                }
            });
        }
    }
}
