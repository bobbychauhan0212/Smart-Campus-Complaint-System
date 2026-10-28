package com.college.complaint.config;

import com.college.complaint.entity.Admin;
import com.college.complaint.entity.Student;
import com.college.complaint.repository.AdminRepository;
import com.college.complaint.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public CustomUserDetailsService(StudentRepository studentRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // First check Admin repository
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            return new CustomUserDetails(adminOpt.get());
        }

        // If not admin, check Student repository
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            return new CustomUserDetails(studentOpt.get());
        }

        throw new UsernameNotFoundException("No user found with email: " + email);
    }
}
