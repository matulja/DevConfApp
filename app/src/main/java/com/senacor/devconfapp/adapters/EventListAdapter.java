package com.senacor.devconfapp.adapters;


import android.app.Activity;
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

import com.senacor.devconfapp.R;
import com.senacor.devconfapp.activities.CreateEventActivity;
import com.senacor.devconfapp.handlers.EventHandler;
import com.senacor.devconfapp.models.Event;

import java.util.ArrayList;
//import org.joda.time.LocalDate;

/**
 * Created by saba on 28.10.16.
 */

public class EventListAdapter extends ArrayAdapter<Event>  {

    EventHandler eventHandler;

    private static class ViewHolder {
        //TextView eventId;
        TextView name;
        TextView place;
        TextView date;
        ImageView editEventButton;
        ImageView deleteEventButton;
    }

    public EventListAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.item_event, events);
        eventHandler = new EventHandler((Activity) context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Event event = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_event, parent, false);

            //viewHolder.eventId = (TextView) convertView.findViewById(R.id.value_event_eventId);
            viewHolder.name = (TextView) convertView.findViewById(R.id.value_event_name);
            viewHolder.place = (TextView) convertView.findViewById(R.id.value_event_place);
            viewHolder.date = (TextView) convertView.findViewById(R.id.value_event_date);
            viewHolder.editEventButton = (ImageView) convertView.findViewById(R.id.button_editEvent);
            viewHolder.deleteEventButton = (ImageView) convertView.findViewById(R.id.button_deleteEvent);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //viewHolder.eventId.setText(event.getEventId());
        viewHolder.name.setText(event.getName());
        viewHolder.place.setText(event.getPlace());
        viewHolder.date.setText(event.dateToString());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String role = sharedPref.getString("role", "role");
        if (role.equals("ADMIN")){
            viewHolder.deleteEventButton.setVisibility(View.VISIBLE);
            viewHolder.deleteEventButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                System.out.println("Delete Event " + event.getUrl());
                eventHandler.deleteEvent(event.getUrl());
            }
        });

        viewHolder.editEventButton.setVisibility(View.VISIBLE);
        viewHolder.editEventButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Activity activity = (Activity)getContext();
                Intent intent = new Intent(activity, CreateEventActivity.class);
                intent.putExtra("name", event.getName());
                intent.putExtra("place", event.getPlace());
                intent.putExtra("date", event.getDate().toString());
                intent.putExtra("eventId", event.getEventId());
                activity.startActivity(intent);
            }
        });

        }
        return convertView;
    }
}
