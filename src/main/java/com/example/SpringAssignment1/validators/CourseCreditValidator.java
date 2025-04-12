package com.example.SpringAssignment1.validators;
import jakarta.validation.*;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomCourseCreditValidator.class)
public @interface CourseCreditValidator {
    String message() default  "Invalid course credit, the course credits that are available are [8, 12, 16].";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
