package com.casa.model;

public class ReallocationRequest {

    private Session session;
    private Teacher proposedTeacher;
    private String status;
    private String rejectionReason;

    public ReallocationRequest(Session session, Teacher proposedTeacher) {
        this.session = session;
        this.proposedTeacher = proposedTeacher;
        this.status = "PENDING";
    }

    public void approve() {
        this.status = "APPROVED";
    }

    public void reject(String reason) {
        this.status = "REJECTED";
        this.rejectionReason = reason;
    }

    public String getStatus() {
        return status;
    }

    public Teacher getProposedTeacher() {
        return proposedTeacher;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public Session getSession() {
        return session;
    }
}
