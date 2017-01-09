package com.senacor.devconfapp.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
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
    @JsonProperty
    private String streetAndNumber;
    @JsonProperty
    private String postalCodeAndCity;

    private String url;


    public Event(JSONObject object) {
        try {
            //this.eventId = (String) object.get("eventId");
            this.name = object.getString("name");
            this.place = object.getString("place");
            this.date = LocalDate.parse(object.getString("date"));
            this.streetAndNumber = object.getString("streetAndNumber");
            this.postalCodeAndCity = object.getString("postalCodeAndCity");

            JSONArray array = object.getJSONArray("links");
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    if (array.getJSONObject(i).getString("rel").equals("self")) {
                        this.url = array.getJSONObject(i).getString("href");

                    }
                }
            }

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
    public String getStreetAndNumber() {
        return streetAndNumber;
    }

    public void setStreetAndNumber(String streetAndNumber) {
        this.streetAndNumber = streetAndNumber;
    }

    public String getPostalCodeAndCity() {
        return postalCodeAndCity;
    }

    public void setPostalCodeAndCity(String postalCodeAndCity) {
        this.postalCodeAndCity = postalCodeAndCity;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String dateToString() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy");
        return getDate().toString( fmt );
    }

   public String extractAndSaveEventId() {
        String[] urlElements = this.url.split("/");
        this.eventId = urlElements[urlElements.length - 1];
        return this.eventId;
    }

}