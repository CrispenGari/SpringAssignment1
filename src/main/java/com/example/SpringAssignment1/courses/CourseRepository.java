package com.example.SpringAssignment1.courses;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByDisplayName(String displayName);

}
