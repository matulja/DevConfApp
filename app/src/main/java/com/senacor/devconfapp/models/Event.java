package com.senacor.devconfapp.models;


import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Event {

    private String id;
    private String name;
    private String place;
    private String date;

    //private LocalDate date;

    public Event(JSONObject object) {
        try {
            this.id = object.getString("id");
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate(Date myDate)

    {
        this.date=new SimpleDateFormat("MM-dd-yyyy").format(myDate);
    }

    public String getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {

        return place;
    }
    public String getDate(){
        return date;
    }

//public LocalDate getDate() {
//  return date;
//}
}