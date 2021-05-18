package com.jntuk.engineers.Model;

public class Material {
    private String title, uri, time, date, subject, submitter,submitter_roll;

    public Material() {

    }

    public Material(String title, String uri, String time, String date, String subject, String submitter, String submitter_roll) {
        this.title = title;
        this.uri = uri;
        this.time = time;
        this.date = date;
        this.subject = subject;
        this.submitter = submitter;
        this.submitter_roll = submitter_roll;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getSubmitter_roll() {
        return submitter_roll;
    }

    public void setSubmitter_roll(String submitter_roll) {
        this.submitter_roll = submitter_roll;
    }
}
