package com.senacor.devconfapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.clients.AuthRestClient;
import com.senacor.devconfapp.handlers.EventHandler;
import com.senacor.devconfapp.models.Token;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    ProgressDialog prgDialog;
    private RequestParams params;
    private SharedPreferences sharedPref;
    EventHandler eventHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        eventHandler = new EventHandler(this);

        final EditText etUsername = (EditText) findViewById(R.id.username);
        final EditText etPassword = (EditText) findViewById(R.id.password);
        final Button bSignIn = (Button) findViewById(R.id.sign_in_button);
        prgDialog = new ProgressDialog(this); //Please wait view
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(true);

        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                login(username, password);
            }
        });


    }


    private void login(final String username, String password) {
        params = new RequestParams();
        if (!username.isEmpty() && !password.isEmpty()) {
            params.put("username", username);
            params.put("password", password);
        }

        prgDialog.show();

        AuthRestClient.post(this, IPAddress.IPuser + "/auth", params, new JsonHttpResponseHandler() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Login failed.")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
                System.out.println(statusCode + " ");
                System.out.println(throwable.toString());
                System.out.println("Unexpected Error");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialog.hide();
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Login failed.")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}

