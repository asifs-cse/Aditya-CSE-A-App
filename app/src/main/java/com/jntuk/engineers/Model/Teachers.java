package com.jntuk.engineers.Model;

public class Teachers {
    private String name, image, phone, email, rank, sub, degree;

    public Teachers() {
    }

    public Teachers(String name, String image, String phone, String email, String rank, String sub, String degree) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.email = email;
        this.rank = rank;
        this.sub = sub;
        this.degree = degree;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
