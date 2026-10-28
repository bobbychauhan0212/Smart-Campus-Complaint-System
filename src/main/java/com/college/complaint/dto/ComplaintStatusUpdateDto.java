package com.college.complaint.dto;

import com.college.complaint.entity.Status;
import jakarta.validation.constraints.NotNull;

public class ComplaintStatusUpdateDto {

    @NotNull(message = "Status is required")
    private Status status;

    private String adminRemarks;

    public ComplaintStatusUpdateDto() {
    }

    public ComplaintStatusUpdateDto(Status status, String adminRemarks) {
        this.status = status;
        this.adminRemarks = adminRemarks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }
}
