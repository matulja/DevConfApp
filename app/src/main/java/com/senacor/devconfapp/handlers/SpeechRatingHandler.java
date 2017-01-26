package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.activities.EventActivity;
import com.senacor.devconfapp.adapters.SpeechAdapter;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.models.Speech;
import com.senacor.devconfapp.models.SpeechRating;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saba on 16.01.17.
 */

public class SpeechRatingHandler {

    Activity activity;
    SpeechAdapter speechAdapter;
    SpeechHandler speechHandler;
    ArrayList<Speech> speeches;
    SharedPreferences sharedPref;


    public SpeechRatingHandler(Activity activity) {
        this.activity = activity;
        this.speeches = new ArrayList<>();
        this.speechAdapter = new SpeechAdapter((AppCompatActivity) activity, speeches);
        this.speechHandler = new SpeechHandler(activity);
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

    }

    public void getSpeechRating(String userId, final Speech speech) {

        AsynchRestClient.get(activity, IPAddress.IPrating + "/speeches/" + speech.getSpeechId() + "/" + userId,
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        boolean isInFuture = sharedPref.getBoolean("isInFuture", true);
                        if (!isInFuture) {
                            System.out.println(jsonObject.toString());
                            if (jsonObject.length() != 0) {
                                SpeechRating speechRating = new SpeechRating(jsonObject);
                                speech.setSpeechRating(speechRating);
                            }
                        }
                        boolean wasAdded = false;
                        for (int i = 0; i < speeches.size(); i++) {
                            if (speeches.get(i).getStartTime().isAfter(speech.getStartTime())) {
                                speeches.add(i, speech);

                                wasAdded = true;
                                break;
                            }
                        }
                        if (!wasAdded) {
                            speeches.add(speech);
                        }
                        ListView speechlist = (ListView) activity.findViewById(R.id.list_speeches);
                        speechlist.setAdapter(speechAdapter);
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        System.out.println("speechRating not received");
                        SpeechRating speechRating = new SpeechRating(errorResponse);
                        System.out.println(speechRating.getRating());
                        System.out.println(throwable);
                    }
                });

    }

    public void putSpeechRating(final String url, RequestParams params) {


        AsynchRestClient.put(activity, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.i("Information", "in speechhandler edit speech method");
                Log.i("Information", "speeches were successfully edited");
                speechHandler.getSpeeches(EventActivity.URL + "/speeches");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("info", "status code = " + statusCode);
                Log.i("info", "throwable = " + throwable.toString());
                Log.i("info", "json error response = " + errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
               // errorWithoutJson(statusCode);
            }

        });
    }

    public void postSpeechRating(final String url, RequestParams params) {
        AsynchRestClient.post(activity, url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.i("Information", "in speechhandler edit speech method");
                Log.i("Information", "speeches were successfully edited");
                speechHandler.getSpeeches(EventActivity.URL + "/speeches");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("info", "status code = " + statusCode);
                Log.i("info", "throwable = " + throwable.toString());
                Log.i("info", "json error response = " + errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                // errorWithoutJson(statusCode);
            }

        });
    }
}
