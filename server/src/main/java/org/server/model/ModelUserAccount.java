package org.server.model;


public class ModelUserAccount {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public ModelUserAccount(int id, String username, String gender, String image, boolean status) {
        this.id = id;
        this.username = username;
        this.gender = gender;
        this.image = image;
        this.status = status;
    }

    public ModelUserAccount() {
    }

    private int id;
    private String username;
    private String gender;
    private String image;
    private boolean status;

}
