package com.senacor.devconfapp.models;

import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by saba on 30.01.17.
 */

public class EventRating {

    private UUID eventRatingId;
    private int caterRating;
    private int locationRating;
    private int informationRating;
    private int contentRating;
    private String suggestions;
    private LocalTime timestamp;
    private String url;

    public EventRating(JSONObject object) {
        try {
            //this.eventId = (String) object.get("eventId");
            this.caterRating = object.getInt("caterRating");
            this.locationRating = object.getInt("locationRating");
            this.informationRating = object.getInt("informationRating");
            this.contentRating = object.getInt("contentRating");
            this.suggestions = object.getString("suggestions");

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

    public EventRating() {

    }

    public UUID getEventRatingId() {
        return eventRatingId;
    }

    public void setEventRatingId(UUID eventRatingId) {
        this.eventRatingId = eventRatingId;
    }

    public int getCaterRating() {
        return caterRating;
    }

    public void setCaterRating(int caterRating) {
        this.caterRating = caterRating;
    }

    public int getLocationRating() {
        return locationRating;
    }

    public void setLocationRating(int locationRating) {
        this.locationRating = locationRating;
    }

    public int getInformationRating() {
        return informationRating;
    }

    public void setInformationRating(int informationRating) {
        this.informationRating = informationRating;
    }

    public int getContentRating() {
        return contentRating;
    }

    public void setContentRating(int contentRating) {
        this.contentRating = contentRating;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
