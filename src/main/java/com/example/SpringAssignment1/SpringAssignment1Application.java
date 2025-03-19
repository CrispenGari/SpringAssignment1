package com.example.SpringAssignment1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringAssignment1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringAssignment1Application.class, args);
	}
	@GetMapping
	public String hi(){
		return "<p>Hello, welcome to our web API for Computer Science Department Courses. See all courses <a href='/api/v1/courses'>HERE</a>.</p>";
	}

}
