package com.example.SpringAssignment1.courses;

import com.example.SpringAssignment1.exceptions.CourseNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class CourseService implements CourseServiceInterface {
    public final CourseRepository repository;

    @Override
    public Course addCourse(Course course) {
        return this.repository.save(course);
    }

    @Override
    public Course getCourse(Long id) {
        return this.repository.findById(id).orElseThrow(
                ()->new CourseNotFoundException("The course with id: '" + id + "' was not found.")
        );
    }

    @Override
    public Boolean isCourseAvailable(String displayName){
        Optional<Course> c = this.repository.findByDisplayName(displayName);
        return  c.isPresent();
    }

    @Override
    public Boolean removeCourse(Long id) {
        this.repository.deleteById(id);
        return true;
    }

    @Override
    public Course updateCourse(Course course) {
        return this.repository.save(course);
    }

    @Override
    public Collection<Course> getCourses() {
        return this.repository.findAll();
    }
}
