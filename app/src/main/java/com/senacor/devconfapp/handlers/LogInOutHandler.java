package com.senacor.devconfapp.handlers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.activities.LoginActivity;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.clients.AuthRestClient;
import com.senacor.devconfapp.models.Token;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by saba on 03.01.17.
 */

public class LogInOutHandler {

    Activity activity;
    ProgressDialog prgDialog;
    SharedPreferences sharedPref;
    EventHandler eventHandler;


    public LogInOutHandler(Activity activity) {
        this.activity = activity;
    }

    public void login(RequestParams params) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        eventHandler = new EventHandler(activity);

        prgDialog = new ProgressDialog(activity); //Please wait view
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(true);

        prgDialog.show();

        AuthRestClient.post(activity, IPAddress.IPuser + "/auth", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {

                System.out.println("post login on success");
                prgDialog.hide();
                if (statusCode == 200) {
                    Token token = new Token(jsonObject);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("tokenId", token.getTokenId());
                    editor.putString("role", token.getRole());
                    editor.putString("userId", token.getUserId());
                    editor.commit();
                    eventHandler.getCurrentEvent();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialog.hide();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Login failed.")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialog.hide();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Login failed.")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        });
    }




    public void logout(RequestParams params) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);


        AsynchRestClient.post(this.activity, IPAddress.IPuser + "/logout", params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();

                Intent intent3 = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent3);

                CharSequence text = "You logged out successfully. Goodbye! ";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText((Context)activity, text, duration);
                toast.show();

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                CharSequence text = "Logout failed. Please try again.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText((Context)activity, text, duration);
                toast.show();
                activity.finish();
            }
        });

        }
}
