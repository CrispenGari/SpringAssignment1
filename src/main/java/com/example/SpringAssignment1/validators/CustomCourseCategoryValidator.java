package com.example.SpringAssignment1.validators;
import com.example.SpringAssignment1.courses.Category;
import jakarta.validation.*;
public class CustomCourseCategoryValidator implements ConstraintValidator<CourseCategoryValidator, Category> {

    @Override
    public void initialize(CourseCategoryValidator constraintAnnotation) {
    }
    @Override
    public boolean isValid(Category category, ConstraintValidatorContext context) {
        return category.getCategory().equalsIgnoreCase(Category.HONOURS.toString()) ||
                category.getCategory().equalsIgnoreCase(Category.FOUNDATION.toString()) ||
                category.getCategory().equalsIgnoreCase(Category.UNDERGRADUATE.toString());
    }
}
