package com.example.SpringAssignment1.types;
import com.example.SpringAssignment1.courses.Category;
import com.example.SpringAssignment1.validators.CourseCategoryValidator;
import com.example.SpringAssignment1.validators.CourseCodeValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseBody {
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
