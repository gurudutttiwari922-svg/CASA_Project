package com.casa.model;

import java.util.List;

public class Teacher {

    private String id;
    private String name;
    private List<String> subjects;
    private int maxClassesPerDay;
    private int currentLoad;

    public Teacher(String id, String name, List<String> subjects, int maxClassesPerDay) {
        this.id = id;
        this.name = name;
        this.subjects = subjects;
        this.maxClassesPerDay = maxClassesPerDay;
        this.currentLoad = 0;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void incrementLoad() {
        currentLoad++;
    }
}
