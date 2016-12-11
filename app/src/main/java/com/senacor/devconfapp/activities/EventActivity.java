package com.senacor.devconfapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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
import com.senacor.devconfapp.clients.RestClient;
import com.senacor.devconfapp.handlers.SpeechHandler;
import com.senacor.devconfapp.models.Event;
import com.senacor.devconfapp.models.Speech;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.view.View.VISIBLE;
import static android.widget.ListPopupWindow.MATCH_PARENT;
import static com.senacor.devconfapp.R.layout.event;


public class EventActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, View.OnClickListener{

    TextView eventName;
    TextView eventPlace;
    TextView eventDate;
    ListView speechlist;
    MenuItem list_all_events;
    SharedPreferences sharedPref;
    Button joinButton;
    ImageButton addSpeechButton;
    private PopupWindow pw;
    private View popupView;
    private LayoutInflater inflater;
    private SpeechHandler speechHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(event);
        String url = getIntent().getExtras().getString("url");
        System.out.println(url);
        getEvent(url);
        joinButton = (Button) findViewById(R.id.joinButton);
        joinButton.setOnClickListener(EventActivity.this);
        addSpeechButton = (ImageButton) findViewById(R.id.addSpeechButton);
        addSpeechButton.setOnClickListener(EventActivity.this);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_layout, null, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPref.getString("role", "role").equals("ADMIN")) {
            addSpeechButton.setVisibility(VISIBLE);
            speechHandler = new SpeechHandler();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.list_all_events){
            Intent intent = new Intent(EventActivity.this, EventListActivity.class);
            EventActivity.this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Opens dialog when Button "Join" get clicked to register/unregister for the conference
    @Override
    public void onClick(View v) {
        if (v == joinButton)    {
            if (joinButton.getText().equals("Join")){
            AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
            builder.setMessage("You successfully registered for the conference.")
                    .setTitle("Conference Registration")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            //ToDo: User soll zum Event gespeichert werden
                            joinButton.setText("Joined");
                        }
                        })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    })
                    .create()
                    .show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
                builder.setMessage("You successfully unregistered for the conference.")
                        .setTitle("Conference Registration")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                //ToDo: User soll wieder vom Event abgemeldet werden
                                joinButton.setText("Join");
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        })
                        .create()
                        .show();
            }
        } if (v == addSpeechButton) {
            pw = new PopupWindow(this);
            pw.setWidth(MATCH_PARENT);
            pw.setHeight(MATCH_PARENT);
            pw.setOutsideTouchable(false);
            pw.setContentView(popupView);
            pw.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

            // Use any one method - showAtLocation or showAsDropDown to show the popup
            pw.showAtLocation(v, Gravity.CENTER, 0, 0);
           // speechHandler.addSpeech();
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
        RestClient.get(EventActivity.this, url,
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
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("links");
                            System.out.println(jsonArray.length() + " ");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if(jsonArray.getJSONObject(i).getString("rel").equals("speeches")){
                                    speechesUrl = jsonArray.getJSONObject(i).getString("href");
                                    System.out.println(speechesUrl);

                                }
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

        RestClient.get(EventActivity.this, speechesUrl,
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

}


