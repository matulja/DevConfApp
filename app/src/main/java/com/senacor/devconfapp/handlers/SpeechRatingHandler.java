package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.widget.RatingBar;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.adapters.SpeechAdapter;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.models.Speech;
import com.senacor.devconfapp.models.SpeechRating;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saba on 16.01.17.
 */

public class SpeechRatingHandler {

    Activity activity;
    RatingBar ratingBar;
    SpeechAdapter speechAdapter;
    

    public SpeechRatingHandler(Activity activity, SpeechAdapter speechAdapter) {
        this.activity = activity;
        this.speechAdapter = speechAdapter;
    }

    public void getSpeechRating(String userId, final Speech speech) {
        AsynchRestClient.get(activity, IPAddress.IPrating + "/" + userId + "/" + speech.getSpeechId(),
                null, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        SpeechRating speechRating = new SpeechRating(jsonObject);
                        speech.setSpeechRating(speechRating);
                        speechAdapter.add(speech);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        System.out.println("speechRating not received");
                        SpeechRating speechRating = new SpeechRating(errorResponse);
                        System.out.println(speechRating.getRating());
                        System.out.println(throwable);
                    }

                    //TODO on failure
                });
    }
}
