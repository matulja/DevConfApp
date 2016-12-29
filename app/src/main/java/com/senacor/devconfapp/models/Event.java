package com.senacor.devconfapp.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;


public class Event{

    //@JsonProperty
    private String eventId;
    @JsonProperty
    private String name;
    @JsonProperty
    private String place;
    @JsonProperty
    private LocalDate date;


    public Event(JSONObject object) {
        try {
            //this.eventId = (String) object.get("eventId");
            this.name = object.getString("name");
            this.place = object.getString("place");
            this.date = LocalDate.parse(object.getString("date"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Event() {

    }
    //Setter & Getter


    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {

        return place;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getEventId() {
    return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String dateToString() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy");
        return getDate().toString( fmt );
    }

}