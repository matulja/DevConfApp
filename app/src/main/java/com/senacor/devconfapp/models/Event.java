package com.senacor.devconfapp.models;


import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

public class Event {

    private String eventId;
    private String name;
    private String place;
    private String date;


    //event.setDate(new LocalDate(2016, 9, i));

    public Event(JSONObject object) {
        try {
            this.eventId = object.getString("eventId");
            this.name = object.getString("name");
            this.place = object.getString("place");
            this.date = object.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Event(String eventId, String name, String place) {
        this.eventId = eventId;
        this.name = name;
        this.place = place;
    }

    public Event(String name) {
        this.name = name;
    }

    //Setter

    public void setId(String eventId) {
        this.eventId = eventId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate (String date) {
        this.date = date.toString();
    }

    //public void setDate(LocalDate date) {
    //    this.date = LocalDate.parse(date.toString());
   // }

    // Getter

    public String getId() {

        return eventId;
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
   // public LocalDate getDate() {
    //    return LocalDate.parse(date.toString());
    //}

}