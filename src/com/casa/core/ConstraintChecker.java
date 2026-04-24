package com.casa.core;

import com.casa.model.Room;
import com.casa.model.Teacher;
import com.casa.model.Subject;


public class ConstraintChecker {

    private AvailabilityService availabilityService;

    public ConstraintChecker(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

   public boolean isValid(Subject subject, Teacher teacher, Room r, int day, int slot) {

    
    boolean teacherAvailable = availabilityService.isTeacherAvailable(teacher, day, slot);

    
    boolean roomAvailable = availabilityService.isRoomAvailable(r, day, slot);

    
    boolean roomTypeMatches = subject.getRequiredRoomType() != null &&
                             subject.getRequiredRoomType().equals(r.getType());

                             
    boolean labTypeMatches = true;

    if ("LAB".equals(subject.getRequiredRoomType())) {
        labTypeMatches = subject.getRequiredLabType() != null &&
                         subject.getRequiredLabType().equals(r.getSubType());
    }

    return teacherAvailable
        && roomAvailable
        && roomTypeMatches
        && labTypeMatches;
}
}