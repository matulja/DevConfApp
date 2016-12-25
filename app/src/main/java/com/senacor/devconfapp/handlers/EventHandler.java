package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.activities.EventActivity;
import com.senacor.devconfapp.adapters.EventListAdapter;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saba on 13.12.16.
 */

public class EventHandler {

    private Event event;
    private Activity activity;
    SharedPreferences sharedPref;
    ListView eventList;

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

                        /*ImageButton addSpeechButton = (ImageButton) activity.findViewById(R.id.addSpeechButton);


                        if (sharedPref.getString("role", "role").equals("ADMIN")) {
                            addSpeechButton.setVisibility(VISIBLE);
                            addSpeechButton.setOnClickListener(new SpeechClickListener(activity));
                        }*/

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        System.out.println(statusCode);
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);
                    }
                });
    }


    public void getEventList(String url) {

        AsynchRestClient.get(activity, url,
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                        System.out.println("in getEventList");
                        eventList = (ListView) activity.findViewById(R.id.list_events);
                        ArrayList<Event> eventListArray = new ArrayList<>();
                        EventListAdapter eventListAdapter = new EventListAdapter(activity, eventListArray);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Event event = new Event(response.getJSONObject(i));
                                String id = response.getJSONObject(i).getString("eventId");
                                event.setEventId(id);
                                eventListAdapter.add(event);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        eventList.setAdapter(eventListAdapter);
                        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Event event = (Event) eventList.getItemAtPosition(position);
                                Intent intent = new Intent(activity,EventActivity.class);
                                String url = IPAddress.IPevent+"/"+ event.getEventId();
                                intent.putExtra("url", url);
                                activity.startActivity(intent);
                            }

                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Failed: ", " cannot get all events ");
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);
                    }
                });
    }


}
