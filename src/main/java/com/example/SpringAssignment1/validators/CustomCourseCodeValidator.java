package com.example.SpringAssignment1.validators;

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
