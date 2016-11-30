package com.senacor.devconfapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saba on 30.11.16.
 */

public class Token {

    private String tokenId;
    private String userId;
    private String [] roles;

    public Token(JSONObject jsonObject) {
        try {
            this.tokenId = jsonObject.getString("tokenId");
            this.userId = jsonObject.getString("userId");
            //TODO change roles to String array
            //this.roles = jsonObject.getJSONArray("roles").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }




}
