package com.casa.main;

import com.casa.core.AvailabilityService;
import com.casa.core.ReallocationLogService;
import com.casa.core.ReallocationService;
import com.casa.core.SessionRegistryService;
import com.casa.model.Room;
import com.casa.model.Session;
import com.casa.model.Subject;
import com.casa.model.Teacher;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class MainApp {

    private static final String[] DAYS = {"Mon", "Tue", "Wed", "Thu", "Fri"};
    private static final String[] SLOTS = {
            "8:00-8:55",
            "9:00-9:55",
            "9:55-10:50",
            "11:10-12:05",
            "12:05-1:00",
            "1:00-1:55"
    };

    public static void main(String[] args) {
        Teacher t1 = new Teacher("T1", "Dr. Akshay Rajput", Arrays.asList("DAA"), 3);
        Teacher t2 = new Teacher("T2", "Dr. Abhay Sharma", Arrays.asList("Microprocessors"), 3);
        Teacher t3 = new Teacher("T3", "Dr. Sidhhant Thapliyal", Arrays.asList("Formal Languages", "DAA"), 2);
        Teacher t4 = new Teacher("T4", "Dr. Jyoti Agarwal", Arrays.asList("Programming in Java Lab", "Java"), 3);
        Teacher t5 = new Teacher("T5", "Mr. Purnendu Agarwal", Arrays.asList("Soft Skills"), 2);
        Teacher t6 = new Teacher("T6", "Ms. Kumkum", Arrays.asList("DAA"), 3);
        Teacher t7 = new Teacher("T7", "Mr. Ravi Prakash", Arrays.asList("DAA"), 2);
        
        List<Teacher> teachers = Arrays.asList(t1, t2, t3, t4, t5, t6, t7);

        AvailabilityService availability = new AvailabilityService();
        for (Teacher teacher : teachers) {
            availability.addTeacher(teacher);
        }

        SessionRegistryService registry = new SessionRegistryService();
        ReallocationLogService logService = new ReallocationLogService();

        Subject daa = new Subject("DAA", 3, "CR", null);
        Subject mp = new Subject("Microprocessors", 3, "CR", null);
        Subject javaLab = new Subject("Programming in Java Lab", 2, "LAB", "Java");
        Subject soft = new Subject("Soft Skills", 2, "CR", null);

        Room cr3 = new Room("CR-3", "CR", null, 60);
        Room lab1 = new Room("Lab-1", "LAB", "Java", 40);
        List<Room> rooms = Arrays.asList(cr3, lab1);

        availability.addRoom(cr3);
        availability.addRoom(lab1);

        registry.addSession(new Session(daa, t1, cr3, 0, 2, "C"));
        registry.addSession(new Session(mp, t2, cr3, 1, 1, "C"));
        registry.addSession(new Session(javaLab, t4, lab1, 0, 5, "C"));
        registry.addSession(new Session(soft, t5, cr3, 2, 1, "C"));

        for (Session session : registry.getSessions()) {
            availability.markTeacherBusy(session.getTeacher(), session.getDay(), session.getSlot());
            availability.markRoomBusy(session.getRoom(), session.getDay(), session.getSlot());
        }

        ReallocationService reallocation =
                new ReallocationService(registry, availability, logService);

        Scanner sc = new Scanner(System.in);

        System.out.println("CASA - Constraint-Based Academic Scheduling & Allocation System");

        while (true) {
            printMainMenu();
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    adminPanel(registry, availability, reallocation, logService, teachers, rooms, sc);
                    break;
                case 2:
                    teacherPanel(registry, availability, teachers, sc);
                    break;
                case 3:
                    studentPanel(registry, availability, teachers, rooms, sc);
                    break;
                case 0:
                    System.out.println("\nExiting CASA System...");
                    sc.close();
                    return;
                default:
                    System.out.println("\nInvalid choice. Please select a valid role.");
            }
        }
    }

    public static void adminPanel(SessionRegistryService registry,
                                  AvailabilityService availability,
                                  ReallocationService reallocation,
                                  ReallocationLogService logService,
                                  List<Teacher> teachers,
                                  List<Room> rooms,
                                  Scanner sc) {
        while (true) {
            printPanelTitle("ADMIN DASHBOARD");
            System.out.println("Logged in as: ADMIN");

            System.out.println("\n1. View Timetable");
            System.out.println("2. View Absence Requests");
            System.out.println("3. Process Reallocation");
            System.out.println("4. View Reallocation Logs");
            System.out.println("5. View Directory");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    printSectionTitle("TIMETABLE");
                    printSessions(registry.getSessions());
                    break;
                case 2:
                    viewAbsenceRequests(availability, teachers);
                    break;
                case 3:
                    processReallocationFlow(registry, availability, reallocation, teachers, sc);
                    printSectionTitle("UPDATED TIMETABLE");
                    printSessions(registry.getSessions());
                    break;
                case 4:
                    logService.printLogs();
                    break;
                case 5:
                    adminDirectory(registry, availability, teachers, rooms);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void teacherPanel(SessionRegistryService registry,
                                    AvailabilityService availability,
                                    List<Teacher> teachers,
                                    Scanner sc) {
        System.out.println("Available Teacher IDs: T1, T2, T3, T4, T5, T6, T7");
        System.out.print("Enter Teacher ID: ");
        String id = sc.next();

        Teacher current = findTeacherById(teachers, id);

        if (current == null) {
            System.out.println("Teacher not found.");
            return;
        }

        while (true) {
            printPanelTitle("TEACHER DASHBOARD");
            System.out.println("Logged in as: TEACHER (" + current.getId() + " - " + current.getName() + ")");

            System.out.println("\n1. Mark Availability");
            System.out.println("2. View My Schedule");
            System.out.println("3. View My Status");
            System.out.println("4. View Reallocated Classes");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    markAvailability(current, availability, sc);
                    break;
                case 2:
                    viewMySchedule(registry, current);
                    break;
                case 3:
                    viewMyStatus(registry, availability, current);
                    break;
                case 4:
                    viewReallocatedClasses(registry, current);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid teacher action.");
            }
        }
    }

    public static void studentPanel(SessionRegistryService registry,
                                    AvailabilityService availability,
                                    List<Teacher> teachers,
                                    List<Room> rooms,
                                    Scanner sc) {
        while (true) {
            printPanelTitle("STUDENT DASHBOARD");
            System.out.println("Logged in as: STUDENT");

            System.out.println("\n1. View Timetable");
            System.out.println("2. View Teacher Availability");
            System.out.println("3. View Room Availability");
            System.out.println("4. View Teacher Directory");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    viewTimetableFlow(registry, sc);
                    break;
                case 2:
                    showTeacherAvailability(registry, availability, teachers);
                    break;
                case 3:
                    showRoomAvailability(registry, rooms);
                    break;
                case 4:
                    showTeacherDirectory(teachers);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static Teacher findTeacherById(List<Teacher> teachers, String id) {
        for (Teacher teacher : teachers) {
            if (teacher.getId().equalsIgnoreCase(id)) {
                return teacher;
            }
        }
        return null;
    }

    private static void printMainMenu() {
        System.out.println("\n================ CASA SYSTEM ================");
        System.out.println("1. Admin");
        System.out.println("2. Teacher");
        System.out.println("3. Student");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    private static void printPanelTitle(String title) {
        System.out.println("\n========== " + title + " ==========");
    }

    private static void printSectionTitle(String title) {
        System.out.println("\n---------------- " + title + " ----------------");
    }

    private static void printSessions(List<Session> sessions) {
        if (sessions.isEmpty()) {
            System.out.println("No sessions available.");
            return;
        }

        List<Session> sortedSessions = sessions.stream()
                .sorted(Comparator.comparing(Session::getDay)
                        .thenComparing(Session::getSlot))
                .toList();

        System.out.printf("%-3s %-11s %-4s %-22s %-22s %-6s %-11s%n",
                "Day", "Time", "Sec", "Subject", "Teacher", "Room", "Status");
        System.out.println("------------------------------------------------------------------------------------------");

        for (Session s : sortedSessions) {
            System.out.printf("%-3s %-11s %-4s %-22s %-22s %-6s %-11s%n",
                    getDayLabel(s.getDay()),
                    getSlotLabel(s.getSlot()),
                    s.getSection(),
                    trimToWidth(s.getSubject().getName(), 22),
                    trimToWidth(s.getTeacher().getName(), 22),
                    s.getRoom().getRoomId(),
                    s.isReallocated() ? "REALLOC" : "SCHEDULED");
        }
    }

    private static void printTeacherSessions(SessionRegistryService registry, Teacher current) {
        List<Session> teacherSessions = registry.getSessions().stream()
                .filter(session -> session.getTeacher().equals(current))
                .toList();

        if (teacherSessions.isEmpty()) {
            System.out.println("No classes currently assigned.");
            return;
        }

        printSessions(teacherSessions);
    }

    private static void viewAbsenceRequests(AvailabilityService availability,
                                            List<Teacher> teachers) {
        printSectionTitle("ABSENCE REQUESTS");
        boolean found = false;

        for (Teacher teacher : teachers) {
            if (!"UNAVAILABLE".equals(availability.getStatus(teacher))) {
                continue;
            }

            found = true;
            System.out.println(teacher.getId() + " | " + teacher.getName());
            System.out.println("Reason: " + availability.getReason(teacher));
            System.out.println("-----------------------------------");
        }

        if (!found) {
            System.out.println("No pending absence requests.");
        }
    }

    private static void processReallocationFlow(SessionRegistryService registry,
                                                AvailabilityService availability,
                                                ReallocationService reallocation,
                                                List<Teacher> teachers,
                                                Scanner sc) {
        printSectionTitle("PROCESS REALLOCATION");
        boolean found = false;

        for (Teacher teacher : teachers) {
            if (!availability.isAbsencePending(teacher)) {
                continue;
            }

            found = true;
            System.out.println("Teacher: " + teacher.getName());
            System.out.println("Reason: " + availability.getReason(teacher));
            System.out.print("Approve absence? (y/n): ");

            String decision = sc.next().trim();

            if (decision.equalsIgnoreCase("y")) {
                availability.approveUnavailability(teacher);
                System.out.println("Starting reallocation...");

                List<Session> impactedSessions = registry.getSessions().stream()
                        .filter(session -> session.getTeacher().equals(teacher))
                        .filter(session -> session.getDay() == 0)
                        .sorted(Comparator.comparing(Session::getSlot))
                        .toList();

                if (impactedSessions.isEmpty()) {
                    System.out.println("No impacted sessions found for today.");
                    continue;
                }

                for (Session impactedSession : impactedSessions) {
                    availability.markTeacherUnavailable(teacher, impactedSession.getDay(), impactedSession.getSlot());
                    reallocation.handleTeacherAbsence(
                            teacher,
                            impactedSession.getDay(),
                            impactedSession.getSlot(),
                            teachers,
                            sc
                    );
                }
            } else {
                availability.markAvailable(teacher);
                System.out.println("Marked as AVAILABLE again.");
            }
        }

        if (!found) {
            System.out.println("No pending absence requests.");
        }
    }

    private static void adminDirectory(SessionRegistryService registry,
                                       AvailabilityService availability,
                                       List<Teacher> teachers,
                                       List<Room> rooms) {
        printSectionTitle("TEACHER DIRECTORY");
        for (Teacher teacher : teachers) {
            System.out.println(teacher.getName() + " -> " + availability.getStatus(teacher));
        }

        printSectionTitle("ROOM STATUS");
        for (Room room : rooms) {
            System.out.println(room.getRoomId() + " -> " + buildRoomStatus(registry, room));
        }
    }

    private static void viewTimetableFlow(SessionRegistryService registry, Scanner sc) {
        printSectionTitle("TIMETABLE NAVIGATION");
        System.out.println("You are navigating the academic structure to view timetable.");

        System.out.println("\nChoose your Course:");
        System.out.println("1. BTech");
        sc.nextInt();

        System.out.println("\nChoose your Department:");
        System.out.println("1. Engineering");
        sc.nextInt();

        System.out.println("\nChoose your Branch:");
        System.out.println("1. CSE");
        sc.nextInt();

        System.out.println("\nChoose your Semester:");
        System.out.println("1. 4");
        sc.nextInt();

        System.out.println("\nChoose your Section:");
        System.out.println("1. A");
        System.out.println("2. B");
        System.out.println("3. C");
        int sectionChoice = sc.nextInt();

        String section = switch (sectionChoice) {
            case 1 -> "A";
            case 2 -> "B";
            case 3 -> "C";
            default -> "C";
        };

        String breadcrumb = "BTech > Engineering > CSE > Sem 4 > Section " + section;

        printSectionTitle("TIMETABLE");
        System.out.println("Selection: " + breadcrumb);

        List<Session> sessions = registry.getSessions().stream()
                .filter(session -> session.getSection().equalsIgnoreCase(section))
                .sorted(Comparator.comparing(Session::getDay)
                        .thenComparing(Session::getSlot))
                .toList();

        if (sessions.isEmpty()) {
            System.out.println("No timetable available for selected section.");
            return;
        }

        printSessions(sessions);
    }

    private static void showTeacherAvailability(SessionRegistryService registry,
                                                AvailabilityService availability,
                                                List<Teacher> teachers) {
        printSectionTitle("TEACHERS TODAY - CURRENT STATUS");
        for (Teacher teacher : teachers) {
            if ("UNAVAILABLE".equals(availability.getStatus(teacher))) {
                System.out.println(teacher.getName() + " -> NOT AVAILABLE (" +
                        availability.getReason(teacher) + ")");
                continue;
            }

            Session activeSession = registry.getSessionByTeacher(teacher, 0, 2);
            if (activeSession == null) {
                activeSession = registry.getSessionByTeacher(teacher, 0, 5);
            }

            if (activeSession != null) {
                System.out.println(teacher.getName() + " -> OCCUPIED (" +
                        activeSession.getSubject().getName() +
                        ", Section " + activeSession.getSection() +
                        ", " + activeSession.getRoom().getRoomId() + ")");
            } else {
                System.out.println(teacher.getName() + " -> FREE");
            }
        }
    }

    private static void markAvailability(Teacher teacher,
                                         AvailabilityService availability,
                                         Scanner sc) {
        printSectionTitle("MARK AVAILABILITY");
        System.out.print("Are you available? (y/n): ");
        String answer = sc.next().trim();

        if (answer.equalsIgnoreCase("y")) {
            availability.markAvailable(teacher);
            System.out.println("Marked AVAILABLE");
        } else {
            System.out.print("Enter reason: ");
            String reason = readLine(sc);
            availability.markUnavailable(teacher, reason);
            System.out.println("Marked UNAVAILABLE");
        }
    }

    private static void viewMySchedule(SessionRegistryService registry,
                                       Teacher teacher) {
        printSectionTitle("MY SCHEDULE");
        printTeacherSessions(registry, teacher);
    }

    private static void viewMyStatus(SessionRegistryService registry,
                                     AvailabilityService availability,
                                     Teacher teacher) {
        printSectionTitle("MY STATUS");
        System.out.println(formatTeacherDirectoryLine(registry, availability, teacher, 0, 2));
    }

    private static void viewReallocatedClasses(SessionRegistryService registry,
                                               Teacher teacher) {
        printSectionTitle("REALLOCATED CLASSES");
        List<Session> reallocatedSessions = registry.getSessions().stream()
                .filter(session -> session.getTeacher().equals(teacher))
                .filter(Session::isReallocated)
                .toList();

        if (reallocatedSessions.isEmpty()) {
            System.out.println("No reallocated classes assigned.");
            return;
        }

        printSessions(reallocatedSessions);
    }

    private static void showRoomAvailability(SessionRegistryService registry,
                                             List<Room> rooms) {
        printSectionTitle("ROOM STATUS");
        for (Room room : rooms) {
            System.out.println(room.getRoomId() + " -> " + buildRoomStatus(registry, room));
        }
    }

    private static void showTeacherDirectory(List<Teacher> teachers) {
        printSectionTitle("TEACHER DIRECTORY");
        for (Teacher teacher : teachers) {
            System.out.println(teacher.getId() + " | " + teacher.getName());
        }
    }

    private static String formatTeacherDirectoryLine(SessionRegistryService registry,
                                                     AvailabilityService availability,
                                                     Teacher teacher,
                                                     int day,
                                                     int slot) {
        if ("UNAVAILABLE".equals(availability.getStatus(teacher))) {
            return teacher.getName() + " -> UNAVAILABLE";
        }

        Session session = registry.getSessionByTeacher(teacher, day, slot);
        if (session != null) {
            return teacher.getName() + " -> TEACHING (" +
                    session.getSubject().getName() + ", Section " + session.getSection() + ")";
        }

        return teacher.getName() + " -> FREE";
    }

    private static String buildRoomStatus(SessionRegistryService registry, Room room) {
        Session activeSession = registry.getSessions().stream()
                .filter(session -> session.getRoom().equals(room))
                .filter(session -> session.getDay() == 0)
                .sorted(Comparator.comparing(Session::getSlot))
                .findFirst()
                .orElse(null);

        if (activeSession != null) {
            return "OCCUPIED (" + activeSession.getSubject().getName() +
                    ", Section " + activeSession.getSection() + ")";
        }

        return "FREE";
    }

    private static String getDayLabel(int day) {
        if (day >= 0 && day < DAYS.length) {
            return DAYS[day];
        }
        return "D" + day;
    }

    private static String getSlotLabel(int slot) {
        if (slot >= 0 && slot < SLOTS.length) {
            return SLOTS[slot];
        }
        return "Slot " + slot;
    }

    private static String trimToWidth(String value, int width) {
        if (value.length() <= width) {
            return value;
        }
        return value.substring(0, width - 3) + "...";
    }

    private static String readLine(Scanner sc) {
        String line = sc.nextLine().trim();
        if (!line.isEmpty()) {
            return line;
        }

        return sc.nextLine().trim();
    }
}
