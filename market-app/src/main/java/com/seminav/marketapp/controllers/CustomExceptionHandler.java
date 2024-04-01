package com.seminav.marketapp.controllers;

import com.seminav.marketapp.exceptions.*;
import com.seminav.marketapp.messages.ExceptionResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;
import java.util.StringJoiner;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<Object> handleUnauthorizedException(Exception e) {
        return handleExceptionInternal(new RuntimeException("No user exists by provided JWT. Authorize"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FilesCanNotBeNullException.class)
    public ResponseEntity<Object> handleUploadingNullFiles(Exception e) {
        return handleExceptionInternal(new RuntimeException("This exception should be caught in app, but smth went wrong..."), HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler({
            DeleteFilesFromStorageException.class,
            UploadFilesException.class
    })
    public ResponseEntity<Object> handleBadParameterException(Exception e) {
        return handleExceptionInternal(e, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({
            ConvertMultipartFileToByteArrayResourceException.class,
            DeleteSavedProductFromFollowersException.class
    })
    public ResponseEntity<Object> handleMultipartFileExceptions(Exception e) {
        return handleExceptionInternal(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            InstanceNotFoundException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<Object> handleNotFoundException(Exception e) {
        return handleExceptionInternal(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            NotEnoughRootsException.class
    })
    public ResponseEntity<Object> handleWrongUserRole(Exception e) {
        return handleExceptionInternal(e, HttpStatus.FORBIDDEN);
    }

        @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e) {
        StringJoiner joiner = new StringJoiner(",");
        e.getConstraintViolations().stream().map(c -> c.getPropertyPath().toString()).forEach(joiner::add);
        return handleExceptionInternal(new RuntimeException(joiner.toString(), e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(ConstraintViolationException e) {
        return handleExceptionInternal(e, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex, Object body, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode, @NonNull WebRequest request
    ) {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, HttpStatus status) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response, status);
    }

}
