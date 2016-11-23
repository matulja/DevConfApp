package com.senacor.devconfapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.clients.RestClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    ProgressDialog prgDialog;
    private RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.username);
        final EditText etPassword = (EditText) findViewById(R.id.password);
        final Button bSignIn = (Button) findViewById(R.id.sign_in_button);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

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
      //  List<Header> headers = new ArrayList<>();
        // headers.add(new BasicHeader("Accept", "application/json"));
        params = new RequestParams();
        if(!username.isEmpty() && !password.isEmpty()){
            params.put("username", username);
            params.put("password", password);
        }

        prgDialog.show();
        RestClient.post(this, IPAddress.IP + "/login", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                System.out.println("on success");
                prgDialog.hide();
                if(statusCode == 200){
                    System.out.println("status = 200");
                    Intent intent = new Intent(LoginActivity.this, EventActivity.class);
                    intent.putExtra("username", username);
                    LoginActivity.this.startActivity(intent);
                }
                if(statusCode == 401) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Login failed.")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialog.hide();
                System.out.println(statusCode + " ");
                System.out.println(errorResponse.toString() + " = jsonObject");
                System.out.println(throwable.toString());
                System.out.println("Unexpected Error");
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

