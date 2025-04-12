package com.example.SpringAssignment1.exceptions;

import com.example.SpringAssignment1.types.ErrorType;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
@ControllerAdvice
public class GlobalExceptionHandler {

//    Handling specific exception
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<?> courseNotFound(CourseNotFoundException exception, WebRequest request){
        ArrayList<ErrorType> errors = new ArrayList<>();
        errors.add(
                new ErrorType(
                        new Date(), exception.getMessage(),
                    request.getDescription(false), "course"
            )
        );
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CourseAlreadyExistsException.class)
    public ResponseEntity<?> courseAlreadyExists(CourseAlreadyExistsException exception, WebRequest request){
        ArrayList<ErrorType> errors = new ArrayList<>();
        errors.add(
                new ErrorType(
                        new Date(), exception.getMessage(),
                        request.getDescription(false), "course"
                )
        );
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<?> errorParsingDataExists(CourseAlreadyExistsException exception, WebRequest request){
        ArrayList<ErrorType> errors = new ArrayList<>();
        errors.add(
                new ErrorType(
                        new Date(), exception.getMessage(),
                        request.getDescription(false), "course"
                )
        );
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }



    //    Global exception handling
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request){
//        return new ResponseEntity<>(
//                new ErrorType(new Date(), exception.getMessage(), request.getDescription(false))
//                , HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
