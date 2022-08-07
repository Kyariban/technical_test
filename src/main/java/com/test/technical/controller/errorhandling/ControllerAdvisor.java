package com.test.technical.controller.errorhandling;

import com.test.technical.controller.errorhandling.exception.InvalidUserException;
import com.test.technical.controller.errorhandling.exception.UserAlreadyExistsException;
import com.test.technical.controller.errorhandling.exception.UserNotFoundException;
import com.test.technical.logging.annotation.LogIOAndExecutionTime;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidUserException.class, UserNotFoundException.class, IllegalArgumentException.class})
    @LogIOAndExecutionTime
    public ResponseEntity<Object> handleInvalidUserException(RuntimeException ex, WebRequest request) {
        return returnResponseEntityWithExceptionData(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @LogIOAndExecutionTime
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        return returnResponseEntityWithExceptionData(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Map<String, Object> body = initializeBodyWithTimestampAndHttpStatus(status);

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> returnResponseEntityWithExceptionData(String message, HttpStatus httpStatus) {
        Map<String, Object> body = initializeBodyWithTimestampAndHttpStatus(httpStatus);
        body.put("message", message);
        return new ResponseEntity<>(body, httpStatus);
    }

    private Map<String, Object> initializeBodyWithTimestampAndHttpStatus(HttpStatus httpStatus) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", httpStatus);
        return body;
    }



}
