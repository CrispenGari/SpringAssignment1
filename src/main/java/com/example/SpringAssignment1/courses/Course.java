package com.example.SpringAssignment1.courses;
import lombok.*;
import java.io.Serializable;

//@Entity
//@Table(name="courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {
    private String name;
    private int code;
    private Category category;

    @Override
    public String toString() {
        String courseCode =  "" + (this.category.equals(Category.FOUNDATION) ? this.code + "F" :  this.code);
        return (
                "Course{" +
                "name='" + this.name + '\'' +
                ", code='" + courseCode +'\''+
                ", category='" + this.category + '\'' +
                '}'
        );
    }
}
