package com.college.complaint.controller;

import com.college.complaint.config.CustomUserDetails;
import com.college.complaint.dto.StudentRegistrationDto;
import com.college.complaint.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final StudentService studentService;

    @Autowired
    public AuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            if (userDetails.isAdmin()) {
                return "redirect:/admin/dashboard";
            } else if (userDetails.isStudent()) {
                return "redirect:/student/dashboard";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            @RequestParam(value = "type", required = false, defaultValue = "student") String type,
            Model model
    ) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Registration successful! Please login with your credentials.");
        }
        model.addAttribute("activeTab", type);
        return "login";
    }

    @GetMapping("/student/login")
    public String showStudentLoginPage() {
        return "redirect:/login?type=student";
    }

    @GetMapping("/admin/login")
    public String showAdminLoginPage() {
        return "redirect:/login?type=admin";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("studentDto", new StudentRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(
            @Valid @ModelAttribute("studentDto") StudentRegistrationDto dto,
            BindingResult bindingResult,
            Model model
    ) {
        if (studentService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "error.studentDto", "An account with this email already exists.");
        }
        if (studentService.existsByRollNo(dto.getRollNo())) {
            bindingResult.rejectValue("rollNo", "error.studentDto", "Roll number is already registered.");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            studentService.registerStudent(dto);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }
}
