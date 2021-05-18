package com.jntuk.engineers.Model;

public class AllServices {
    private String service, link;

    public AllServices() {
    }

    public AllServices(String service, String link) {
        this.service = service;
        this.link = link;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
