package com.example.SpringAssignment1.validators;

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
