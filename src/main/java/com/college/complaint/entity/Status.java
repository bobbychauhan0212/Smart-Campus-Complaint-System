package com.college.complaint.entity;

public enum Status {
    PENDING("Pending", "bg-warning text-dark"),
    IN_PROGRESS("In Progress", "bg-primary"),
    RESOLVED("Resolved", "bg-success"),
    REJECTED("Rejected", "bg-danger");

    private final String displayName;
    private final String badgeClass;

    Status(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
