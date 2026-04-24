package com.casa.core;

import com.casa.model.Room;
import com.casa.model.Teacher;
import java.util.HashMap;
import java.util.Map;

public class AvailabilityService {

    private Map<Teacher, boolean[][]> teacherAvailability;
    private Map<Room, boolean[][]> roomAvailability;
    private Map<String, String> teacherStatus;
    private Map<String, String> absenceReason;
    private Map<String, Boolean> absenceApproved;

    public AvailabilityService() {
        teacherAvailability = new HashMap<>();
        roomAvailability = new HashMap<>();
        teacherStatus = new HashMap<>();
        absenceReason = new HashMap<>();
        absenceApproved = new HashMap<>();
    }

    public void addTeacher(Teacher teacher) {
        teacherAvailability.put(teacher, new boolean[5][8]);
        teacherStatus.put(teacher.getId(), "AVAILABLE");
        absenceApproved.put(teacher.getId(), false);
    }

    public void addRoom(Room r) {
        roomAvailability.put(r, new boolean[5][8]);   
    }

    public boolean isRoomAvailable(Room r, int day, int slot) {
        return !roomAvailability.get(r)[day][slot];
    }

    public void markRoomBusy(Room r, int day, int slot) {
        roomAvailability.get(r)[day][slot] = true;
    }

    public boolean isTeacherAvailable(Teacher teacher, int day, int slot) {
        return !teacherAvailability.get(teacher)[day][slot];
    }

    public void markTeacherBusy(Teacher teacher, int day, int slot) {
        teacherAvailability.get(teacher)[day][slot] = true;
    }

    public void markTeacherUnavailable(Teacher teacher, int day, int slot) {
        teacherAvailability.get(teacher)[day][slot] = true;
    }

    public void markAvailable(Teacher teacher) {
        teacherStatus.put(teacher.getId(), "AVAILABLE");
        absenceReason.remove(teacher.getId());
        absenceApproved.put(teacher.getId(), false);
    }

    public void markUnavailable(Teacher teacher, String reason) {
        teacherStatus.put(teacher.getId(), "UNAVAILABLE");
        absenceReason.put(teacher.getId(), reason);
        absenceApproved.put(teacher.getId(), false);
    }

    public void approveUnavailability(Teacher teacher) {
        teacherStatus.put(teacher.getId(), "UNAVAILABLE");
        absenceApproved.put(teacher.getId(), true);
    }

    public String getStatus(Teacher teacher) {
        return teacherStatus.getOrDefault(teacher.getId(), "AVAILABLE");
    }

    public String getReason(Teacher teacher) {
        return absenceReason.getOrDefault(teacher.getId(), "");
    }

    public boolean isAbsencePending(Teacher teacher) {
        return "UNAVAILABLE".equals(getStatus(teacher))
                && !absenceApproved.getOrDefault(teacher.getId(), false);
    }
}
