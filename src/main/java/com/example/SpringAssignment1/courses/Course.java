package com.example.SpringAssignment1.courses;
import jakarta.persistence.*;
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

    @Column(nullable = false, name = "credits")
    private int credits;

    @Column(nullable = false, name="displayName", unique = true, length = 8)
    private  String displayName;

    @Column(nullable = false, name="title")
    private  String title;

    @Column(nullable = false, name = "category")
    private Category category;

    @Column(nullable = false, name = "purpose", length = 500)
    private String purpose;

    @Column(nullable = false, name = "content", length = 500)
    private String content;

    @Column(nullable = false, name = "instruction", length = 500)
    private String instruction;

    @Column(nullable = false, name = "assessment")
    private String assessment;
}
