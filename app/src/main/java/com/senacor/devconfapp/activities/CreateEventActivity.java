package com.senacor.devconfapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    EditText eventName, eventPlace, eventStreetAndNumber, eventPostalCodeAndCity;
    Button saveEvent, cancelEvent;
    ValidationHandler validationHandler;
    TextView invalidEventData, invalidEventInput, createEventHeadline;
    SharedPreferences sharedPref;
    private String eventId;
    boolean editing = false;
    boolean needsValidation = false;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        toolbar=(Toolbar) findViewById(R.id.menutoolbar);
        setSupportActionBar(toolbar);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        eventHandler = new EventHandler(this);
        validationHandler = new ValidationHandler();
        //assign fields to view
        eventName = (EditText) findViewById(R.id.event_name);
        eventPlace = (EditText) findViewById(R.id.event_place);
        eventStreetAndNumber = (EditText) findViewById(R.id.event_streetNumber);
        eventPostalCodeAndCity= (EditText) findViewById(R.id.event_postalCodeCity);
        eventDatePicker = (DatePicker) findViewById(R.id.eventDatePicker);
        invalidEventData = (TextView) findViewById(R.id.event_validationErrorDate);
        invalidEventInput = (TextView) findViewById(R.id.event_validationErrorInput);
        createEventHeadline = (TextView) findViewById(R.id.createEventHeadline);
        final Bundle info = getIntent().getExtras();

        if (info != null) {
            needsValidation = true;
            if (info.getString("eventId") != null){
                eventId = info.getString("eventId");
                createEventHeadline.setText("Edit Event");
                editing = true;
            }
            eventName.setText(info.getString("name"));
            eventPlace.setText(info.getString("place"));
            eventStreetAndNumber.setText(info.getString("streetAndNumber"));
            eventPostalCodeAndCity.setText(info.getString("postalCodeAndCity"));
            LocalDate date = LocalDate.parse(info.getString("date"));
            int year = date.getYear();
            int month = date.getMonthOfYear();
            int day = date.getDayOfMonth();

            eventDatePicker.updateDate(year, (month - 1), day);
            if (info.getBoolean("validationErrorDate")) {
                invalidEventData.setVisibility(View.VISIBLE);
            }
            if (info.getBoolean("validationErrorInput")) {
                invalidEventInput.setVisibility(View.VISIBLE);
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
                final String streetAndNumber = eventStreetAndNumber.getText().toString();
                final String postalCodeAndCity = eventPostalCodeAndCity.getText().toString();

                int day = eventDatePicker.getDayOfMonth();
                int month = eventDatePicker.getMonth() + 1;
                int year = eventDatePicker.getYear();

                LocalDate eventDate = new LocalDate(year, month, day);


                if (validationHandler.isNotInFuture(eventDate) || validationHandler.isNotFilled(name)) {
                    Intent intent = new Intent(CreateEventActivity.this, CreateEventActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("place", place);
                    intent.putExtra("date", eventDate.toString());
                    intent.putExtra("streetAndNumber", streetAndNumber);
                    intent.putExtra("postalCodeAndCity", postalCodeAndCity);
                    intent.putExtra("validationErrorDate", validationHandler.isNotInFuture(eventDate));
                    intent.putExtra("validationErrorInput", (validationHandler.isNotFilled(name) || validationHandler.isNotFilled(place)));
                    if (editing) {
                        intent.putExtra("eventId", eventId);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    RequestParams params = new RequestParams();
                    params.put("name", name);
                    params.put("place", place);
                    params.put("date", eventDate);
                    params.put("streetAndNumber", streetAndNumber);
                    params.put("postalCodeAndCity", postalCodeAndCity);
                    String url = IPAddress.IPevent + "/" + eventId;
                    if (editing) {
                        params.put("eventId", eventId);
                        eventHandler.editEvent(url, params);
                    } else {
                        eventHandler.addEvent(params);
                    }
                }


            }
        });

        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.out.println("Cancel Button is pressed");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        MenuItem newEvent = menu.findItem(R.id.list_all_events);
        newEvent.setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
                return super.onCreateOptionsMenu(menu);
    }
}

