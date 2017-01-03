package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.activities.CreateEventActivity;
import com.senacor.devconfapp.activities.EventActivity;
import com.senacor.devconfapp.activities.EventListActivity;
import com.senacor.devconfapp.adapters.EventListAdapter;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.fragments.SpeechDialog;
import com.senacor.devconfapp.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.view.View.VISIBLE;

/**
 * Created by saba on 13.12.16.
 */

public class EventHandler {

    private Event event;
    private Activity activity;
    SharedPreferences sharedPref;
    ListView eventList;
    TextView noEvents;
    public static boolean eventsPresent = true;
    public static boolean eventDateUsed = false;

    public EventHandler(Activity activity) {
        this.activity = activity;
    }

    public void getCurrentEvent() {
        AsynchRestClient.get(activity, IPAddress.IPevent + "/currentEvent", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String url = "";
                try {
                    url = response.getString("eventId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent;
                //no events present yet
                if (url.equals("noEvent")) {
                    intent = new Intent(activity, EventListActivity.class);

                } else {
                    intent = new Intent(activity, EventActivity.class);
                    intent.putExtra("url", IPAddress.IPevent + "/" + url);
                }
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });
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
                eventDate.setText(event.dateToString());

                ImageButton addSpeechButton = (ImageButton) activity.findViewById(R.id.addSpeechButton);


                if (sharedPref.getString("role", "role").equals("ADMIN")) {
                    addSpeechButton.setVisibility(VISIBLE);
                    addSpeechButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment speechFragment = SpeechDialog.newInstance(null, false, false, "");
                            speechFragment.show(activity.getFragmentManager(), "SpeechDialog");

                        }
                    });
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


    public void getEventList() {

        AsynchRestClient.get(activity, IPAddress.IPevent,
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                        System.out.println("in getEventList");
                        eventList = (ListView) activity.findViewById(R.id.list_events);
                        noEvents = (TextView) activity.findViewById(R.id.info_noEvent);

                        if (response.length() == 0) {
                            noEvents.setVisibility(VISIBLE);
                            eventsPresent = false;
                        } else {
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
                                    Intent intent = new Intent(activity, EventActivity.class);
                                    String url = IPAddress.IPevent + "/" + event.getEventId();
                                    intent.putExtra("url", url);
                                    activity.startActivity(intent);
                                }

                            });
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Failed: ", " cannot get all events ");
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);
                    }
                });
    }

    public void addEvent(RequestParams params) {
        AsynchRestClient.post(activity, IPAddress.IPevent + "/createEvent", params, new JsonHttpResponseHandler() {

            //returns the id of the created event
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("Information", "in speechhandler add speech method");
                Log.i("Information", "speeches were successfully added");
                event = new Event(response);
                if (event != null) {
                    Intent intent = new Intent(activity, EventActivity.class);
                    intent.putExtra("url",event.getUrl());
                    activity.startActivity(intent);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (statusCode == 409) {
                    event = new Event(errorResponse);
                    Intent intent = new Intent(activity, CreateEventActivity.class);
                    intent.putExtra("name", event.getName());
                    intent.putExtra("place", event.getPlace());
                    intent.putExtra("date", event.getDate().toString());
                    intent.putExtra("validationError", true);
                    //intent.putExtra("eventId", event.extractAndSaveEventId());
                    activity.startActivity(intent);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                Log.d("Failed: ", "could not add event");
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });


    }

      public void deleteEvent (final String url) {

        AsynchRestClient.delete(activity, url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println("in event handler/url");
                System.out.println("in delete event");
                Intent intent = new Intent(activity, EventListActivity.class);
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("in delete event: " + throwable);
            }
        });
    }

    public void editEvent(RequestParams params){
        Log.i("Information", "in editEventHandler");
    }



}
