package com.senacor.devconfapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Marynasuprun on 28.10.2016.
 */

public class EventAdapter extends ArrayAdapter<Event>

    {
        private static class ViewHolder {
            TextView id;
            TextView name;
        }

        public EventAdapter(Context context, ArrayList<Event> events) {
            super(context, R.layout.list_data, events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Event event = getItem(position);
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_data, parent, false);

                viewHolder.id = (TextView) convertView.findViewById(R.id.id_value);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name_value);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.id.setText(event.getId());
            viewHolder.name.setText(event.getName());

            return convertView;
        }
    }

