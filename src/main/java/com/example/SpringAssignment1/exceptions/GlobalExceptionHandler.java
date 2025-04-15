package com.example.SpringAssignment1.exceptions;
import com.example.SpringAssignment1.types.ErrorType;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        ArrayList<Map<String,String>> errors = new ArrayList<Map<String, String>>();
        Map<String, String> e = new HashMap<>();
        String field = "body";
        String message = exception.getMessage();
        e.put(field, message);
        e.put("timestamp", new Date().toString());
        e.put("field", field);
        errors.add(e);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(NoResourceFoundException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Resource not found");
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("timestamp", new Date());
        errorDetails.put("path", ex.getResourcePath());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //    Global exception handling
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request){
//        return new ResponseEntity<>(
//                new ErrorType(new Date(), exception.getMessage(), request.getDescription(false))
//                , HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
