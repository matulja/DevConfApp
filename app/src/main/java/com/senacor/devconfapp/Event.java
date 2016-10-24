package com.senacor.devconfapp;

/**
 * Created by Marynasuprun on 24.10.2016.
 */

public class Event {

    private String id;

    private String place;

    private LocalDate date;

    private String name;

    public Event(String name){
        this.name=name;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {

        return place;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getId() {

        return id;
    }
}
