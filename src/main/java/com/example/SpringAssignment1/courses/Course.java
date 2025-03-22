package com.example.SpringAssignment1.courses;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name="courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false, length = 3, name = "name")
    private String name;

    @Column(nullable = false, name = "code")
    private int code;

    @Column(nullable = false, name="displayName", unique = true, length = 8)
    private  String displayName;

    @Column(nullable = false, name = "category")
    private Category category;

    @Column(nullable = false, name = "description", length = 500)
    private String description;
}
