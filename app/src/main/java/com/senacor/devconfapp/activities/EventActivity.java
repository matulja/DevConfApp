package com.senacor.devconfapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.adapters.SpeechAdapter;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.handlers.SpeechHandler;
import com.senacor.devconfapp.models.Event;
import com.senacor.devconfapp.models.Speech;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.view.View.VISIBLE;
import static com.senacor.devconfapp.R.layout.event;
import static com.senacor.devconfapp.clients.AsynchRestClient.get;


public class EventActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, View.OnClickListener{

    TextView eventName;
    TextView eventPlace;
    TextView eventDate;
    ListView speechlist;
    MenuItem list_all_events;
    Button joinButton;
    ImageButton addSpeechButton;
    private PopupWindow pw;
    private View popupView;
    private LayoutInflater inflater;
    private SpeechHandler speechHandler;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(event);
        String url = getIntent().getExtras().getString("url");
        System.out.println(url);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        getEvent(url);
        joinButton = (Button) findViewById(R.id.joinButton);
        joinButton.setOnClickListener(EventActivity.this);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        addSpeechButton = (ImageButton) findViewById(R.id.addSpeechButton);
        addSpeechButton.setOnClickListener(EventActivity.this);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_layout, null, false);
        if(sharedPref.getString("role", "role").equals("ADMIN")) {
            addSpeechButton.setVisibility(VISIBLE);
            speechHandler = new SpeechHandler();
        }
    }


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.list_all_events){
            Intent intent = new Intent(EventActivity.this, EventListActivity.class);
            EventActivity.this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       switch(item.getItemId()) {

            case R.id.list_all_events:

                Intent intent = new Intent(EventActivity.this, EventListActivity.class);
                EventActivity.this.startActivity(intent);
                break;

            case R.id.create_new_event:

                Intent intent2 = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(intent2);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

            return true;
    }


    //Opens dialog when Button "Join" get clicked to register/unregister for the conference
    @Override
    public void onClick(View v) {

        if (v == joinButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
            builder.setTitle("Conference Registration");
            if(joinButton.getText().equals("Join")){
                builder.setMessage("You successfully registered for the conference.");
            }else{
                builder.setMessage("You successfully unregistered for the conference.");
            }
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String url = getIntent().getExtras().getString("url") + "/attendees/" + sharedPref.getString("userId", "userId");
                            System.out.println(url);
                            AsynchRestClient.put(getBaseContext(), url, null, new JsonHttpResponseHandler(){

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    System.out.println("putting attendance successful");
                                    String url = getIntent().getExtras().getString("url") + "/attendees/" + sharedPref.getString("userId", "userId");
                                    setAttendanceButton(url);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    Log.d("Failed: ", "" + statusCode);
                                    Log.d("Error : ", "" + throwable);
                                    System.out.println("putting attendance not successful");
                                }
                            });

                        }})
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    builder.create()
                            .show();
        }
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
        get(EventActivity.this, url,
                null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {

                        Event event = new Event(jsonObject);
                        System.out.println(event.getName());

                        eventName = (TextView)findViewById(R.id.event_name);
                        eventPlace = (TextView)findViewById(R.id.event_place);
                        eventDate = (TextView)findViewById(R.id.event_date);

                        eventName.setText(event.getName());
                        eventPlace.setText(event.getPlace());
                        eventDate.setText(event.getDate());


                        //TODO JsonObject in KLasse überführen - Hal-Object / Resource .getLink();
                        String speechesUrl = "";
                        String attendanceUrl = "";
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("links");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if(jsonArray.getJSONObject(i).getString("rel").equals("speeches")){
                                    speechesUrl = jsonArray.getJSONObject(i).getString("href");

                                }
                                if(jsonArray.getJSONObject(i).getString("rel").equals("attendance")){
                                    attendanceUrl = jsonArray.getJSONObject(i).getString("href");
                                    System.out.println("extracting attendance url:" + attendanceUrl);

                                };
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!(speechesUrl.equals(""))) {
                            getSpeeches(speechesUrl);
                        }
                        if (!(attendanceUrl.equals(""))) {
                            setAttendanceButton(attendanceUrl);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);                    }
                });
    }

    private void getSpeeches(String speechesUrl) {

        get(EventActivity.this, speechesUrl,
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
                });


    }

    private void setAttendanceButton(String attendanceUrl) {
        AsynchRestClient.get(EventActivity.this, attendanceUrl,
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response.getBoolean("isAttending"));
                            if (response.getBoolean("isAttending")) {
                                joinButton.setText("Joined");
                            }
                            else{
                                joinButton.setText("Join");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Failed: ", "" + statusCode);
                        Log.d("Error : ", "" + throwable);
                    }


                });
    }

}


