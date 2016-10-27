package com.senacor.devconfapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    //activity started

    @Override
    protected void onStart() {
        super.onStart();
        new HttpRequestTask().execute();
    }

    //execute the HTTP request when the "Refresh" menu item is selected

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new HttpRequestTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // This task will make an HTTP request to the provided URL and match the response against the provided regular expression.

    private class HttpRequestTask extends AsyncTask<Void, Void, Event> {
        @Override
        protected Event doInBackground(Void... params) {
            try {
                final String url = "http://localhost:8080/event";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Event event = restTemplate.getForObject(url, Event.class);
                return event;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Event event) {
            TextView eventIdText = (TextView) findViewById(R.id.id_value);
            TextView eventNameText = (TextView) findViewById(R.id.content_value);
            eventIdText.setText(event.getId());
            eventNameText.setText(event.getName());
        }

    }

}

