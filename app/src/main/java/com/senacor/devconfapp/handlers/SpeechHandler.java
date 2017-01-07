package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.activities.EventActivity;
import com.senacor.devconfapp.adapters.SpeechAdapter;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.models.Speech;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saba on 11.12.16.
 */

public class SpeechHandler {

    private Activity activity;
    private TextView noSpeeches;

    public SpeechHandler(Activity activity) {
        this.activity = activity;
    }

    public void getSpeeches(String speechesUrl) {

        AsynchRestClient.get(activity, speechesUrl, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                noSpeeches=(TextView) activity.findViewById(R.id.info_noSpeech);
                if(response.length()== 0){
                    noSpeeches.setVisibility(View.VISIBLE);
                    System.out.println("passt");
                }
                else {
                    noSpeeches.setVisibility(View.GONE);
                    ArrayList<Speech> speechArray = new ArrayList<>();
                    SpeechAdapter speechAdapter = new SpeechAdapter(activity, speechArray);

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Speech speech = new Speech(response.getJSONObject(i));
                            String id = response.getJSONObject(i).getString("speechId");
                            System.out.println(id);
                            speech.setSpeechId(id);
                            speechAdapter.add(speech);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ListView speechlist = (ListView) activity.findViewById(R.id.list_speeches);
                    speechlist.setAdapter(speechAdapter);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                errorWithoutJson(statusCode);
            }
        });
    }


    public void deleteSpeech(String url) {

        AsynchRestClient.delete(activity, url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Intent intent = new Intent(activity, EventActivity.class);
                intent.putExtra("url", EventActivity.URL);
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               errorWithoutJson(statusCode);
            }
        });
    }


    public void addSpeech(RequestParams params) {

        final String url = EventActivity.URL + "/speeches/createSpeech";

        AsynchRestClient.post(activity, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                getSpeeches(EventActivity.URL + "/speeches");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                errorWithJson(statusCode, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                errorWithoutJson(statusCode);
            }
        });


    }

    public void editSpeech(final String url, RequestParams params) {


        AsynchRestClient.put(activity, url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.i("Information", "in speechhandler edit speech method");
                Log.i("Information", "speeches were successfully edited");
                getSpeeches(EventActivity.URL + "/speeches");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                errorWithJson(statusCode, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                errorWithoutJson(statusCode);
            }

        });
    }

    private void errorWithoutJson(int statusCode) {
        switch (statusCode) {
            case 401:
                ErrorHandler.handleUnauthorizedError(activity);
                break;
            default:
                CharSequence text = "Sorry, your request could not be handled. Please try again.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText((Context) activity, text, duration);
                toast.show();
                Intent intent = new Intent(activity, EventActivity.class);
                intent.putExtra("url", EventActivity.URL);
                activity.startActivity(intent);
                break;
        }

    }

    private void errorWithJson(int statusCode, JSONObject errorResponse) {
        switch (statusCode) {
            case 401:
                ErrorHandler.handleUnauthorizedError(activity);
                break;
            case 409:
                Speech speech = new Speech(errorResponse);
                ErrorHandler.handleConflictError(speech, activity);
            default:
                CharSequence text = "Sorry, your request could not be handled. Please try again.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText((Context) activity, text, duration);
                toast.show();
                Intent intent = new Intent(activity, EventActivity.class);
                intent.putExtra("url", EventActivity.URL);
                activity.startActivity(intent);
                break;
        }
    }

}
