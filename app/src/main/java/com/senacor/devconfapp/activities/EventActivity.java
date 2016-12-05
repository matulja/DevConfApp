package com.senacor.devconfapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.adapters.SpeechAdapter;
import com.senacor.devconfapp.clients.RestClient;
import com.senacor.devconfapp.models.Event;
import com.senacor.devconfapp.models.Speech;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.message.BasicHeader;

import static com.senacor.devconfapp.R.id.username;
import static com.senacor.devconfapp.R.layout.event;


public class EventActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener{

    TextView welcome;
    TextView eventName;
    TextView eventPlace;
    TextView eventDate;
    ListView speechlist;
    MenuItem list_all_events;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_events);
        //setContentView(R.layout.list_speeches);
        setContentView(event);
/*        Intent intent = getIntent();
        String greeting = "Welcome ";
        String username = intent.getStringExtra("username");
        welcome = (TextView) findViewById(R.id.welcome);
        welcome.setText(greeting + username);*/
        String url = getIntent().getExtras().getString("url");
        getEvent(url);

       // sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
       // String tokenId = sharedPref.getString("tokenId", "tokenId");
       // System.out.println(tokenId);
    }

    public String getToken()

    {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String tokenId = sharedPref.getString("tokenId", "tokenId");
        return tokenId;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.list_all_events){
            Intent intent = new Intent(EventActivity.this, EventListActivity.class);
            intent.putExtra("username", username);
            EventActivity.this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
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

    private void getEvent(String url) {

        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader ("Authorization", getToken()));
        System.out.println("Header in getCurrentEvent" +headers.toString());

        RestClient.get(EventActivity.this, url, headers.toArray(new Header[headers.size()]),
                null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {

                        Event event = new Event(jsonObject);
                        System.out.println(event.getName());
                        System.out.println(event.getPlace());

                        eventName = (TextView)findViewById(R.id.event_name);
                        eventPlace = (TextView)findViewById(R.id.event_place);
                        eventDate = (TextView)findViewById(R.id.event_date);

                        eventName.setText(event.getName());
                        eventPlace.setText(event.getPlace());
                        eventDate.setText(event.getDate());

                        //JsonObject in KLasse überführen - Hal-Object / Resource .getLink();
                        String speechesUrl = "";
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("links");
                            System.out.println(jsonArray.length() + " ");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if(jsonArray.getJSONObject(i).getString("rel").equals("speeches")){
                                    speechesUrl = jsonArray.getJSONObject(i).getString("href");
                                    System.out.println(speechesUrl);

                                };
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!(speechesUrl.equals(""))) {
                            getSpeeches(speechesUrl);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);                    }
                });
    }

    private void getSpeeches(String speechesUrl) {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader ("Authorization", getToken()));
        System.out.println("Header in getSpeeches" +headers.toString());

        RestClient.get(EventActivity.this, speechesUrl, headers.toArray(new Header[headers.size()]),
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        System.out.println("in getSpeeches");
                        ArrayList<Speech> speechArray = new ArrayList<>();
                        SpeechAdapter speechAdapter = new SpeechAdapter(EventActivity.this, speechArray);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                speechAdapter.add(new Speech(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        speechlist = (ListView) findViewById(R.id.list_speeches);
                        speechlist.setAdapter(speechAdapter);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);
                    }

                   /*     for (int i = 0; i < 1; i++) {
                            try {
                                JSONObject jObj = response.getJSONObject(0);
                                String eventID = jObj.getString("eventId");
                                List<Header> headers2 = new ArrayList<>();
                                headers2.add(new BasicHeader("Accept", "application/json"));
                                RestClient.get(EventActivity.this, "event/"+eventID+"/speeches", headers2.toArray(new Header[headers2.size()]),
                                        null, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                                ArrayList<Speech> speechArray = new ArrayList<>();
                                                SpeechAdapter speechAdapter = new SpeechAdapter(EventActivity.this, speechArray);

                                                for (int i = 0; i < response.length(); i++) {
                                                    try {
                                                        speechAdapter.add(new Speech(response.getJSONObject(i)));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                speechList = (ListView) findViewById(R.id.list_speeches);
                                                speechList.setAdapter(speechAdapter);
                                            }
                                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object) {
                                                //super.onFailure(statusCode, headers, responseString, throwable);
                                                Log.d("Failed: ", "" + statusCode);
                                                Log.d("Error : ", "" + throwable);
                                            }
                                        });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }*/




                });


    }

}


