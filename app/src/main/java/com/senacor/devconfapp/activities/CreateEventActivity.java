package com.senacor.devconfapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.senacor.devconfapp.R;

import static com.senacor.devconfapp.R.id.password;
import static com.senacor.devconfapp.R.id.username;
import static com.senacor.devconfapp.R.layout.event;

/**
 * Created by Marynasuprun on 12.12.2016.
 */

    public class CreateEventActivity extends Activity  {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_event);

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

                    System.out.println(" " + name);
                    System.out.println(" " + place);
                    System.out.println(" " + date);
                }
            });

            //Handler for Buttons
            //TODO switch case

            createEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //action on click
                   // createEvent.setText("Event is created");
                    System.out.println("Create Button is pressed");
                }


            }

            );

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

