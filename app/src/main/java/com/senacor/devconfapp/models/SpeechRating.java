package com.senacor.devconfapp.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeechRating {

    private UUID speechRatingId;
    private LocalTime timestamp;
    private Integer rating;
    private String comment;
    private String url;

    public SpeechRating() {

    }

    public SpeechRating(JSONObject jsonObject) {
        try {
//            this.timestamp = LocalTime.parse(jsonObject.getString("timestamp"));
            this.rating = jsonObject.getInt("rating");
            this.comment = jsonObject.getString("comment");
            JSONArray array = jsonObject.getJSONArray("links");
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

    public UUID getSpeechRatingId() {
        return speechRatingId;
    }

    public void setSpeechRatingId(UUID speechRatingId) {
        this.speechRatingId = speechRatingId;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
