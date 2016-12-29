package com.senacor.devconfapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.senacor.devconfapp.R;
import com.senacor.devconfapp.models.Event;

import java.util.ArrayList;
//import org.joda.time.LocalDate;

/**
 * Created by saba on 28.10.16.
 */

public class EventListAdapter extends ArrayAdapter<Event>  {

    private static class ViewHolder {
        TextView eventId;
        TextView name;
        TextView place;
        TextView date;
    }

    public EventListAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.item_event, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_event, parent, false);

            viewHolder.eventId = (TextView) convertView.findViewById(R.id.value_event_eventId);
            viewHolder.name = (TextView) convertView.findViewById(R.id.value_event_name);
            viewHolder.place = (TextView) convertView.findViewById(R.id.value_event_place);
            viewHolder.date = (TextView) convertView.findViewById(R.id.value_event_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(event.getName());
        viewHolder.place.setText(event.getPlace());
        viewHolder.date.setText(event.dateToString());
        return convertView;
    }
}
