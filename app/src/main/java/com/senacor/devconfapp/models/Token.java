package com.senacor.devconfapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saba on 30.11.16.
 */

public class Token {

    private String tokenId;
    private String userId;
    private String role;

    public Token(JSONObject jsonObject) {
        try {
            this.tokenId = jsonObject.getString("tokenId");
            this.userId = jsonObject.getString("userId");
            this.role = jsonObject.getString("role");

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }




}
