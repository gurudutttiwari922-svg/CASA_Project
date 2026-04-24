package com.casa.model;

public class ReallocationLog {

    private String subject;
    private String section;
    private String originalTeacher;
    private String newTeacher;
    private int day;
    private int slot;
    private String reason;

    public ReallocationLog(String subject, String section,
                           String originalTeacher, String newTeacher,
                           int day, int slot, String reason) {

        this.subject = subject;
        this.section = section;
        this.originalTeacher = originalTeacher;
        this.newTeacher = newTeacher;
        this.day = day;
        this.slot = slot;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "[REALLOCATION] Day " + day + " Slot " + slot +
                " | Section: " + section +
                " | Subject: " + subject +
                "\nOriginal: " + originalTeacher +
                "\nNew: " + newTeacher +
                "\nReason: " + reason + "\n";
    }
}
