package com.jntuk.engineers.Model;

public class RoutineClass {
    private String sub, teacher, room;

    public RoutineClass() {
    }

    public RoutineClass(String sub, String teacher, String room) {
        this.sub = sub;
        this.teacher = teacher;
        this.room = room;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
