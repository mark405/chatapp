package org.chatapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelUserAccount {
    public ModelUserAccount(int userID, String username, String gender, String image, boolean status) {
        this.id = userID;
        this.username = username;
        this.gender = gender;
        this.image = image;
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setUserID(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    private int id;
    private String username;
    private String gender;
    private String image;
    private boolean status;


    public ModelUserAccount(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            id = obj.getInt("id");
            username = obj.getString("username");
            gender = obj.getString("gender");
            image = obj.getString("image");
            status = obj.getBoolean("status");
        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
    }

}
