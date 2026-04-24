package com.casa.core;

import com.casa.model.ReallocationLog;
import com.casa.model.ReallocationRequest;
import com.casa.model.Session;
import com.casa.model.Teacher;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ReallocationService {

    private static final String[] DAYS = {"Mon", "Tue", "Wed", "Thu", "Fri"};
    private static final String[] SLOTS = {
            "8:00-8:55",
            "9:00-9:55",
            "9:55-10:50",
            "11:10-12:05",
            "12:05-1:00",
            "1:00-1:55"
    };

    private SessionRegistryService registry;
    private AvailabilityService availability;
    private ReallocationLogService logService;

    public ReallocationService(SessionRegistryService registry,
                               AvailabilityService availability,
                               ReallocationLogService logService) {
        this.registry = registry;
        this.availability = availability;
        this.logService = logService;
    }

    public void handleTeacherAbsence(Teacher absentTeacher, int day, int slot, List<Teacher> allTeachers, Scanner sc) {

        Session session = registry.getSessionByTeacher(absentTeacher, day, slot);

        if (session == null) {
            System.out.println("No class found at this slot.");
            return;
        }

        System.out.println("\n================ REALLOCATION EVENT ================");
        System.out.println("[BEFORE]");
        System.out.println(formatEventLine(session));

        List<Teacher> candidates = new ArrayList<>();

        for (Teacher t : allTeachers) {
            if (!t.equals(absentTeacher)
                    && availability.isTeacherAvailable(t, day, slot)
                    && t.getSubjects().contains(session.getSubject().getName())) {
                candidates.add(t);
            }
        }

        candidates.sort(Comparator.comparingInt(registry::getTeacherLoad));

        for (Teacher candidate : candidates) {
            ReallocationRequest request = new ReallocationRequest(session, candidate);

            System.out.println("\nTrying: " + candidate.getName() + " (" + candidate.getId() + ")");
            System.out.println("Notification sent to " + candidate.getName());
            System.out.print("Approve assignment? (y/n): ");

            String decision = sc.next().trim();

            if (decision.equalsIgnoreCase("y")) {
                request.approve();

                String original = session.getOriginalTeacher().getName();
                String newTeacher = candidate.getName();

                session.setTeacher(candidate);
                registry.updateSession(session, session);
                availability.markTeacherBusy(candidate, day, slot);

                ReallocationLog log = new ReallocationLog(
                        session.getSubject().getName(),
                        session.getSection(),
                        original,
                        newTeacher,
                        day,
                        slot,
                        "Teacher Absent"
                );

                logService.addLog(log);

                System.out.println("Teacher approved. Class assigned.");
                System.out.println("\n[AFTER]");
                System.out.println(formatEventLine(session));
                System.out.println("\nReason: Teacher Absent");
                return;
            }

            System.out.print("Enter rejection reason: ");
            String reason = readLine(sc);

            request.reject(reason);

            System.out.println("\n[ADMIN ALERT]");
            System.out.println(candidate.getName() + " rejected the assignment.");
            System.out.println("Reason: " + request.getRejectionReason());
            System.out.print("Admin approval to retry with next candidate? (y/n): ");

            String adminDecision = sc.next().trim();
            if (!adminDecision.equalsIgnoreCase("y")) {
                System.out.println("Reallocation stopped by admin.");
                return;
            }

            System.out.println("Admin approved retry. Checking next candidate...");
        }

        System.out.println("No replacement found.");
    }

    private String formatEventLine(Session session) {
        return session.getSubject().getName() +
                " | Section " + session.getSection() +
                " | " + getDayLabel(session.getDay()) +
                " " + getSlotLabel(session.getSlot()) +
                " | " + session.getTeacher().getName();
    }

    private String getDayLabel(int day) {
        if (day >= 0 && day < DAYS.length) {
            return DAYS[day];
        }
        return "Day " + day;
    }

    private String getSlotLabel(int slot) {
        if (slot >= 0 && slot < SLOTS.length) {
            return SLOTS[slot];
        }
        return "Slot " + slot;
    }

    private String readLine(Scanner sc) {
        String line = sc.nextLine().trim();
        if (!line.isEmpty()) {
            return line;
        }

        return sc.nextLine().trim();
    }
}
