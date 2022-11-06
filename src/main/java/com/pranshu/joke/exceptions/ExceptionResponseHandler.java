package com.pranshu.joke.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<HttpStatus> handleUnauthorizedUserException(UnauthorizedUserException exception) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<HttpStatus> handleInvalidRequestException(InvalidRequestException exception) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceUnavailableException.class)
    public ResponseEntity<HttpStatus> handleResourceUnavailableException(ResourceUnavailableException exception) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
