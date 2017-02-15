package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.activities.EventActivity;
import com.senacor.devconfapp.adapters.SpeechAdapter;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.fragments.SpeechRatingDialog;
import com.senacor.devconfapp.models.Speech;
import com.senacor.devconfapp.models.SpeechRating;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saba on 16.01.17.
 */

public class SpeechRatingHandler {

    Activity activity;
    SpeechHandler speechHandler;
    SpeechAdapter.ViewHolder viewHolder;



    public SpeechRatingHandler(Activity activity) {
        this.activity = activity;
        this.speechHandler = new SpeechHandler(activity);
    }

    public void getSpeechRating(final String userId, final Speech speech, final SpeechAdapter.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;

        AsynchRestClient.get(activity, IPAddress.IPrating + "/speeches/" + speech.getSpeechId() + "/" + userId,
                null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        final SpeechRating speechRating;
                        final boolean ratingExists;
                        if (jsonObject.length() != 0) {
                            ratingExists = true;
                            speechRating = new SpeechRating(jsonObject);
                            viewHolder.getSubmitButton().setText("Edit");
                            viewHolder.getRateNow().setText("Your rating: ");
                        } else {
                            ratingExists = false;
                            speechRating = new SpeechRating();
                            speechRating.setRating(0);
                            speechRating.setSpeechId(speech.getSpeechId());
                            speechRating.setUserId(userId);
                            viewHolder.getSubmitButton().setText("Rate");
                            viewHolder.getRateNow().setText("Rate now: ");
                        }
                        viewHolder.getRatingBar().setRating(speechRating.getRating());
                        viewHolder.getSubmitButton().setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                DialogFragment speechRatingFragment = SpeechRatingDialog.newInstance(speechRating, ratingExists);
                                speechRatingFragment.show(activity.getFragmentManager(), "SpeechRatingDialog");
                            }
                        });
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        System.out.println("speechRating not received");
                        System.out.println(errorResponse.toString());
                        System.out.println(throwable.toString());
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
        AsynchRestClient.post(activity, url, params, new JsonHttpResponseHandler() {
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
