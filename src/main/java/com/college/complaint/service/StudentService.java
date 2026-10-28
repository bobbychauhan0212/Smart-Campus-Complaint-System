package com.college.complaint.service;

import com.college.complaint.dto.StudentRegistrationDto;
import com.college.complaint.entity.Student;
import com.college.complaint.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Student registerStudent(StudentRegistrationDto dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("An account with email " + dto.getEmail() + " already exists.");
        }
        if (studentRepository.existsByRollNo(dto.getRollNo())) {
            throw new IllegalArgumentException("Roll number " + dto.getRollNo() + " is already registered.");
        }

        Student student = new Student(
                dto.getName(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getRollNo(),
                dto.getDepartment(),
                dto.getYear()
        );

        return studentRepository.save(student);
    }

    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with email: " + email));
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
    }

    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    public boolean existsByRollNo(String rollNo) {
        return studentRepository.existsByRollNo(rollNo);
    }
}
