package com.college.complaint.controller;

import com.college.complaint.config.CustomUserDetails;
import com.college.complaint.dto.ComplaintFormDto;
import com.college.complaint.entity.*;
import com.college.complaint.service.ComplaintService;
import com.college.complaint.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final ComplaintService complaintService;

    @Autowired
    public StudentController(StudentService studentService, ComplaintService complaintService) {
        this.studentService = studentService;
        this.complaintService = complaintService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Student student = studentService.getStudentById(userDetails.getId());
        List<Complaint> complaints = complaintService.getComplaintsByStudent(student);

        long total = complaintService.countStudentTotalComplaints(student.getId());
        long pending = complaintService.countStudentComplaintsByStatus(student.getId(), Status.PENDING);
        long inProgress = complaintService.countStudentComplaintsByStatus(student.getId(), Status.IN_PROGRESS);
        long resolved = complaintService.countStudentComplaintsByStatus(student.getId(), Status.RESOLVED);
        long rejected = complaintService.countStudentComplaintsByStatus(student.getId(), Status.REJECTED);

        model.addAttribute("student", student);
        model.addAttribute("recentComplaints", complaints.stream().limit(5).toList());
        model.addAttribute("totalComplaints", total);
        model.addAttribute("pendingComplaints", pending);
        model.addAttribute("inProgressComplaints", inProgress);
        model.addAttribute("resolvedComplaints", resolved);
        model.addAttribute("rejectedComplaints", rejected);

        return "student/dashboard";
    }

    @GetMapping("/complaints/new")
    public String showRaiseComplaintForm(Model model) {
        model.addAttribute("complaintDto", new ComplaintFormDto());
        model.addAttribute("categories", Category.values());
        model.addAttribute("priorities", Priority.values());
        return "student/raise-complaint";
    }

    @PostMapping("/complaints/new")
    public String submitComplaint(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute("complaintDto") ComplaintFormDto dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", Category.values());
            model.addAttribute("priorities", Priority.values());
            return "student/raise-complaint";
        }

        Student student = studentService.getStudentById(userDetails.getId());
        Complaint created = complaintService.raiseComplaint(student, dto);

        redirectAttributes.addFlashAttribute("successMessage",
                "Complaint #" + created.getId() + " has been raised successfully!");
        return "redirect:/student/complaints";
    }

    @GetMapping("/complaints")
    public String myComplaints(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Student student = studentService.getStudentById(userDetails.getId());
        List<Complaint> complaints = complaintService.getComplaintsByStudent(student);
        model.addAttribute("complaints", complaints);
        return "student/my-complaints";
    }

    @GetMapping("/complaints/{id}")
    public String viewComplaintDetails(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("id") Long complaintId,
            Model model
    ) {
        // Enforces strict ownership verification in backend!
        Complaint complaint = complaintService.getComplaintForStudent(complaintId, userDetails.getId());
        model.addAttribute("complaint", complaint);
        return "student/complaint-details";
    }
}
