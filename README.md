### SpringAssignment Group 15

🕸️ Introduction to web APIs using Spring and Java Assignment. Group Assignment. This read me file will detail all the code explanation and how we go about solving the assignment problem. This assignment was for testing Java Developers their capability in using Springboot to build a basic `CRUD` `REST` Web `API` with error handling.

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
3.  `description` -> course description.
4.  `code` -> a number which is the corse code and it should be a number of size `3` e.g. `112`
5.  `displayName` -> which is the full display name for the course e.g. `CSC 113F`, `CSC 113` and should be unique in the database.
6.  `category` - the category of the course which was created as a java Enum type of [`HONORS`, `UNDERGRADUATE`, `FOUNDATION`] and in code it looks as follows:
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

    @Column(nullable = false, name="displayName", unique = true, length = 6)
    private  String displayName;

    @Column(nullable = false, name = "category")
    private Category category;

    @Column(nullable = false, name = "description", length = 500)
    private String description;
}
```

> The sql that was generated for creating the table is:

```sql
create table courses (
    category smallint not null check (category between 0 and 2),
    code integer not null,
    name varchar(3) not null,
    display_name varchar(6) not null unique,
    id bigint not null,
    description varchar(500) not null,
    primary key (id)
)
```

After creating the `Course` class we created the `CourseRepository` which extends from `JpaRepository` with the table of `Course` and the `Id` of the this table `Course`. We are also going to create another function that will help us to query the course from the database using `jpa` called `findByDisplayName`.

```java
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByDisplayName(String displayName);

}
```

We created another interface called `CourseServiceInterface` which defines the methods that we will implement in on the course. Basically we are performing the `CRUD` operations on the `course` table.

```java
import java.util.Collection;

public interface CourseServiceInterface {
    Course addCourse(Course Course);
    Course getCourse(Long id);
    Boolean removeCourse(Long id);
    Course updateCourse(Course course);
    Collection<Course> getCourses();
}

```

Then following a `CourseService` class that does everything that has to do with `Creating`, `Reading`, `Updating`, `Deleting` of course.

```java
import com.example.SpringAssignment1.exceptions.CourseNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;


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
import com.example.SpringAssignment1.validators.CourseCategoryValidator;
import com.example.SpringAssignment1.validators.CourseCodeValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseBody {

    @Valid
    @NotNull(message = "The course code is required.")
    @CourseCodeValidator
    private int code;

    @NotNull(message = "The course name is required.")
    @Size(max = 3, min = 3, message = "The course name must have exactly 3 characters.")
    private String name;


    @NotNull(message = "The course category is required.")
    @CourseCategoryValidator
    private Category category;

    @NotNull(message = "The course description is required.")
    @Size(max = 500, min = 10, message = "The course description must have at least 10 characters and at most 500 characters.")
    private String description;
}

```

We have `2` custom validators for our input which are located in the `validators` package. They helps us to implement our custom error checking on the course code and course category. Here is the code for the annotations.

1. `@CourseCategoryValidator`

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

Same applies to teh course codes.

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
@ControllerAdvice
public class GlobalExceptionHandler {

//    Handling specific exception
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<?> courseNotFound(CourseNotFoundException exception, WebRequest request){
        ErrorType errorType = new ErrorType(new Date(), exception.getMessage(),
                request.getDescription(false), "course"
        );
        return new ResponseEntity<>(errorType, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CourseAlreadyExistsException.class)
    public ResponseEntity<?> courseAlreadyExists(CourseAlreadyExistsException exception, WebRequest request){
        ErrorType errorType = new ErrorType(new Date(), exception.getMessage(),
                request.getDescription(false), "course"
        );
        return new ResponseEntity<>(errorType, HttpStatus.NOT_FOUND);
    }


//    //    Global exception handling
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        course.setDescription(body.getDescription());
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
        course.setDescription(body.getDescription());
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

```

### API `Request` and `Response`:

Here are the available routes

1. `GET` = `http://localhost:3001/api/v1/courses/all` - getting all the courses
2. `GET` = `http://localhost:3001/api/v1/courses/course/1` - getting the course with id `1`.
3. `POST` = `http://localhost:3001/api/v1/courses/add` - adding a new course, with the request body that looks as follows:
   ```json
   {
     "name": "CSC",
     "code": 133,
     "category": "FOUNDATION",
     "description": "This is a computer Science Foundation Course."
   }
   ```
4. GET = `/` => The default route that renders an html about the `API`.
5. `PUT` = `http://localhost:3001/api/v1/courses/update/1` - update an existing course, with the request body that must looks as follows:

   ```json
   {
     "name": "CSC",
     "code": 133,
     "category": "FOUNDATION",
     "description": "This is a computer Science Foundation Course."
   }
   ```

6. `DELETE`= `http://localhost:3001/api/v1/courses/update/1` - deletes a course from the existing courses.

Here is the response for all the list of courses.

```json
[
  {
    "id": 1,
    "name": "CSC",
    "code": 133,
    "displayName": "CSC 133",
    "category": "UNDERGRADUATE",
    "description": "This is a computer Science Foundation Course."
  }
]
```

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
