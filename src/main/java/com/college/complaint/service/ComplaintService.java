package com.college.complaint.service;

import com.college.complaint.dto.ComplaintFormDto;
import com.college.complaint.dto.ComplaintStatusUpdateDto;
import com.college.complaint.entity.*;
import com.college.complaint.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @Transactional
    public Complaint raiseComplaint(Student student, ComplaintFormDto dto) {
        Complaint complaint = new Complaint(
                student,
                dto.getTitle(),
                dto.getCategory(),
                dto.getDescription(),
                dto.getPriority()
        );
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getComplaintsByStudent(Student student) {
        return complaintRepository.findByStudentOrderByCreatedAtDesc(student);
    }

    /**
     * Ownership Security Enforcer:
     * Ensures that a student can ONLY view complaints that belong to them.
     */
    public Complaint getComplaintForStudent(Long complaintId, Long studentId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new IllegalArgumentException("Complaint not found with ID: " + complaintId));

        if (!complaint.getStudent().getId().equals(studentId)) {
            throw new AccessDeniedException("Access Denied: You do not have permission to view this complaint.");
        }

        return complaint;
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Complaint not found with ID: " + id));
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Complaint> filterAndSearchComplaints(Status status, Category category, Priority priority, String search) {
        return complaintRepository.filterAndSearchComplaints(status, category, priority, search);
    }

    @Transactional
    public Complaint updateComplaintStatus(Long complaintId, Status status, String adminRemarks) {
        Complaint complaint = getComplaintById(complaintId);
        complaint.setStatus(status);
        complaint.setAdminRemarks(adminRemarks);
        complaint.setUpdatedAt(LocalDateTime.now());
        return complaintRepository.save(complaint);
    }

    @Transactional
    public Complaint updateComplaintStatus(Long complaintId, ComplaintStatusUpdateDto dto) {
        return updateComplaintStatus(complaintId, dto.getStatus(), dto.getAdminRemarks());
    }

    // Metric Counters

    public long countTotalComplaints() {
        return complaintRepository.count();
    }

    public long countPendingComplaints() {
        return complaintRepository.countByStatus(Status.PENDING);
    }

    public long countInProgressComplaints() {
        return complaintRepository.countByStatus(Status.IN_PROGRESS);
    }

    public long countResolvedComplaints() {
        return complaintRepository.countByStatus(Status.RESOLVED);
    }

    public long countRejectedComplaints() {
        return complaintRepository.countByStatus(Status.REJECTED);
    }

    public long countStudentTotalComplaints(Long studentId) {
        return complaintRepository.countByStudentId(studentId);
    }

    public long countStudentComplaintsByStatus(Long studentId, Status status) {
        return complaintRepository.countByStudentIdAndStatus(studentId, status);
    }
}
