package com.jntuk.engineers.Model;

public class status {
    String name, wish, image;

    public status() {
    }

    public status(String name, String wish) {
        this.name = name;
        this.wish = wish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }
}
