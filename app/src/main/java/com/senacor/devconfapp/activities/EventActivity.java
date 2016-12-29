package com.senacor.devconfapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.AttendanceHandler;
import com.senacor.devconfapp.handlers.EventHandler;
import com.senacor.devconfapp.handlers.SpeechHandler;

import static com.senacor.devconfapp.R.layout.event;


public class EventActivity extends AppCompatActivity {


    private SharedPreferences sharedPref;
    public static String URL;
    EventHandler eventHandler = new EventHandler(EventActivity.this);
    public SpeechHandler speechHandler = new SpeechHandler(EventActivity.this);
    AttendanceHandler attendanceHandler = new AttendanceHandler(EventActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(event);
        URL = getIntent().getExtras().getString("url");
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        eventHandler.getEvent(URL);
        speechHandler.getSpeeches(URL + "/speeches");
        attendanceHandler.setAttendanceButton(URL + "/attendees/" + sharedPref.getString("userId", "userId"));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.list_all_events:

                Intent intent = new Intent(EventActivity.this, EventListActivity.class);
                EventActivity.this.startActivity(intent);
                return true;

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
        return true;
    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.main, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        protected void onStart () {
            super.onStart();
        }

    }


