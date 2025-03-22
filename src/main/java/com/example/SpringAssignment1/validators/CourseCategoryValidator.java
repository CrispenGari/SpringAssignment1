package com.example.SpringAssignment1.validators;

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
