package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.fragments.EventRatingDialog;
import com.senacor.devconfapp.models.EventRating;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.view.View.VISIBLE;

/**
 * Created by saba on 30.01.17.
 */

public class EventRatingHandler {
    TextView rateSpeech;
    Activity activity;
    EventHandler eventHandler;

    public EventRatingHandler(Activity activity) {
        this.activity = activity;
    }

    public void getEventRating(final String eventId, final String userId) {
        Log.i("location", "in getEventRating()");
        Log.i("url", IPAddress.IPrating + "/events/" + eventId + "/" + userId);
        rateSpeech = (TextView) activity.findViewById(R.id.rateEvent);
        rateSpeech.setVisibility(VISIBLE);

        AsynchRestClient.get(activity, IPAddress.IPrating + "/events/" + eventId + "/" + userId, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("success", "getting eventrating was successful");
                final EventRating eventRating;
                Log.i("jsonObject", response.toString());
                final boolean ratingExists = response.length() > 0;
                if(ratingExists){
                    rateSpeech.setText("View/Edit your rating here...");
                    eventRating = new EventRating(response);
                }else{
                    eventRating = new EventRating();
                    eventRating.setCaterRating(0);
                    eventRating.setContentRating(0);
                    eventRating.setInformationRating(0);
                    eventRating.setLocationRating(0);
                    eventRating.setSuggestions("");
                    eventRating.setUrl(IPAddress.IPrating + "/events/" + eventId + "/" + userId + "/add");
                }
                rateSpeech.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        DialogFragment eventRatingDialog = EventRatingDialog.newInstance(eventRating, ratingExists);
                        eventRatingDialog.show(activity.getFragmentManager(), "EventRatingDialog");

                    }
                });


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("error", "Could not get eventrating");
            }
        });
    }

    public void putEventRating(String url, RequestParams params) {

        AsynchRestClient.put(activity, url + "/edit", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("information", "putting eventRating was successful");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("error", "putting eventRating was not successful");
                Log.e("throwable", throwable.toString());
                Log.e("jsonErrorResponse", errorResponse.toString());

            }
        });
    }

    public void postEventRating(String url, RequestParams params) {
        AsynchRestClient.post(activity, url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("information", "posting eventRating was successful");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("error", "posting eventRating was not successful");
                Log.e("throwable", throwable.toString());
                Log.e("jsonErrorResponse", errorResponse.toString());

            }
        });
    }
}
