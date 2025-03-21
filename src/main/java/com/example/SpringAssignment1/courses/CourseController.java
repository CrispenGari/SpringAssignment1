package com.example.SpringAssignment1.courses;

import com.example.SpringAssignment1.types.CourseBody;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;

    @PostMapping(path = "/add")
    public ResponseEntity<Course> addCourse(@RequestBody CourseBody body) throws Exception {
        Course course = new Course();
        // do validation
         String [] allowedCourses = {"undergraduate", "honours", "foundation"};
        if(body.getName().length() != 3){
            throw new Exception(
                    "Course can not be added because the Course Name should contain exactly 3 characters, but got '" +
                            body.getName() + "'."
            );
        }
        if(Integer.toString(body.getCode()).length() != 3){
            throw new Exception(
                    "Course can not be added because the Course Code should contain exactly 3 characters, but got '" +
                    body.getCode() + "'."
            );
        }
        course.setName(body.getName().toUpperCase());
        course.setCode(body.getCode());
        course.setDescription(body.getDescription());
        course.setCategory(body.getCategory());
        String ext =  course.getCategory().equals(Category.FOUNDATION) ? "F" : "";
        String displayName = course.getName() + " " + course.getCode() + ext;
        course.setDisplayName(displayName);
        // then save the course
        return ResponseEntity.status(201).body(this.service.addCourse(course));
    }


//    @PutMapping("/update/{animalId}")
//    public ResponseEntity<Animal> updateAnimal(@PathVariable("animalId") Long animalId, @RequestBody Animal animal){
//        Animal animal1 = this.service.getAnimal(animalId);
//        animal1.setName(animal.getName());
//        return ResponseEntity.status(204).body(this.service.updateAnimal(animal1));
//    }
//
//    @DeleteMapping("/delete/{animalId}")
//    public ResponseEntity<Boolean> deleteAnimal(@PathVariable("animalId") Long animalId){
//        return ResponseEntity.status(204).body(this.service.deleteAnimal(animalId));
//    }
//
//    @GetMapping("/animal/{animalId}")
//    public ResponseEntity<Animal> getAnimal(@PathVariable("animalId") Long animalId){
//        return ResponseEntity.status(200).body(this.service.getAnimal(animalId));
//    }
//
    @GetMapping
    public ResponseEntity<Collection<Course>> getCourses(){
        return ResponseEntity.status(200).body(this.service.getCourses());
    }
}
