package com.senacor.devconfapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.adapters.EventListAdapter;
import com.senacor.devconfapp.clients.RestClient;
import com.senacor.devconfapp.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.senacor.devconfapp.R.layout.activity_events;

/**
 * Created by Berlina on 30.11.16.
 */

public class EventListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, MenuItem.OnMenuItemClickListener {

    ListView eventList;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_events);
        getEventList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private void getEventList() {
        RestClient.get(EventListActivity.this, IPAddress.IPevent+ "/list",
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                        System.out.println("in getEventList");
                        ArrayList<Event> eventListArray = new ArrayList<>();
                        EventListAdapter eventListAdapter = new EventListAdapter(EventListActivity.this, eventListArray);
                        for (int i = 0; i < response.length(); i++) {
                           try {
                                Event event = new Event(response.getJSONObject(i));
                                String eventId2= response.getJSONObject(i).getString("eventId");
                                event.setEventId(eventId2);
                                eventListAdapter.add(event);
                           } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        eventList = (ListView) findViewById(R.id.list_events);
                        eventList.setAdapter(eventListAdapter);
                        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Event event = (Event) eventList.getItemAtPosition(position);
                                Intent intent = new Intent(EventListActivity.this,EventActivity.class);
                                String url = IPAddress.IPevent+"/"+event.getEventId();
                                intent.putExtra("url", url);
                                EventListActivity.this.startActivity(intent);
                            }

                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}


