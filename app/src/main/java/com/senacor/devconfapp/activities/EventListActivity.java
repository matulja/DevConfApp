package com.senacor.devconfapp.activities;

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


