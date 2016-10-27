package com.senacor.devconfapp;

public class Event {

    private String id;
    private String name;
    private String place;

    //private LocalDate date;


    public Event(){

    }

    public Event(String name){
        this.name=name;
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

    //public void setDate(LocalDate date) {
      //  this.date = date;
    //}

    public String getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {

        return place;
    }

    //public LocalDate getDate() {
      //  return date;
    //}


}