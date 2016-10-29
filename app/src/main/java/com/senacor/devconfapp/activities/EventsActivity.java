package com.senacor.devconfapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.adapters.EventAdapter;
import com.senacor.devconfapp.clients.EventRestClient;
import com.senacor.devconfapp.models.Event;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public class EventsActivity extends AppCompatActivity {

    private ListView eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        getEvents();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getEvents() {

        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Accept", "application/json"));

        EventRestClient.get(EventsActivity.this, "event", headers.toArray(new Header[headers.size()]),
                null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        ArrayList<Event> eventArray = new ArrayList<Event>();
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
}
