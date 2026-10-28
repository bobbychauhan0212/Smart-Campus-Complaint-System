package com.college.complaint.entity;

public enum Category {
    ACADEMIC("Academic"),
    INFRASTRUCTURE("Infrastructure"),
    HOSTEL("Hostel"),
    LIBRARY("Library"),
    TRANSPORT("Transport"),
    EXAMINATION("Examination"),
    FEES("Fees"),
    FACULTY("Faculty"),
    IT_INTERNET("IT / Internet"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
