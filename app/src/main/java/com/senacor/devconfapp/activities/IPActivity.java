package com.senacor.devconfapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.senacor.devconfapp.IPAddress;
import com.senacor.devconfapp.R;

/**
 * A screen to enter server IP-addresses.
 */
public class IPActivity extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView eventIP, userIP, ratingIP;
    private Button saveIPs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        eventIP = (AutoCompleteTextView) findViewById(R.id.ip_event);
        userIP = (AutoCompleteTextView) findViewById(R.id.ip_user);
        ratingIP = (AutoCompleteTextView) findViewById(R.id.ip_rating);
        saveIPs = (Button) findViewById(R.id.set_ip_button);

        saveIPs.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String ipEvent = eventIP.getText().toString().trim();
                IPAddress.setIPevent(ipEvent);
                System.out.println(ipEvent);

                String ipUser = userIP.getText().toString().trim();
                IPAddress.setIPuser(ipUser);
                System.out.println(ipUser);

                String ipRating = ratingIP.getText().toString().trim();
                IPAddress.setIPrating(ipRating);
                System.out.println(ipRating);

                Intent intent = new Intent(IPActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}