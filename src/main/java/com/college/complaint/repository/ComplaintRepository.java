package com.college.complaint.repository;

import com.college.complaint.entity.Category;
import com.college.complaint.entity.Complaint;
import com.college.complaint.entity.Priority;
import com.college.complaint.entity.Status;
import com.college.complaint.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByStudentOrderByCreatedAtDesc(Student student);

    List<Complaint> findAllByOrderByCreatedAtDesc();

    long countByStudentId(Long studentId);

    long countByStudentIdAndStatus(Long studentId, Status status);

    long countByStatus(Status status);

    @Query("SELECT c FROM Complaint c WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:category IS NULL OR c.category = :category) AND " +
           "(:priority IS NULL OR c.priority = :priority) AND " +
           "(:search IS NULL OR :search = '' OR " +
           " LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           " LOWER(c.student.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           " LOWER(c.student.rollNo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           " CAST(c.id AS string) LIKE CONCAT('%', :search, '%')) " +
           "ORDER BY c.createdAt DESC")
    List<Complaint> filterAndSearchComplaints(
            @Param("status") Status status,
            @Param("category") Category category,
            @Param("priority") Priority priority,
            @Param("search") String search
    );
}
