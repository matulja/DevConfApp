package com.senacor.devconfapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Berlina on 14.11.16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Speech {

    private String speechId;
    private String speechTitle;
    private LocalTime startTime;
    private LocalTime endTime;
    private String speechRoom;
    private String speaker;
    private String speakerInfo;
    private String speechSummary;
    private String eventId;
    private SpeechRating speechRating;

    private String url;

    public Speech(JSONObject jsonObject) {
        try {
            // this.speechId = jsonObject.getString("speechId");
            this.speechTitle = jsonObject.getString("speechTitle");
            String start = jsonObject.getString("startTime");

            this.startTime = LocalTime.parse(jsonObject.getString("startTime"));
            this.endTime = LocalTime.parse(jsonObject.getString("endTime"));
            this.speechRoom = jsonObject.getString("speechRoom");
            this.speaker = jsonObject.getString("speaker");
            this.speakerInfo = jsonObject.getString("speakerInfo");
            this.speechSummary = jsonObject.getString("speechSummary");
//            this.eventId = jsonObject.getString("eventId");
            JSONArray array = jsonObject.getJSONArray("links");
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    if (array.getJSONObject(i).getString("rel").equals("self")) {
                        this.url = array.getJSONObject(i).getString("href");
                        System.out.println(url);

                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Speech() {

    }

    public String getSpeechId() {
        return speechId;
    }

    public void setSpeechId(String speechId) {
        this.speechId = speechId;
    }

    public String getSpeechTitle() {
        return speechTitle;
    }

    public void setSpeechTitle(String speechTitle) {
        this.speechTitle = speechTitle;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getSpeechRoom() {
        return speechRoom;
    }

    public void setSpeechRoom(String speechRoom) {
        this.speechRoom = speechRoom;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getSpeakerInfo() {
        return speakerInfo;
    }

    public void setSpeakerInfo(String speakerInfo) {
        this.speakerInfo = speakerInfo;
    }

    public String getSpeechSummary() {
        return speechSummary;
    }

    public void setSpeechSummary(String speechSummary) {
        this.speechSummary = speechSummary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String timeToString(LocalTime time) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        return time.toString(fmt);
    }

    public SpeechRating getSpeechRating() {
        return speechRating;
    }

    public void setSpeechRating(SpeechRating speechRating) {
        this.speechRating = speechRating;
    }

    public String extractAndSaveSpeechId() {
        String[] urlElements = this.url.split("/");
        this.speechId = urlElements[urlElements.length - 1];
        return this.speechId;
    }

}
