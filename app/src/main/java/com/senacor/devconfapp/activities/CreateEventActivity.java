package com.senacor.devconfapp.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.fragments.DateDialog;
import com.senacor.devconfapp.handlers.EventHandler;

/**
 * Created by Marynasuprun on 12.12.2016.
 */

    public class CreateEventActivity extends AppCompatActivity {

    EventHandler eventHandler;
   /* public static CreateEventActivity newInstance(Event event) {
        CreateEventActivity eventActivity = new CreateEventActivity();
        Bundle args = new Bundle();
        if (event != null) {
            args.putString("eventId", event.getEventId());
            args.putString("name", event.getName());
            argS.putString("place", event.getPlace());
            args.putString("date", event.getDate());
            eventActivity.setArguments(args);
        }
        return eventActivity;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        eventHandler = new EventHandler(this);
        //assign fields to view
        final TextView title = (TextView) findViewById(R.id.createEventHeadline);
        final EditText eventName = (EditText) findViewById(R.id.event_name);
        final EditText eventPlace = (EditText) findViewById(R.id.event_place);
        final EditText eventDate = (EditText) findViewById(R.id.event_date);
        eventDate.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft,"DatePicker");
                }
            }
        });

        final Button createEvent = (Button) findViewById(R.id.create_button);
        final Button cancelEvent = (Button) findViewById(R.id.cancel_button);
        //process Data
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = eventName.getText().toString();
                final String place = eventPlace.getText().toString();
                final String date = eventDate.getText().toString();

                RequestParams params = new RequestParams();
                params.put("name", name);
                params.put("place", place);
                params.put("date", date);

                System.out.println(" " + name);
                System.out.println(" " + place);
                System.out.println(" " + date);

                eventHandler.addEvent(params);
            }
        });

        //Handler for Buttons
        //TODO switch case


        cancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //action on click

                Intent i = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(i);
                System.out.println("Cancel Button is pressed");

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.list_all_events:
                Intent intent = new Intent(CreateEventActivity.this, EventListActivity.class);
                CreateEventActivity.this.startActivity(intent);
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

