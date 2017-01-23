package com.senacor.devconfapp.activities;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.handlers.LogInOutHandler;

import static com.senacor.devconfapp.R.id.username;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    LogInOutHandler logInOutHandler;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logInOutHandler = new LogInOutHandler(this);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //AppLogo
        getSupportActionBar().setLogo(R.mipmap.logow); //wrong logo diplayed
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        final EditText etUsername = (EditText) findViewById(username);
        final EditText etPassword = (EditText) findViewById(R.id.password);
        final Button bSignIn = (Button) findViewById(R.id.sign_in_button);


        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                final String username = etUsername.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                if (!username.isEmpty() && !password.isEmpty()) {
                    params.put("username", username);
                    params.put("password", password);
                }

                logInOutHandler.login(params);
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

