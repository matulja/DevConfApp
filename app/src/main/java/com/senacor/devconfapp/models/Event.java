package com.senacor.devconfapp.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private String date;


    public Event(JSONObject object) {
        try {
            //this.eventId = (String) object.get("eventId");
            this.name = object.getString("name");
            this.place = object.getString("place");
            this.date = object.getString("date");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Event(String name, String place) {
        this.name = name;
        this.place = place;
    }

    public Event(String name) {
        this.name = name;
    }

    @JsonCreator
    public Event() {

    }
    //Setter


    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate(String date) {
        this.date = date.toString();
    }

    public String getName() {
        return name;
    }

    public String getPlace() {

        return place;
    }

    public String getDate() {
        return date;
    }

    public String getEventId() {
    return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}