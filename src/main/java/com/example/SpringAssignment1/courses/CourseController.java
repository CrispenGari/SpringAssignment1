package com.example.SpringAssignment1.courses;

import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService userService;

    @GetMapping
    public ArrayList<Course> getUsers(){
        return  this.userService.getCourses();
    }
}
