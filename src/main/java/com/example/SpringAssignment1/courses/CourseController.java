package com.example.SpringAssignment1.courses;
import com.example.SpringAssignment1.exceptions.CourseAlreadyExistsException;
import com.example.SpringAssignment1.types.AddCourseBody;
import com.example.SpringAssignment1.types.UpdateCourseBody;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping(path = "/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;

    @PostMapping(path = "/add")
    public ResponseEntity<Course> addCourse(@Valid @RequestBody AddCourseBody body) throws Exception {
        Course course = new Course();
        course.setName(body.getName().toUpperCase().trim());
        course.setCode(body.getCode());

        course.setContent(body.getContent().trim());
        course.setCredits(body.getCredits());
        course.setTitle(body.getTitle().trim());
        course.setAssessment(body.getAssessment().trim());
        course.setPurpose(body.getPurpose().trim());
        course.setInstruction(body.getInstruction().trim());

        course.setCategory(body.getCategory());
        String ext =  course.getCategory().equals(Category.FOUNDATION) ? "F" : "";
        String displayName = course.getName() + " " + course.getCode() + ext;
        course.setDisplayName(displayName);
        if (service.isCourseAvailable(course.getDisplayName())){
            throw new CourseAlreadyExistsException("The course '" + course.getDisplayName() + "' is already there.");
        }
        // then save the course
        return ResponseEntity.status(201).body(this.service.addCourse(course));
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable("courseId") Long courseId,
            @Valid @RequestBody UpdateCourseBody body
    ) {
        Course course = this.service.getCourse(courseId);
        course.setName(body.getName().toUpperCase().trim());
        course.setCode(body.getCode());

        course.setContent(body.getContent().trim());
        course.setCredits(body.getCredits());
        course.setTitle(body.getTitle().trim());
        course.setAssessment(body.getAssessment().trim());
        course.setPurpose(body.getPurpose().trim());
        course.setInstruction(body.getInstruction().trim());


        course.setCategory(body.getCategory());
        String ext =  course.getCategory().equals(Category.FOUNDATION) ? "F" : "";
        String displayName = course.getName() + " " + course.getCode() + ext;
        course.setDisplayName(displayName);
        return ResponseEntity.status(204).body(this.service.updateCourse(course));
    }

    @DeleteMapping("/remove/{courseId}")
    public ResponseEntity<Boolean> deleteCourse(@PathVariable("courseId") Long courseId){
        this.service.getCourse(courseId);
        return ResponseEntity.status(204).body(this.service.removeCourse(courseId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Course> getAnimal(@PathVariable("courseId") Long courseId){
        return ResponseEntity.status(200).body(this.service.getCourse(courseId));
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Course>> getCourses(){
        return ResponseEntity.status(200).body(this.service.getCourses());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ArrayList<Map<String,String>>  handleValidationExceptions(MethodArgumentNotValidException exception){
        ArrayList<Map<String,String>> errors = new ArrayList<Map<String, String>>();
        exception.getBindingResult().getAllErrors().forEach((error)->{
            Map<String, String> e = new HashMap<>();
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            e.put(field, message);
            e.put("timestamp", new Date().toString());
            e.put("field", field);
            errors.add(e);
        });
        return errors;
    }
}
