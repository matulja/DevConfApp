package com.senacor.devconfapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.senacor.devconfapp.R;
import com.senacor.devconfapp.activities.EventActivity;
import com.senacor.devconfapp.clients.AsynchRestClient;
import com.senacor.devconfapp.models.Speech;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Veronika Babic on 14.11.2016.
 */

public class SpeechAdapter extends ArrayAdapter<Speech>{
    public static class ViewHolder{
        TextView speechId;
        TextView speechTitle;
        TextView startTime;
        TextView endTime;
        TextView speechRoom;
        TextView speaker;
        TextView speakerInfo;
        TextView speechSummary;
        ImageView deleteButton;
    }

    public SpeechAdapter(Context context, ArrayList<Speech> speeches) {
        super(context, R.layout.item_speech, speeches);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Speech speech = getItem(position);
        SpeechAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new SpeechAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_speech, parent, false);

            viewHolder.speechTitle= (TextView) convertView.findViewById(R.id.speechTitle);
            viewHolder.speechRoom = (TextView) convertView.findViewById(R.id.speechRoom);
            viewHolder.speaker = (TextView) convertView.findViewById(R.id.speakerName);
            viewHolder.speakerInfo = (TextView) convertView.findViewById(R.id.speakerInfo);
            viewHolder.speechSummary = (TextView) convertView.findViewById(R.id.speechSummary);
            viewHolder.startTime = (TextView) convertView.findViewById(R.id.value_startTime);
            viewHolder.endTime = (TextView) convertView.findViewById(R.id.value_endTime);
            viewHolder.deleteButton = (ImageView) convertView.findViewById(R.id.button_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SpeechAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.speechTitle.setText(speech.getSpeechTitle());
        viewHolder.speechRoom.setText(speech.getSpeechRoom());
        viewHolder.speaker.setText(speech.getSpeaker());
        viewHolder.speakerInfo.setText(speech.getSpeakerInfo());
        viewHolder.speechSummary.setText(speech.getSpeechSummary());
        viewHolder.startTime.setText(speech.getStartTime());
        viewHolder.endTime.setText(speech.getEndTime());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String role = sharedPref.getString("role", "role");
        if (role.equals("ADMIN")) {
            viewHolder.deleteButton.setVisibility(View.VISIBLE);
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    deleteSpeech(speech.getUrl());
                }
            });

        }

        return convertView;
    }

    private void deleteSpeech(final String url) {

        AsynchRestClient.delete(getContext(), url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("in delete speech");
                Intent intent = new Intent(getContext(), EventActivity.class);
                String[] parts = url.split("/");
                String newUrl="";
                for (int i = 0; i < 5; i++) {
                    newUrl += parts[i] + "/";
                }
                intent.putExtra("url", newUrl);
                getContext().startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("in delete speech: " + throwable);
            }
        });
        }
}

