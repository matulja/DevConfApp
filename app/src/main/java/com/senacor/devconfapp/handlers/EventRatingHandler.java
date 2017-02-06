package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.fragments.EventRatingDialog;
import com.senacor.devconfapp.models.EventRating;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.view.View.VISIBLE;

/**
 * Created by saba on 30.01.17.
 */

public class EventRatingHandler {
    TextView rateEvent;
    Activity activity;

    public EventRatingHandler(Activity activity) {
        this.activity = activity;
    }

    public void getEventRating(final String eventId, final String userId) {
        Log.i("location", "in getEventRating()");
        Log.i("url", IPAddress.IPrating + "/events/" + eventId + "/" + userId);
        rateEvent = (TextView) activity.findViewById(R.id.rateEvent);
        rateEvent.setVisibility(VISIBLE);

        AsynchRestClient.get(activity, IPAddress.IPrating + "/events/" + eventId + "/" + userId, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("success", "getting eventrating was successful");
                final EventRating eventRating;
                Log.i("jsonObject", response.toString());
                final boolean ratingExists = response.length() > 0;
                if(ratingExists){
                    rateEvent.setText("View/Edit your rating here...");
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
                rateEvent.setOnClickListener(new View.OnClickListener(){
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

    public void putEventRating(String url, final String eventId, final String userId, RequestParams params) {

        AsynchRestClient.put(activity, url + "/edit", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("information", "putting eventRating was successful");
                CharSequence text = "Your rating has been edited. Thanks for your feedback!";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText((Context) activity, text, duration);
                toast.show();
                getEventRating(eventId, userId);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("error", "putting eventRating was not successful");
                Log.e("throwable", throwable.toString());
                Log.e("jsonErrorResponse", errorResponse.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("error", throwable.toString());
            }
        });
    }

    public void postEventRating(String url, RequestParams params) {
        AsynchRestClient.post(activity, url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("information", "posting eventRating was successful");
                EventRating eventRating = new EventRating(response);
                CharSequence text = "Your rating has been submitted. Thanks for your feedback!";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText((Context) activity, text, duration);
                toast.show();
                getEventRating(eventRating.getEventId(), eventRating.getUserId());
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
