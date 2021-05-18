package com.jntuk.engineers.Model;

import java.io.Serializable;

public class SignInUser implements Serializable {
    private String uId, first_name, last_name, full_name, roll, registration, phone, department, semester, institute, blood, email, time, date, user_image, birthday, gender, present_address, permanent_address, bio;
    public Boolean isAuth, isProfile, isInternet;

    public SignInUser()
    {
    }

    public SignInUser(String uId, String first_name, String last_name, String full_name, String roll, String registration, String phone, String department, String semester, String institute, String blood, String email, String time, String date, String user_image, String birthday, String gender, String present_address, String permanent_address, String bio) {
        this.uId = uId;
        this.first_name = first_name;
        this.last_name = last_name;
        this.full_name = full_name;
        this.roll = roll;
        this.registration = registration;
        this.phone = phone;
        this.department = department;
        this.semester = semester;
        this.institute = institute;
        this.blood = blood;
        this.email = email;
        this.time = time;
        this.date = date;
        this.user_image = user_image;
        this.birthday = birthday;
        this.gender = gender;
        this.present_address = present_address;
        this.permanent_address = permanent_address;
        this.bio = bio;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPresent_address() {
        return present_address;
    }

    public void setPresent_address(String present_address) {
        this.present_address = present_address;
    }

    public String getPermanent_address() {
        return permanent_address;
    }

    public void setPermanent_address(String permanent_address) {
        this.permanent_address = permanent_address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
