package com.jntuk.engineers.Model;

public class UserTitle {
    private String uId,full_name,roll,department,semester,institute,user_image,rank;

    public UserTitle() {
    }

    public UserTitle(String uId, String full_name, String roll, String department, String semester, String institute, String user_image, String rank) {
        this.uId = uId;
        this.full_name = full_name;
        this.roll = roll;
        this.department = department;
        this.semester = semester;
        this.institute = institute;
        this.user_image = user_image;
        this.rank = rank;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
