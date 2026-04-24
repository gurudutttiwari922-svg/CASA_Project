package com.casa.model;

public class Room {

    private String roomId;      // CR1, LT2, DL1
    private String type;        // CR, TR, LT, LAB, SEMINAR, AUDITORIUM
    private String subType;     // Only for labs: Digital, Physics, etc.
    private int capacity;

    public Room(String roomId, String type, String subType, int capacity) {
        this.roomId = roomId;
        this.type = type;
        this.subType = subType;
        this.capacity = capacity;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public int getCapacity() {
        return capacity;
    }
}