package com.example.SpringAssignment1.types;
import com.example.SpringAssignment1.courses.Category;
import com.example.SpringAssignment1.validators.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseBody {
    @Valid
    @NotNull(message = "The course code is required.")
    @CourseCodeValidator
    private int code;

    @NotNull(message = "The course credits are required.")
    @CourseCreditValidator
    private int credits;

    @NotNull(message = "The course name is required.")
    @Size(max = 3, min = 3, message = "The course name must have exactly 3 characters.")
    private String name;

    @NotNull(message = "The course title is required.")
    @Size(message = "The title name must be provided.")
    private String title;

    @NotNull(message = "The course purpose is required.")
    @Size(message = "The purpose name must be provided.")
    private String purpose;

    @NotNull(message = "The course content is required.")
    @Size(message = "The content name must be provided.")
    private String content;

    @NotNull(message = "The course instruction is required.")
    @Size(message = "The instruction name must be provided.")
    private String instruction;

    @NotNull(message = "The course assessment is required.")
    @Size(message = "The assessment name must be provided.")
    private String assessment;

    @NotNull(message = "The course category is required.")
    @CourseCategoryValidator
    private Category category;

    @NotNull(message = "The course description is required.")
    @Size(max = 500, min = 10, message = "The course description must have at least 10 characters and at most 500 characters.")
    private String description;
}
