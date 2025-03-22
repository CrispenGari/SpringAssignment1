package com.example.SpringAssignment1.exceptions;

public class CourseAlreadyExistsException extends  Exception{
    public CourseAlreadyExistsException(String message){
        super(message);
    }
}
