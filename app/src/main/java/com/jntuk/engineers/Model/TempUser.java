package com.jntuk.engineers.Model;

public class TempUser {
    private String uId, email, name, image;

    public TempUser() {
    }

    public TempUser(String uId, String email, String name, String image) {
        this.uId = uId;
        this.email = email;
        this.name = name;
        this.image = image;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
