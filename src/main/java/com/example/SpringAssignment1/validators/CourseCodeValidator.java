package com.example.SpringAssignment1.validators;
import jakarta.validation.*;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomCourseCodeValidator.class)
public @interface CourseCodeValidator {
        String message() default  "Invalid course code, the course code must contain exactly 3 digit numbers.";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}
