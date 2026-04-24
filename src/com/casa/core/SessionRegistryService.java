package com.casa.core;

import com.casa.model.Room;
import com.casa.model.Session;
import com.casa.model.Teacher;
import java.util.ArrayList;
import java.util.List;

public class SessionRegistryService {

    private List<Session> sessions;

    public SessionRegistryService() {
        sessions = new ArrayList<>();
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public Session getSessionByTeacher(Teacher teacher, int day, int slot) {
        for (Session s : sessions) {
            if (s.getTeacher().equals(teacher)
                    && s.getDay() == day
                    && s.getSlot() == slot) {
                return s;
            }
        }
        return null;
    }

    public Session getSessionByRoom(Room room, int day, int slot) {
        for (Session s : sessions) {
            if (s.getRoom().equals(room)
                    && s.getDay() == day
                    && s.getSlot() == slot) {
                return s;
            }
        }
        return null;
    }

    public List<Session> getSessionsByDaySlot(int day, int slot) {
        List<Session> result = new ArrayList<>();
        for (Session s : sessions) {
            if (s.getDay() == day && s.getSlot() == slot) {
                result.add(s);
            }
        }
        return result;
    }

    public void updateSession(Session oldSession, Session newSession) {
        int index = sessions.indexOf(oldSession);
        if (index != -1) {
            sessions.set(index, newSession);
        }
    }

    public int getTeacherLoad(Teacher teacher) {
        int count = 0;
        for (Session s : sessions) {
            if (s.getTeacher().equals(teacher)) {
                count++;
            }
        }
        return count;
    }

    public String getTeacherStatus(Teacher teacher, int day, int slot) {
        Session s = getSessionByTeacher(teacher, day, slot);

        if (s != null) {
            return "Teaching: " + s.getSubject().getName() +
                   " (Section " + s.getSection() + ")";
        }

        return "Free";
    }

    public String getRoomStatus(Room room, int day, int slot) {
        Session s = getSessionByRoom(room, day, slot);

        if (s != null) {
            return "Occupied: " + s.getSubject().getName() +
                   " (Section " + s.getSection() + ")";
        }

        return "Free";
    }

    public List<Session> getSectionSchedule(String section, int day) {
        List<Session> result = new ArrayList<>();

        for (Session s : sessions) {
            if (s.getSection().equals(section) && s.getDay() == day) {
                result.add(s);
            }
        }

        return result;
    }
}
