package com.senacor.devconfapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.EventHandler;

/**
 * Created by Marynasuprun on 12.12.2016.
 */

    public class CreateEventActivity extends Activity {

    EventHandler eventHandler;

   /* public static CreateEventActivity newInstance(Event event) {
        CreateEventActivity eventActivity = new CreateEventActivity();
        Bundle args = new Bundle();
        if (event != null) {
            args.putString("eventId", event.getEventId());
            args.putString("name", event.getName());
            args.putString("place", event.getPlace());
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
        //read EventDate
        final EditText eventName = (EditText) findViewById(R.id.event_name);
        final EditText eventPlace = (EditText) findViewById(R.id.event_place);
        final EditText eventDate = (EditText) findViewById(R.id.event_date);

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


    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

