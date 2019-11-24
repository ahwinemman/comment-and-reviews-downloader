package com.globalaccelerex.controller;

import com.globalaccelerex.exception.NoCommentsException;
import com.globalaccelerex.exception.NoReviewsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus( HttpStatus.OK)
    @ExceptionHandler(NoReviewsException.class)
    public Map<String, String> handleNoReviewsException(NoReviewsException e) {
        return Collections.singletonMap("error", "There are no reviews on this page");
    }

    @ResponseStatus( HttpStatus.OK)
    @ExceptionHandler(NoCommentsException.class)
    public Map<String, String> handleNoCommentsException(NoCommentsException e) {
        return Collections.singletonMap("error", "There are no comments attached to this video");
    }
}
