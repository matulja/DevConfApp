package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    Button joinButton;
    SharedPreferences sharedPref;

    public AttendanceHandler(Activity activity) {
        this.activity = activity;
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

    }

    public void setAttendanceButton (final String attendanceUrl) {
        joinButton = (Button) activity.findViewById(R.id.joinButton);
        joinButton.setVisibility(View.VISIBLE);
        joinButton.setOnClickListener(new AttendanceClickListener(activity));

        AsynchRestClient.get(activity, attendanceUrl,
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getBoolean("isAttending")) {
                                joinButton.setText("Joined");
                                Context context = (Context)activity;
                                CharSequence text = "You are registered for this conference.";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("isAttending", true);
                                editor.commit();
                            } else {
                                joinButton.setText("Join");
                                Context context = (Context)activity;
                                CharSequence text = "You are not registered for this conference.";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("isAttending", false);
                                editor.commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);
                        joinButton.setText("Error, please reload.");
                    }
                });
    }

}
