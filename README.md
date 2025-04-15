### SpringAssignment Group 15

🕸️ Introduction to web APIs using Spring and Java Assignment. Group Assignment. This read me file will detail all the code explanation and how we go about solving the assignment problem. This assignment was for testing Java Developers their capability in using Springboot to build a basic `CRUD` `REST` Web `API` with error handling.

<p align="center">
  <a href="https://github.com/CrispenGari/SpringAssignment1/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License: MIT">
  </a>
  <a href="https://www.java.com/">
    <img src="https://img.shields.io/badge/language-java-red.svg" alt="Language: Java">
  </a>
</p>

### 🤝 Collaborators

Thanks to these amazing people for their contributions:

<table align="center">
  <tr>
    <td align="center">
      <a href="https://github.com/ThibazaMlambo">
        <img src="https://github.com/ThibazaMlambo.png" width="80px;" alt="The ThibazaMlambo"/>
        <br /><sub><b>Thibaza Mlambo</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/CrispenGari">
        <img src="https://github.com/CrispenGari.png" width="80px;" alt="Crispen Gari"/>
        <br /><sub><b>Crispen Gari</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/DbyProsper">
        <img src="https://github.com/DbyProsper.png" width="80px;" alt="The DbyProsper"/>
        <br /><sub><b>Prosper Masuku</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/PNomnga">
        <img src="https://github.com/PNomnga.png" width="80px;" alt="The PNomnga"/>
        <br /><sub><b>P. Nomnga</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Kingsleyxelo">
        <img src="https://github.com/Kingsleyxelo.png" width="80px;" alt="The Kingsleyxelo"/>
        <br /><sub><b>Kingsleyxelo</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/siphocraig">
        <img src="https://github.com/siphocraig.png" width="80px;" alt="The siphocraig"/>
        <br /><sub><b>siphocraig</b></sub>
      </a>
    </td>
  
  </tr>
</table>

### Tech Stack

We used teh following technologies in this project:

1. `Postgres` - As the database.

   - We created a database called `csc` that will hold the table named `course`.
   - The SQL command was used:

   ```sql
   CREATE DATABASE csc;
   ```

2. `Spingboot` - Framework for creating web APIs.

   - For this we use the [`start.spring.io`](https://start.spring.io/) and here are the list of dependencies that was added to the project.

   - `web`
   - `postgresql`
   - `data-jpa`
   - `validation`
   - `lombok`

> Note that we are using `Java 17` and `marven` as a dependency manager. Our `application.properties` for this project was configured to be:

```yml
# postgres sql connection
spring:
  devtools:
    restart:
      enabled: true
      additional-paths: src/main/java
      exclude: static/**,public/**
  datasource:
    password: postgres
    url: jdbc:postgresql://localhost:5432/csc
    username: postgres
  jpa:
    hibernate:
      ddl-auto: create #create
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    show-sql: true
# Configuring the port for our server
server:
  port: 3001
```

### Course Table

We created a course table so that it will have the following fields:

1.  `id` -> which is an automatically generated column for each and every course that will be added
2.  `name` -> the course name which is a string of size `3` e.g. `CSC`
3.  `instruction` -> course instructions.
4.  `title` - course title.
5.  `purpose` -> the purpose of the course.
6.  `assessments` -> Course assessment details.
7.  `content` -> Summary of course `content`
8.  `credits` -> course credits which are `16`, `8` and `12`.
9.  `code` -> a number which is the corse code and it should be a number of size `3` e.g. `112`
10. `displayName` -> which is the full display name for the course e.g. `CSC 113F`, `CSC 113` and should be unique in the database.
11. `category` - the category of the course which was created as a java Enum type of [`HONORS`, `UNDERGRADUATE`, `FOUNDATION`] and in code it looks as follows:
    ```java
    import lombok.Getter;
    @Getter
    public enum Category {
           FOUNDATION("FOUNDATION"),
           HONORS("HONORS"),
           UNDERGRADUATE("UNDERGRADUATE");
           private final String category;
           Category(String category){
               this.category =category;
           }
    }
    ```
    The whole table implementation in code will look as follows:

```java
package com.example.SpringAssignment1.courses;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name="courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Serializable {

   @GeneratedValue(strategy = GenerationType.AUTO)
   @Id
   private Long id;

   @Column(nullable = false, length = 3, name = "name")
   private String name;

   @Column(nullable = false, name = "code")
   private int code;

   @Column(nullable = false, name = "credits")
   private int credits;

   @Column(nullable = false, name="displayName", unique = true, length = 8)
   private  String displayName;

   @Column(nullable = false, name="title")
   private  String title;

   @Column(nullable = false, name = "category")
   private Category category;

   @Column(nullable = false, name = "purpose", length = 500)
   private String purpose;

   @Column(nullable = false, name = "content", length = 500)
   private String content;

   @Column(nullable = false, name = "instruction", length = 500)
   private String instruction;

   @Column(nullable = false, name = "assessment")
   private String assessment;
}

```

> The sql that was generated for creating the table is:

```sql
  create table courses (
        category smallint not null check (category between 0 and 2),
        code integer not null,
        credits integer not null,
        name varchar(3) not null,
        display_name varchar(8) not null unique,
        id bigint not null,
        content varchar(500) not null,
        instruction varchar(500) not null,
        purpose varchar(500) not null,
        assessment varchar(255) not null,
        title varchar(255) not null,
        primary key (id)

```

After creating the `Course` class we created the `CourseRepository` which extends from `JpaRepository` with the table of `Course` and the `Id` of the this table `Course`. We are also going to create another function that will help us to query the course from the database using `jpa` called `findByDisplayName`.

```java
package com.example.SpringAssignment1.courses;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByDisplayName(String displayName);
    Collection<Course> findByCategory(Category category);
}
```

We created another interface called `CourseServiceInterface` which defines the methods that we will implement in on the course. Basically we are performing the `CRUD` operations on the `course` table.

```java
package com.example.SpringAssignment1.courses;
import java.util.Collection;

public interface CourseServiceInterface {
    Course addCourse(Course Course);
    Course getCourse(Long id);
    Boolean removeCourse(Long id);
    Course updateCourse(Course course);
    Collection<Course> getCourses();
    Boolean isCourseAvailable(String displayName);
    Collection<Course> getGroupedCourses(Category category);
}

```

Then following a `CourseService` class that does everything that has to do with `Creating`, `Reading`, `Updating`, `Deleting` of course.

```java
package com.example.SpringAssignment1.courses;
import com.example.SpringAssignment1.exceptions.CourseNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

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
    public Collection<Course> getGroupedCourses(Category category) {
        return this.repository.findByCategory(category).stream().toList();
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

```

### Validations of User Input.

For the input validations we used the [`SpringBoot Validation`](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-validation.html) and here is the step by step implementation of the error handling. First we created the `CourseController` class:

```java

@RestController
@RequestMapping(path = "/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;
    @PostMapping(path = "/add")
    public ResponseEntity<Course> addCourse(@Valid @RequestBody AddCourseBody body) throws Exception {
        Course course = new Course();
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
```

And we handle the exceptions when the http status `BadRequest` is triggered. `AddCourseBody` is a type of json body that we expect when creating a course. It has the following code in it.

```java
package com.example.SpringAssignment1.types;
import com.example.SpringAssignment1.courses.Category;
import com.example.SpringAssignment1.validators.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseBody {
   @Valid
   @NotNull(message = "The course code is required.")
   @CourseCodeValidator
   private int code;

   @NotNull(message = "The course credits are required.")
   @CourseCreditValidator
   private int credits;

   @NotNull(message = "The course name is required.")
   @Size(max = 3, min = 3, message = "The course name must have exactly 3 characters.")
   private String name;

   @NotNull(message = "The course title is required.")
   @Size(message = "The title name must be provided.")
   private String title;

   @NotNull(message = "The course purpose is required.")
   @Size(message = "The purpose name must be provided.")
   private String purpose;

   @NotNull(message = "The course content is required.")
   @Size(message = "The content name must be provided.")
   private String content;

   @NotNull(message = "The course instruction is required.")
   @Size(message = "The instruction name must be provided.")
   private String instruction;

   @NotNull(message = "The course assessment is required.")
   @Size(message = "The assessment name must be provided.")
   private String assessment;

   @NotNull(message = "The course category is required.")
   @CourseCategoryValidator
   private Category category;

   @NotNull(message = "The course description is required.")
   @Size(max = 500, min = 10, message = "The course description must have at least 10 characters and at most 500 characters.")
   private String description;
}


```

We have `3` custom validators for our input which are located in the `validators` package. They helps us to implement our custom error checking on the course code and course category. Here is the code for the annotations.

1. `@CourseCreditValidator`

The first thing is we want to validate the course credits based on what have been observed in the prospectus which are one of the values `[8, 12, 16, 15, 50]`.

```java
package com.example.SpringAssignment1.validators;
import jakarta.validation.*;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomCourseCreditValidator.class)
public @interface CourseCreditValidator {
    String message() default  "Invalid course credit, the course credits that are available are [8, 12, 16, 15, 50].";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


```

In the implementation of the `CustomCourseCreditValidator` class we have the following.

```java
package com.example.SpringAssignment1.validators;
import jakarta.validation.*;
public class CustomCourseCreditValidator implements ConstraintValidator<CourseCreditValidator, Integer> {

    @Override
    public void initialize(CourseCreditValidator constraintAnnotation) {
    }
    @Override
    public boolean isValid(Integer credits, ConstraintValidatorContext context) {
        return credits == 8 || credits == 12 || credits == 16 || credits == 50 || credits == 15;
    }
}

```

2. `@CourseCategoryValidator`

```java
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomCourseCategoryValidator.class)
public @interface CourseCategoryValidator {
        String message() default "Invalid course category, categories can only be ['HONORS', 'UNDERGRADUATE', 'FOUNDATION'].";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}

```

In the validation implementation we are required to check if the course category are valid.

```java
import com.example.SpringAssignment1.courses.Category;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomCourseCategoryValidator implements ConstraintValidator<CourseCategoryValidator, Category> {

    @Override
    public void initialize(CourseCategoryValidator constraintAnnotation) {
    }
    @Override
    public boolean isValid(Category category, ConstraintValidatorContext context) {
        return category.getCategory().equalsIgnoreCase(Category.HONORS.toString()) ||
                category.getCategory().equalsIgnoreCase(Category.FOUNDATION.toString()) ||
                category.getCategory().equalsIgnoreCase(Category.UNDERGRADUATE.toString());
    }
}
```

Same applies to the course codes.

```java
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomCourseCodeValidator.class)
public @interface CourseCodeValidator {
        String message() default  "Invalid course code, the course code must contain exactly 3 digit numbers.";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}

```

Again we check if the payload is valid.

```java
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomCourseCodeValidator implements ConstraintValidator<CourseCodeValidator, Integer> {
    @Override
    public void initialize(CourseCodeValidator constraintAnnotation) {
    }
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return Integer.toString(value).length() == 3;
    }
}
```

Errors from the API are returned as a list. Here is an example of errors payload when the invalid input is given:

```json
[
  {
    "field": "name",
    "name": "The course name must have exactly 3 characters.",
    "timestamp": "Sat Mar 22 08:19:02 SAST 2025"
  },
  {
    "code": "Invalid course code, the course code must contain exactly 3 digit numbers.",
    "field": "code",
    "timestamp": "Sat Mar 22 08:19:02 SAST 2025"
  }
]
```

> Exceptions can also be handled globally. Here are the step by step implementations of the global exceptional handling.

### Global Exceptional Handler

We created a general Exception capture, that captures all the exception that are going to be thrown in this app. Because when data validation doesn't match what we will throw the exceptions and then we catch them within the `GlobalExceptionHandler` which looks as follows:

```java
package com.example.SpringAssignment1.exceptions;
import com.example.SpringAssignment1.types.ErrorType;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;
@ControllerAdvice
public class GlobalExceptionHandler {

//    Handling specific exception
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<?> courseNotFound(CourseNotFoundException exception, WebRequest request){
        ArrayList<ErrorType> errors = new ArrayList<>();
        errors.add(
                new ErrorType(
                        new Date(), exception.getMessage(),
                    request.getDescription(false), "course"
            )
        );
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CourseAlreadyExistsException.class)
    public ResponseEntity<?> courseAlreadyExists(CourseAlreadyExistsException exception, WebRequest request){
        ArrayList<ErrorType> errors = new ArrayList<>();
        errors.add(
                new ErrorType(
                        new Date(), exception.getMessage(),
                        request.getDescription(false), "course"
                )
        );
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<?> errorParsingDataExists(CourseAlreadyExistsException exception, WebRequest request){
        ArrayList<ErrorType> errors = new ArrayList<>();
        errors.add(
                new ErrorType(
                        new Date(), exception.getMessage(),
                        request.getDescription(false), "course"
                )
        );
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        ArrayList<Map<String,String>> errors = new ArrayList<Map<String, String>>();
        Map<String, String> e = new HashMap<>();
        String field = "body";
        String message = exception.getMessage();
        e.put(field, message);
        e.put("timestamp", new Date().toString());
        e.put("field", field);
        errors.add(e);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(NoResourceFoundException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Resource not found");
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("timestamp", new Date());
        errorDetails.put("path", ex.getResourcePath());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //    Global exception handling
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request){
//        return new ResponseEntity<>(
//                new ErrorType(new Date(), exception.getMessage(), request.getDescription(false))
//                , HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}

```

A `CourseNotFoundException` is just a custom exception that get thrown when we did not find the course:

```java
public class CourseNotFoundException extends RuntimeException{
    public  CourseNotFoundException(String message){
        super(message);
    }
}
```

And the `CourseAlreadyExistsException` looks as follows:

```java

public class CourseAlreadyExistsException extends  Exception{
    public CourseAlreadyExistsException(String message){
        super(message);
    }
}

```

And an `ErrorType` is just a custom type of error that have the properties:

1. `timestamp` - the time when the error was thrown
2. `message` - the message of the error.
3. `details` - the details of the error.

```java
import lombok.*;
import java.util.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorType {
    private Date timestamp;
    private String message;
    private String details;
}
```

### Course Controller

A lot of magic is held in the heart of the API which is the `CourseController` class. This is where http request method are being defined to match the routes we expect from the user.

```java

package com.example.SpringAssignment1.courses;
import com.example.SpringAssignment1.exceptions.CourseAlreadyExistsException;
import com.example.SpringAssignment1.types.AddCourseBody;
import com.example.SpringAssignment1.types.UpdateCourseBody;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.jpa.repository.Query;
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

        System.out.println(body.getCategory());

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
    public ResponseEntity<Course> getCourse(@PathVariable("courseId") Long courseId){
        return ResponseEntity.status(200).body(this.service.getCourse(courseId));
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<Collection<Course>> getCoursesByCategory(
            @PathVariable("category") String category
    ) {
        if(category.equalsIgnoreCase("undergraduate")){
            return ResponseEntity.status(200).body(this.service.getGroupedCourses(Category.UNDERGRADUATE));
        }else if(category.equalsIgnoreCase("honours")){
            return ResponseEntity.status(200).body(this.service.getGroupedCourses(Category.HONOURS));
        }else if(category.equalsIgnoreCase("foundation")){
            return ResponseEntity.status(200).body(this.service.getGroupedCourses(Category.FOUNDATION));
        }else{
            return ResponseEntity.status(200).body(this.service.getCourses());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Collection<Course>>> getCourses(
            @RequestParam(required = false) boolean group
    ){
        if(group){
            Map<String, Collection<Course>> courses = new HashMap<>();
            courses.put("UNDERGRADUATE", this.service.getGroupedCourses(Category.UNDERGRADUATE));
            courses.put("HONOURS", this.service.getGroupedCourses(Category.HONOURS));
            courses.put("FOUNDATION", this.service.getGroupedCourses(Category.FOUNDATION));
            System.out.println(courses);
            return ResponseEntity.status(200).body(courses);
        }
        return ResponseEntity.status(200).body(
                Map.of("ALL", this.service.getCourses())
        );
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

```

### API `Request` and `Response`:

Here are the available routes

1. `GET` = `http://localhost:3001/api/v1/courses/all` - getting all the courses

```json
{
  "ALL": [
    {
      "id": 1,
      "name": "CSC",
      "code": 113,
      "credits": 16,
      "displayName": "CSC 113",
      "title": "Introduction to Computing and Programming Concepts",
      "category": "UNDERGRADUATE",
      "purpose": "Intended for students who have no previous knowledge of computers and wish to obtain a basic understanding of Compute Science",
      "content": "Theory: Uses of computers; components of a computer, processor, memory, input devices, output devices; theoretical aspects of word processors, spreadsheets, and databases; computer networks and the Internet; an introduction to basic HTML, algorithms, basic programming concepts using Visual Basic for Applications Practical: Use of the operating system, the file system, word processing, spreadsheets, MS Access, the World Wide Web and electronic mail, programming using Visual Basic for Applications",
      "instruction": "180 minutes per week of lectures; 120 minutes per week of formal practicals; Self study",
      "assessment": "Continuous assessment based on tests, assignments and monitored cal work, as well as one three-hour final examination as summative assessment."
    }
  ]
}
```

2. `GET` = `http://localhost:3001/api/v1/courses/all?group=true` - getting the grouped coursed based on the category.

```json
{
  "HONOURS": [],
  "FOUNDATION": [],
  "UNDERGRADUATE": []
}
```

3. `GET` = `http://localhost:3001/api/v1/courses/category/{category}` - getting categorized courses in categories [`foundation`, `undergraduate`, `honours`].

```json
[
  {
    "id": 3,
    "name": "CSC",
    "code": 121,
    "credits": 16,
    "displayName": "CSC 121F",
    "title": "Elementary Computer Programming",
    "category": "FOUNDATION",
    "purpose": "Introduce computer programming and algorithmics using a programming language such as Visual Basic, C++ or Java.",
    "content": "The programming environment; variables and declarations; conditional statements; file I/O; use of functions and procedures; repetition using the For and While loop; the ASCII character set; arrays; structures / records; graphics; design of algorithms.",
    "instruction": "180 minutes per week of lectures; 120 minutes per week of formal practicals; Self study",
    "assessment": "Continuous assessment based on tests, assignments and monitored cal work, as well as one three-hour final examination as summative assessment."
  },
  {
    "id": 4,
    "name": "CSC",
    "code": 113,
    "credits": 16,
    "displayName": "CSC 113F",
    "title": "Introduction to Computing and Programming Concepts",
    "category": "FOUNDATION",
    "purpose": "Intended for students who have no previous knowledge of computers and wish to obtain a basic understanding of Compute Science",
    "content": "Theory: Uses of computers; components of a computer, processor, memory, input devices, output devices; theoretical aspects of word processors, spreadsheets, and databases; computer networks and the Internet; an introduction to basic HTML, algorithms, basic programming concepts using Visual Basic for Applications Practical: Use of the operating system, the file system, word processing, spreadsheets, MS Access, the World Wide Web and electronic mail, programming using Visual Basic for Applications",
    "instruction": "180 minutes per week of lectures; 120 minutes per week of formal practicals; Self study",
    "assessment": "Continuous assessment based on tests, assignments and monitored cal work, as well as one three-hour final examination as summative assessment."
  }
]
```

2. `GET` = `http://localhost:3001/api/v1/courses/course/1` - getting the course with id `1`.
3. `POST` = `http://localhost:3001/api/v1/courses/add` - adding a new course, with the request body that looks as follows:
   ```json
   {
     "name": "CSC",
     "code": 517,
     "credits": 15,
     "title": "Theory of Computing",
     "category": "HONOURS",
     "purpose": "To introduce the various theoretical computing machines and formal language types, and to investigate the limits of computability",
     "content": "AFinite state automata (deterministic and nondeterministic), pushdown automata, Turing machines, formal languages, computability and complexity, the classes P, NP and NPC.",
     "instruction": "20 hours of lectures",
     "assessment": "Continuous assessment based on tests, assignments and practical work, as well as one three-hour final examination as summative assessment."
   }
   ```
4. GET = `/` => The default route that renders an html about the `API`.
5. `PUT` = `http://localhost:3001/api/v1/courses/update/1` - update an existing course, with the request body that must looks as follows:

   ```json
   {
   "id": 1,
   "name": "CSC",
   "code": 113,
   "credits": 16,
   "displayName": "CSC 113",
   "title": "Introduction to Computing and Programming Concepts",
   "category": "UNDERGRADUATE",
   "purpose": "Intended for students who have no previous knowledge of computers and wish to obtain a basic understanding of Compute Science",
   "content": "Theory: Uses of computers; components of a computer, processor, memory, input devices, output devices; theoretical aspects of word processors, spreadsheets, and databases; computer networks and the Internet; an introduction to basic HTML, algorithms, basic programming concepts using Visual Basic for Applications Practical: Use of the operating system, the file system, word processing, spreadsheets, MS Access, the World Wide Web and electronic mail, programming using Visual Basic for Applications",
   "instruction": "180 minutes per week of lectures; 120 minutes per week of formal practicals; Self study",
   "assessment": "Continuous assessment based on tests, assignments and monitored cal work, as well as one three-hour final examination as summative assessment."
   },
   ```

6. `DELETE`= `http://localhost:3001/api/v1/courses/update/1` - deletes a course from the existing courses.

### Cross Recourse Origin Sharing (`CORS`).

Next we are going to configure `CORS` for our api to be used with other domains. In the `SpringAssignment1Application` class we are going to add the following configuration as a `Bean` as follows:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
@RestController
public class SpringAssignment1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringAssignment1Application.class, args);
	}
	@GetMapping
	public String hi(){
		return "<p>Hello, welcome to our web API for Computer Science Department Courses. See all courses <a href='/api/v1/courses/all'>HERE</a>.</p>";
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3002", "*"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Filename"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
```

The following origins will be allowed to access the API:

1. `"http://localhost:3000"`
2. `"http://localhost:3002"`,
3. `"*"` - For everyone who has the api link
