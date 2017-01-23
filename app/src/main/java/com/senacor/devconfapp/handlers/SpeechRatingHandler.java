package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
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
    ArrayList<Speech> speeches;


    public SpeechRatingHandler(Activity activity) {
        this.activity = activity;
        this.speeches = new ArrayList<>();
        this.speechAdapter = new SpeechAdapter((AppCompatActivity) activity, speeches);

    }

    public void getSpeechRating(String userId, final Speech speech) {
        AsynchRestClient.get(activity, IPAddress.IPrating + "/" + userId + "/" + speech.getSpeechId(),
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        SpeechRating speechRating = new SpeechRating(jsonObject);
                        speech.setSpeechRating(speechRating);
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
}
