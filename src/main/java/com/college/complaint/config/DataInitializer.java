package com.college.complaint.config;

import com.college.complaint.entity.Admin;
import com.college.complaint.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed Default College Admin User if no admin exists in database
        if (adminRepository.count() == 0) {
            Admin admin = new Admin(
                    "College Administrator",
                    "admin@college.edu",
                    passwordEncoder.encode("admin123")
            );
            adminRepository.save(admin);
            System.out.println(">>> Default College Admin Account Created: admin@college.edu / admin123");
        }
    }
}
