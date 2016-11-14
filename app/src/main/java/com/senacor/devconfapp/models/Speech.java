package com.senacor.devconfapp.models;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Berlina on 14.11.16.
 */

public class Speech {

    private String speechId;
    private String speechTitle;
    //private LocalTime startTime;
    //private LocalTime endTime;
    private String speechRoom;
    private String speaker;
    private String speakerInfo;
    private String speechSummary;
    private String eventId;

    public Speech(JSONObject object) {
        try {
            this.speechId = object.getString("speechId");
            this.speechTitle = object.getString("speechTitle");
            this.speechRoom = object.getString("speechRoom");
            this.speaker = object.getString("speaker");
            this.speakerInfo = object.getString("speakerInfo");
            this.speechSummary = object.getString("speechSummary");
            this.eventId = object.getString("eventId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
