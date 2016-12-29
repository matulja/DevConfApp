package com.senacor.devconfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.EventHandler;

import static com.senacor.devconfapp.R.layout.activity_events;

/**
 * Created by Berlina on 30.11.16.
 */

public class EventListActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {

    ListView eventList;
    EventHandler eventHandler = new EventHandler(this);
    public static String URL = IPAddress.IPevent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_events);
        eventHandler.getEventList(URL);
    }

    // in jeder Activity überschreiben, oder von TabActivity(default--> alle Methods) erben
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {

/*
            case R.id.list_all_events:

                Intent intent = new Intent(EventListActivity.this, EventListActivity.class);
                EventListActivity.this.startActivity(intent);
                break;
*/

            case R.id.create_new_event:

                Intent intent2 = new Intent(getApplicationContext(), CreateEventActivity.class);
                intent2.putExtra("hasNoEvent", false);
                startActivity(intent2);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
    


}


