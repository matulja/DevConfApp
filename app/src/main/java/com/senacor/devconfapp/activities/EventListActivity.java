package com.senacor.devconfapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.EventHandler;
import com.senacor.devconfapp.handlers.LogInOutHandler;

import static com.senacor.devconfapp.R.layout.activity_events;

/**
 * Created by Berlina on 30.11.16.
 */

public class EventListActivity extends AppCompatActivity {

    EventHandler eventHandler = new EventHandler(this);
    public SharedPreferences sharedPref;
    private Toolbar toolbar;
    private TextView textViewToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_events);
        toolbar=(Toolbar) findViewById(R.id.toolbar_event);
        textViewToolbar= (TextView)findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        textViewToolbar.setText("All DevCons");
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        eventHandler.getEventList();
    }

    // in jeder Activity überschreiben, oder von TabActivity(default--> alle Methods) erben
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.backToEvent:

                Intent intent2 = new Intent(EventListActivity.this, EventActivity.class);
                intent2.putExtra("url", sharedPref.getString("url", "url"));
                EventListActivity.this.startActivity(intent2);
                return true;

            case R.id.action_log_out:
                LogInOutHandler logInOutHandler = new LogInOutHandler(this);
                RequestParams params = new RequestParams();
                params.put("tokenId",sharedPref.getString("tokenId", "tokenId"));
                params.put("role", sharedPref.getString("role", "role"));
                params.put("userId", sharedPref.getString("userId", "userId"));
                logInOutHandler.logout(params);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
/*      MenuItem newEvent = menu.findItem(R.id.list_all_events);
        newEvent.setVisible(sharedPref.getString("role", "role").equals("ADMIN"));*/
        MenuItem showAllEvents = menu.findItem(R.id.list_all_events);
        showAllEvents.setVisible(false);
        MenuItem backToEvent = menu.findItem(R.id.backToEvent);
        backToEvent.setVisible(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



}


