package com.senacor.devconfapp.models;

import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;


/**
 * Created by Marynasuprun on 10.01.2017.
 */
public class SpeechRating{


    UUID speechRatingId;
    private Speech speech;
    private LocalTime timestamp;
    private Integer rating;
    //private String comment;

    public SpeechRating(JSONObject jsonObject) {

        try {
            this.speechRatingId = UUID.fromString(jsonObject.getString("speechRatingId"));
            this.speech = new Speech(jsonObject.getJSONObject("speech"));
            this.timestamp = LocalTime.parse(jsonObject.getString("timestamp"));
            this.rating = jsonObject.getInt("rating");
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

    public Speech getSpeech() {
        return speech;
    }

    public void setSpeech(Speech speech) {
        this.speech = speech;
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

   /* public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }*/
}
