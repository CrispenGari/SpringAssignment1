package com.example.SpringAssignment1.courses;
import lombok.Getter;

@Getter
public enum Category {

        FOUNDATION("FOUNDATION"),
        HONORS("HONORS"),
        UNDERGRADUATE("UNDERGRADUATE");

        private final String category;
        Category(String category){
            this.category =category;
        }

}
