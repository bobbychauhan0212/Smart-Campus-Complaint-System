package com.college.complaint.controller;

import com.college.complaint.config.CustomUserDetails;
import com.college.complaint.dto.ComplaintFormDto;
import com.college.complaint.dto.ComplaintStatusUpdateDto;
import com.college.complaint.entity.*;
import com.college.complaint.service.ComplaintService;
import com.college.complaint.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ComplaintApiController {

    private final ComplaintService complaintService;
    private final StudentService studentService;

    @Autowired
    public ComplaintApiController(ComplaintService complaintService, StudentService studentService) {
        this.complaintService = complaintService;
        this.studentService = studentService;
    }

    // POST /api/complaints - Raise complaint for student
    @PostMapping("/complaints")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Complaint> createComplaint(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ComplaintFormDto dto
    ) {
        Student student = studentService.getStudentById(userDetails.getId());
        Complaint complaint = complaintService.raiseComplaint(student, dto);
        return new ResponseEntity<>(complaint, HttpStatus.CREATED);
    }

    // GET /api/complaints/my - Get logged-in student's complaints
    @GetMapping("/complaints/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Complaint>> getMyComplaints(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Student student = studentService.getStudentById(userDetails.getId());
        List<Complaint> complaints = complaintService.getComplaintsByStudent(student);
        return ResponseEntity.ok(complaints);
    }

    // GET /api/complaints/{id} - Get specific complaint details (ownership validated)
    @GetMapping("/complaints/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Complaint> getComplaintDetails(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("id") Long id
    ) {
        Complaint complaint = complaintService.getComplaintForStudent(id, userDetails.getId());
        return ResponseEntity.ok(complaint);
    }

    // GET /api/admin/complaints - Get all complaints for Admin
    @GetMapping("/admin/complaints")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Complaint>> getAllComplaintsAdmin(
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "search", required = false) String search
    ) {
        List<Complaint> complaints = complaintService.filterAndSearchComplaints(status, category, priority, search);
        return ResponseEntity.ok(complaints);
    }

    // PUT /api/admin/complaints/{id}/status - Update complaint status (Admin)
    @PutMapping("/admin/complaints/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Complaint> updateStatusAdmin(
            @PathVariable("id") Long id,
            @Valid @RequestBody ComplaintStatusUpdateDto dto
    ) {
        Complaint updated = complaintService.updateComplaintStatus(id, dto);
        return ResponseEntity.ok(updated);
    }
}
