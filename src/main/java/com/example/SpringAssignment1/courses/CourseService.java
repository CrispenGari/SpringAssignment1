package com.example.SpringAssignment1.courses;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CourseService {

    public ArrayList<Course> getCourses(){
        String[] names = {"CSC", "CSC", "CSC", "CSC", "CSC"};
        int[] codes = {113, 212, 121, 223, 313};
        ArrayList<Course> courses = new ArrayList<>();

//		UNDERGRADUATE COURSES
        courses.add( new Course("CSC", 113, Category.UNDERGRADUATE));
        courses.add( new Course("CSC", 121, Category.UNDERGRADUATE));
        courses.add( new Course("CSC", 212, Category.UNDERGRADUATE));
        courses.add( new Course("CSC", 313, Category.UNDERGRADUATE));
        courses.add( new Course("CSC", 312, Category.UNDERGRADUATE));

//		FOUNDATION COURSES
        courses.add( new Course("CSC", 113, Category.FOUNDATION));
        courses.add( new Course("CSC", 121, Category.FOUNDATION));

        return courses;
    }
}
