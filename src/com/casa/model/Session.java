package com.casa.model;

public class Session {
    private Subject subject;
    private Teacher teacher;
    private Room room;
    private int day;
    private int slot;
    private String section;
    private boolean isReallocated;
    private Teacher originalTeacher;

    public Session(Subject subject, Teacher teacher, Room room, int day, int slot, String section) {
        this.subject = subject;
        this.teacher = teacher;
        this.originalTeacher = teacher;
        this.room = room;
        this.day = day;
        this.slot = slot;
        this.section = section;
        this.isReallocated = false;
    }

    public Subject getSubject() {
        return subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Room getRoom() {
        return room;
    }

    public int getDay() {
        return day;
    }

    public int getSlot() {
        return slot;
    }

    public String getSection() {
        return section;
    }

    public boolean isReallocated() {
        return isReallocated;
    }

    public Teacher getOriginalTeacher() {
        return originalTeacher;
    }

    public void setTeacher(Teacher newTeacher) {
        this.teacher = newTeacher;
        this.isReallocated = true;
    }

    @Override
    public String toString() {
        return "Day: " + day +
               " Slot: " + slot +
               " | Section: " + section +
               " | Subject: " + subject.getName() +
               " | Teacher: " + teacher.getName() +
               " | Room: " + room.getRoomId() +
               (isReallocated ? " [REALLOCATED from " + originalTeacher.getName() + "]" : "");
    }
}
