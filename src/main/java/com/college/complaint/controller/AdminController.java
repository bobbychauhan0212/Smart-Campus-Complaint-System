package com.college.complaint.controller;

import com.college.complaint.dto.ComplaintStatusUpdateDto;
import com.college.complaint.entity.*;
import com.college.complaint.service.AdminService;
import com.college.complaint.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final ComplaintService complaintService;

    @Autowired
    public AdminController(AdminService adminService, ComplaintService complaintService) {
        this.adminService = adminService;
        this.complaintService = complaintService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long total = complaintService.countTotalComplaints();
        long pending = complaintService.countPendingComplaints();
        long inProgress = complaintService.countInProgressComplaints();
        long resolved = complaintService.countResolvedComplaints();
        long rejected = complaintService.countRejectedComplaints();

        List<Complaint> recentComplaints = complaintService.getAllComplaints().stream().limit(10).toList();

        model.addAttribute("totalComplaints", total);
        model.addAttribute("pendingComplaints", pending);
        model.addAttribute("inProgressComplaints", inProgress);
        model.addAttribute("resolvedComplaints", resolved);
        model.addAttribute("rejectedComplaints", rejected);
        model.addAttribute("recentComplaints", recentComplaints);

        return "admin/dashboard";
    }

    @GetMapping("/complaints")
    public String allComplaints(
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "search", required = false) String search,
            Model model
    ) {
        List<Complaint> complaints = complaintService.filterAndSearchComplaints(status, category, priority, search);

        model.addAttribute("complaints", complaints);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("categories", Category.values());
        model.addAttribute("priorities", Priority.values());

        // Retain selected filter parameters in the UI
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedPriority", priority);
        model.addAttribute("searchQuery", search);

        return "admin/all-complaints";
    }

    @GetMapping("/complaints/{id}")
    public String complaintDetails(@PathVariable("id") Long id, Model model) {
        Complaint complaint = complaintService.getComplaintById(id);

        ComplaintStatusUpdateDto statusDto = new ComplaintStatusUpdateDto(
                complaint.getStatus(),
                complaint.getAdminRemarks()
        );

        model.addAttribute("complaint", complaint);
        model.addAttribute("statusDto", statusDto);
        model.addAttribute("statuses", Status.values());

        return "admin/complaint-details";
    }

    @PostMapping("/complaints/{id}/status")
    public String updateComplaintStatus(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("statusDto") ComplaintStatusUpdateDto dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            Complaint complaint = complaintService.getComplaintById(id);
            model.addAttribute("complaint", complaint);
            model.addAttribute("statuses", Status.values());
            return "admin/complaint-details";
        }

        complaintService.updateComplaintStatus(id, dto.getStatus(), dto.getAdminRemarks());

        redirectAttributes.addFlashAttribute("successMessage",
                "Complaint #" + id + " status successfully updated to " + dto.getStatus().getDisplayName() + "!");

        return "redirect:/admin/complaints/" + id;
    }
}
