package com.senacor.devconfapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.ListView;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private ListView eventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getEvents();
    }

    private void getEvents() {
        {
            List<Header> headers = new ArrayList<Header>();
            headers.add(new BasicHeader("Accept", "application/json"));

            EventRestClient.get(MainActivity.this, "event",
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            ArrayList<Event> noteArray = new ArrayList<Event>();
                            EventAdapter eventAdapter = new EventAdapter(MainActivity.this, noteArray);

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    eventAdapter.add(new Event(response.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            eventList = (ListView) findViewById(R.id.list_notes);
                            eventList.setAdapter(eventAdapter);
                        }
                    });
        }
    }
}





