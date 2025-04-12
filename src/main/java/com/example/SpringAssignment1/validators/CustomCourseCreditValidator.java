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
