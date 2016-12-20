package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.listeners.AttendanceClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saba on 13.12.16.
 */

public class AttendanceHandler {

    private Activity activity;

    public AttendanceHandler(Activity activity) {
        this.activity = activity;
    }

    public void setAttendanceButton (String attendanceUrl) {
        AsynchRestClient.get(activity, attendanceUrl,
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Button joinButton = (Button) activity.findViewById(R.id.joinButton);
                            joinButton.setOnClickListener(new AttendanceClickListener(activity));
                            if (response.getBoolean("isAttending")) {
                                joinButton.setText("Joined");
                            } else {
                                joinButton.setText("Join");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);
                    }


                });
    }

}
