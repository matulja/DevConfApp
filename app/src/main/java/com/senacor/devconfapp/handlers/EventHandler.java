package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.listeners.ClickListener;
import com.senacor.devconfapp.models.Event;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.view.View.VISIBLE;

/**
 * Created by saba on 13.12.16.
 */

public class EventHandler {

    private Event event;
    private Activity activity;
    SharedPreferences sharedPref;

    public EventHandler(Activity activity) {
        this.activity = activity;
    }


    public void getEvent(String url) {
        System.out.println("in event handler/url" + url);

        AsynchRestClient.get(activity, url, null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

                        event = new Event(jsonObject);
                        System.out.println("eventhandler on success " + event.getName());
                        TextView eventName = (TextView) activity.findViewById(R.id.event_name);
                        eventName.setText(event.getName());

                        TextView eventPlace = (TextView) activity.findViewById(R.id.event_place);
                        eventPlace.setText(event.getPlace());

                        TextView eventDate = (TextView) activity.findViewById(R.id.event_date);
                        eventDate.setText(event.getDate());

                        ImageButton addSpeechButton = (ImageButton) activity.findViewById(R.id.addSpeechButton);
                        addSpeechButton.setOnClickListener(new ClickListener(activity));

                        if (sharedPref.getString("role", "role").equals("ADMIN")) {
                            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View popupView = inflater.inflate(R.layout.popup_layout, null, false);
                            addSpeechButton.setVisibility(VISIBLE);
                        }



                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        System.out.println(statusCode);
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);
                    }
                });
    }


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
