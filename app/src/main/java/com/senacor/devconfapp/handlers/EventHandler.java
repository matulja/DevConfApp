package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.view.View.VISIBLE;
import static com.senacor.devconfapp.activities.EventActivity.URL;

/**
 * Created by saba on 13.12.16.
 */

public class EventHandler {

    public static boolean eventsPresent = true;
    SharedPreferences sharedPref;
    ListView eventList;
    TextView noEvents;
    AttendanceHandler attendanceHandler;
    SpeechHandler speechHandler;
    EventRatingHandler eventRatingHandler;
    private Event event;
    private Activity activity;


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
                activity.finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                errorWithoutJson(statusCode);

            }
        });
    }


    public void getEvent(final String url) {
        System.out.println("in event handler/url" + url);

        AsynchRestClient.get(activity, url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                attendanceHandler = new AttendanceHandler(activity);
                speechHandler = new SpeechHandler(activity);
                eventRatingHandler = new EventRatingHandler(activity);
                String[] urlElements = url.split("/");
                final String eventId = urlElements[urlElements.length - 1];
                event = new Event(jsonObject);

                TextView eventName = (TextView) activity.findViewById(R.id.event_name);
                eventName.setText(event.getName());


                TextView eventPlace = (TextView) activity.findViewById(R.id.event_place);
                if (event.getPlace().isEmpty()) {
                    eventPlace.setVisibility(View.GONE);
                } else {
                    eventPlace.setText(event.getPlace());
                }
                TextView eventDate = (TextView) activity.findViewById(R.id.event_date);
                eventDate.setText(event.dateToString());

                TextView eventStreetAndNumber = (TextView) activity.findViewById(R.id.event_streetAndNumber);
                if (event.getStreetAndNumber().isEmpty()) {
                    eventStreetAndNumber.setVisibility(View.GONE);
                } else {
                    eventStreetAndNumber.setText(event.getStreetAndNumber());
                }

                TextView eventPostalCodeAndCity= (TextView) activity.findViewById(R.id.event_postalCodeAndCity);
                if (event.getPostalCodeAndCity().isEmpty()) {
                    eventPostalCodeAndCity.setVisibility(View.GONE);
                } else {
                    eventPostalCodeAndCity.setText(event.getPostalCodeAndCity());
                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isInFuture", event.getDate().isAfter(LocalDate.now()));
                System.out.println("event is today = " + event.getDate().isEqual(LocalDate.now()));
                editor.putBoolean("isToday", event.getDate().isEqual(LocalDate.now()));
                editor.commit();

                speechHandler.getSpeeches(URL + "/speeches");
                ImageView addSpeechButton = (ImageView) activity.findViewById(R.id.addSpeechButton);
                ImageView editEventButton = (ImageView) activity.findViewById(R.id.button_editEventButton);
                ImageView deleteEventButton = (ImageView) activity.findViewById(R.id.button_deleteEventButton);

                if (sharedPref.getBoolean("isInFuture", true) || sharedPref.getBoolean("isToday", true)) {

                    attendanceHandler.setAttendanceButton(URL + "/attendees/" + sharedPref.getString("userId", "userId"));

                    if (sharedPref.getString("role", "role").equals("ADMIN")) {
                        addSpeechButton.setVisibility(VISIBLE);
                        addSpeechButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment speechFragment = SpeechDialog.newInstance(null, false, false);
                                speechFragment.show(activity.getFragmentManager(), "SpeechDialog");

                            }
                        });

                        editEventButton.setVisibility(VISIBLE);
                        editEventButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, CreateEventActivity.class);
                                intent.putExtra("name", event.getName());
                                intent.putExtra("place", event.getPlace());
                                intent.putExtra("date", event.getDate().toString());
                                intent.putExtra("streetAndNumber",event.getStreetAndNumber());
                                intent.putExtra("postalCodeAndCity", event.getPostalCodeAndCity());
                                intent.putExtra("eventId", eventId);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        });

                        deleteEventButton.setVisibility(VISIBLE);
                        deleteEventButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Alert Dialog
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                System.out.println("Delete Event " + event.getUrl());
                                                deleteEvent(event.getUrl());
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                System.out.println("Event is not deleted" );
                                                dialog.cancel();
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setMessage("Do you want delete this event?")
                                        .setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener)
                                        .show();

                            }
                        });

                    }
                }

                if(event.getDate().isBefore(LocalDate.now()) || event.getDate().isEqual(LocalDate.now())){
                    String userId = sharedPref.getString("userId", "userId");
                    eventRatingHandler.getEventRating(eventId, userId);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                errorWithoutJson(statusCode);
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
                        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

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
                                    activity.finish();

                                }

                            });
                        }
                        if(sharedPref.getString("role", "role").equals("ADMIN")){
                            ImageView addEventButton = (ImageView) activity.findViewById(R.id.addEventButton);
                            addEventButton.setVisibility(View.VISIBLE);
                            addEventButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Activity activity = new Activity();
                                    Intent intent1 = new Intent(activity, CreateEventActivity.class);
                                    activity.startActivity(intent1);
                                    activity.finish();

                                }
                            });

                        }

                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        errorWithoutJson(statusCode);
                    }
                });


    }

    public void addEvent(RequestParams params) {
        AsynchRestClient.post(activity, IPAddress.IPevent + "/createEvent", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("Information", "in speechhandler add speech method");
                Log.i("Information", "speeches were successfully added");
                event = new Event(response);
                if (event != null) {
                    Intent intent = new Intent(activity, EventActivity.class);
                    intent.putExtra("url", event.getUrl());
                    activity.startActivity(intent);
                    activity.finish();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                errorWithJson(statusCode, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                errorWithoutJson(statusCode);
            }

        });
    }

    public void deleteEvent(final String url) {
        AsynchRestClient.delete(activity, url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println("in event handler/url");
                System.out.println("in delete event");
                Intent intent = new Intent(activity, EventListActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                errorWithoutJson(statusCode);
            }
        });
    }

    public void editEvent(final String url, RequestParams params) {
        AsynchRestClient.put(activity, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("Information", "in editEventHandler");
                event = new Event(response);
                if (event != null) {
                    Intent intent = new Intent(activity, EventActivity.class);
                    intent.putExtra("url", url);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                errorWithJson(statusCode, errorResponse);
            }
        });
    }

    private void errorWithoutJson(int statusCode) {
        switch (statusCode) {
            case 401:
                ErrorHandler.handleUnauthorizedError(activity);
                break;
            default:
                CharSequence text = "Sorry, your request could not be handled. Please try again.";
                int duration = Toast.LENGTH_LONG;
                Context context = (Context) activity;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                activity.finish();
                break;
        }

    }

    private void errorWithJson(int statusCode, JSONObject errorResponse) {
        switch (statusCode) {
            case 401:
                ErrorHandler.handleUnauthorizedError(activity);
                break;
            case 409:
                Event event = new Event(errorResponse);
                ErrorHandler.handleConflictError(event, activity);
                break;
            default:
                CharSequence text = "Sorry, your request could not be handled. Please try again.";
                int duration = Toast.LENGTH_LONG;
                Context context = (Context) activity;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                activity.finish();
                break;
        }
    }

}



