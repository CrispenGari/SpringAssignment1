package com.example.SpringAssignment1.courses;

import java.util.Collection;

public interface CourseServiceInterface {
    Course addCourse(Course Course);
    Course getCourse(Long id);
    Boolean removeCourse(Long id);
    Course updateCourse(Course course);
    Collection<Course> getCourses();
}
