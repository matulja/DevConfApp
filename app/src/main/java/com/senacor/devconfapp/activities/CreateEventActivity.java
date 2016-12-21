package com.senacor.devconfapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.senacor.devconfapp.R;

/**
 * Created by Marynasuprun on 12.12.2016.
 */

    public class CreateEventActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_event);
        }

     //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    }

