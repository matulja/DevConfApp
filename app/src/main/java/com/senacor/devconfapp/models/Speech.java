package com.senacor.devconfapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Berlina on 14.11.16.
 */

public class Speech {

    private String speechId;
    private String speechTitle;
    private String startTime;
    private String endTime;
    private String speechRoom;
    private String speaker;
    private String speakerInfo;
    private String speechSummary;
   // private String eventId;

    public Speech(JSONObject jsonObject) {
        try {
           // this.speechId = jsonObject.getString("speechId");
            this.speechTitle = jsonObject.getString("speechTitle");
            this.startTime = jsonObject.getString("startTime");
            this.endTime = jsonObject.getString("endTime");
            this.speechRoom = jsonObject.getString("speechRoom");
            this.speaker = jsonObject.getString("speaker");
            this.speakerInfo = jsonObject.getString("speakerInfo");
            this.speechSummary = jsonObject.getString("speechSummary");
          //  this.eventId = jsonObject.getString("eventId");
        }catch (JSONException e){
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime=startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

   /* public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }*/
}
