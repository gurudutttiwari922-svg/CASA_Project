package com.casa.model;

public class Subject {

    private String name;
    private int hoursPerWeek;
    private int priorityScore;
    private String requiredRoomType;
    private String requiredLabType;

    public Subject(String name, int hoursPerWeek, String requiredRoomType, String requiredLabType) {
        this.name = name;
        this.hoursPerWeek = hoursPerWeek;
        this.requiredRoomType = requiredRoomType;
        this.requiredLabType = requiredLabType;
    }

    public String getName() {
        return name;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public String getRequiredRoomType() {
     return requiredRoomType;
    }

    public String getRequiredLabType() {
     return requiredLabType;
    }
}