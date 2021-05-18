package com.jntuk.engineers.Model;

public class SingleUser {
    private String first_name, last_name, full_name, phone, roll, uId, user_image;

    public SingleUser() {
    }

    public SingleUser(String first_name, String last_name, String full_name, String phone, String roll, String uId, String user_image) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.full_name = full_name;
        this.phone = phone;
        this.roll = roll;
        this.uId = uId;
        this.user_image = user_image;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
