package com.senacor.devconfapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.EventHandler;
import com.senacor.devconfapp.handlers.LogInOutHandler;
import com.senacor.devconfapp.handlers.ValidationHandler;

import org.joda.time.LocalDate;

/**
 * Created by Marynasuprun on 12.12.2016.
 */

public class CreateEventActivity extends AppCompatActivity {

    EventHandler eventHandler;
    DatePicker eventDatePicker;
    EditText eventName, eventPlace;
    Button saveEvent, cancelEvent;
    ValidationHandler validationHandler;
    TextView invalidEventData;
    SharedPreferences sharedPref;
    private String eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        eventHandler = new EventHandler(this);
        validationHandler = new ValidationHandler();
        //assign fields to view
        eventName = (EditText) findViewById(R.id.event_name);
        eventPlace = (EditText) findViewById(R.id.event_place);
        eventDatePicker = (DatePicker) findViewById(R.id.eventDatePicker);
        invalidEventData = (TextView) findViewById(R.id.event_validationError);

        final Bundle info = getIntent().getExtras();

        if (info != null) {
            if (info.getString("eventId") != null) {
                eventId = info.getString("eventId");
            }
            eventName.setText(info.getString("name"));
            eventPlace.setText(info.getString("place"));
            LocalDate date = LocalDate.parse(info.getString("date"));
            int year = date.getYear();
            int month = date.getMonthOfYear();
            int day = date.getDayOfMonth();

            eventDatePicker.updateDate(year, (month - 1), day);
            if (info.getBoolean("validationErrorDate")) {
                invalidEventData.setVisibility(View.VISIBLE);
            }
            if(info.getBoolean("validationErrorInput")){

            }
        }


        saveEvent = (Button) findViewById(R.id.save_button);
        cancelEvent = (Button) findViewById(R.id.cancel_button);
        //process Data
        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = eventName.getText().toString();
                final String place = eventPlace.getText().toString();

                int day = eventDatePicker.getDayOfMonth();
                int month = eventDatePicker.getMonth() + 1;
                int year = eventDatePicker.getYear();

                LocalDate eventDate = new LocalDate(year, month, day);


                if (validationHandler.isNotInFuture(eventDate) || validationHandler.isNotFilled(name) || validationHandler.isNotFilled(place)) {
                    Intent intent = new Intent(CreateEventActivity.this, CreateEventActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("place", place);
                    intent.putExtra("date", eventDate.toString());
                    startActivity(intent);
                    if (validationHandler.isNotInFuture(eventDate)) {
                        intent.putExtra("validationErrorDate", true);
                    }
                    if (validationHandler.isNotFilled(name) || validationHandler.isNotFilled(place)) {
                        intent.putExtra("validationErrorInput", true);
                    }
                }else{
                    RequestParams params = new RequestParams();
                    params.put("name", name);
                    params.put("place", place);
                    params.put("date", eventDate);
                    if (info != null) {
                        if (info.getString("eventId") != null) {
                            params.put("eventId", eventId);
                            String url = IPAddress.IPevent + "/" + eventId;
                            System.out.println(url);
                            eventHandler.editEvent(url, params);
                        } else{
                            eventHandler.addEvent(params);
                        }

                    } else {
                        eventHandler.addEvent(params);
                    }

                }
                }






        });

        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventHandler.getCurrentEvent();
                System.out.println("Cancel Button is pressed");
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.list_all_events:
                Intent intent = new Intent(CreateEventActivity.this, EventListActivity.class);
                CreateEventActivity.this.startActivity(intent);
                return true;

            case R.id.action_log_out:
                LogInOutHandler logInOutHandler = new LogInOutHandler(this);
                RequestParams params = new RequestParams();
                params.put("tokenId", sharedPref.getString("tokenId", "tokenId"));
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
        MenuItem newEvent = menu.findItem(R.id.create_new_event);
        newEvent.setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

