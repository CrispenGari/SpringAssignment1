package com.example.SpringAssignment1.types;


import com.example.SpringAssignment1.courses.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseBody {

    private String name;
    private int code;
    private Category category;
    private String description;
}


