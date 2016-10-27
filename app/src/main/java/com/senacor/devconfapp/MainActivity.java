package com.senacor.devconfapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new HttpRequestTask().execute();
    }



    private class HttpRequestTask extends AsyncTask<Void, Event, Event> {

        @Override
        protected Event doInBackground(Void... params) {
            try {
                // TODO change IP address to your personal IP address (ifconfig/ipconfig im Terminal eingeben)
                final String url = "http://192.168.2.104:8080/event";
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
            TextView eventNameText = (TextView) findViewById(R.id.name_value);
            eventIdText.setText(event.getId());
            eventNameText.setText(event.getName());
        }
    }
}
