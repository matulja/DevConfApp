package com.senacor.devconfapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.EventHandler;

import static com.senacor.devconfapp.R.layout.activity_events;

/**
 * Created by Berlina on 30.11.16.
 */

public class EventListActivity extends AppCompatActivity {

    EventHandler eventHandler = new EventHandler(this);
    public SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_events);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        eventHandler.getEventList();
    }

    // in jeder Activity überschreiben, oder von TabActivity(default--> alle Methods) erben
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.create_new_event:

                Intent intent2 = new Intent(getApplicationContext(), CreateEventActivity.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem newEvent = menu.findItem(R.id.create_new_event);
        newEvent.setVisible(sharedPref.getString("role", "role").equals("ADMIN"));

        MenuItem showAllEvents = menu.findItem(R.id.list_all_events);
        showAllEvents.setVisible(false);
        return true;
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



}


