package com.example.SpringAssignment1.exceptions;

public class CourseNotFoundException extends RuntimeException{
    public  CourseNotFoundException(String message){
        super(message);
    }
}
