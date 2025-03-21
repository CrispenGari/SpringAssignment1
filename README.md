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

After creating the `Course` class we created the `CourseRepository` which extends from `JpaRepository` with the table of `Course` and the `Id` of the this table `Course`.

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
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

We created a general Exception capture, that captures all the exception that are going to be thrown in this app. Because when data validation doesn't match what we will throw the exceptions and then we catch them within the `GlobalExceptionHandler` which looks as follows:

```java
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
    public ResponseEntity<?> animalNotFound(CourseNotFoundException exception, WebRequest request){
        ErrorType errorType = new ErrorType(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorType, HttpStatus.NOT_FOUND);
    }

    //    Global exception handling
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request){
        return new ResponseEntity<>(
                new ErrorType(new Date(), exception.getMessage(), request.getDescription(false))
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
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

A lot of magic is held in the heart of the API which is the `CourseController` class. This is where http request method are being defined to match the routes we expect from the user.

```java

```

### API Response:

```json
[
  {
    "id": 1,
    "name": "CSC",
    "code": 113,
    "displayName": "CSC 113",
    "category": "HONORS"
  },
  {
    "id": 2,
    "name": "CSC",
    "code": 113,
    "displayName": "CSC 113F",
    "category": "FOUNDATION"
  }
]
```
