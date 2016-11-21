package com.senacor.devconfapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.adapters.EventAdapter;
import com.senacor.devconfapp.adapters.SpeechAdapter;
import com.senacor.devconfapp.models.Event;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.clients.RestClient;
import com.senacor.devconfapp.models.Speech;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.client.HttpResponseException;
import cz.msebera.android.httpclient.*;
import com.loopj.android.http.*;



public class EventsActivity extends AppCompatActivity {

    private ListView eventList;
    private ListView speechList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_events);
        setContentView(R.layout.list_speeches);

        //getEvents();
        getSpeeches();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getEvents() {

        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Accept", "application/json"));

        RestClient.get(EventsActivity.this, "event/list", headers.toArray(new Header[headers.size()]),
                null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        ArrayList<Event> eventArray = new ArrayList<>();
                        EventAdapter eventAdapter = new EventAdapter(EventsActivity.this, eventArray);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                eventAdapter.add(new Event(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        eventList = (ListView) findViewById(R.id.list_events);
                        eventList.setAdapter(eventAdapter);
                    }
                });
    }

    private void getSpeeches() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Accept", "application/json"));

        RestClient.get(EventsActivity.this, "event/{eventID}/speeches", headers.toArray(new Header[headers.size()]),
                null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        for (int i = 0; i < 1; i++) {
                            try {
                                JSONObject jObj = response.getJSONObject(0);
                                String eventID = jObj.getString("eventId");
                                List<Header> headers2 = new ArrayList<>();
                                headers2.add(new BasicHeader("Accept", "application/json"));
                                EventRestClient.get(EventsActivity.this, "event/"+eventID+"/speeches", headers2.toArray(new Header[headers2.size()]),
                                        null, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                                ArrayList<Speech> speechArray = new ArrayList<>();
                                                SpeechAdapter speechAdapter = new SpeechAdapter(EventsActivity.this, speechArray);

                                                for (int i = 0; i < response.length(); i++) {
                                                    try {
                                                        speechAdapter.add(new Speech(response.getJSONObject(i)));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                speechList = (ListView) findViewById(R.id.list_speeches);
                                                speechList.setAdapter(speechAdapter);
                                            }
                                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                                                //super.onFailure(statusCode, headers, responseString, throwable);
                                                Log.d("Failed: ", "" + statusCode);
                                                Log.d("Error : ", "" + throwable);
                                            }
                                        });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });


    }

}


