package org.adminBo.exception;

import org.adminBo.wrapper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(
            RuntimeException ex
    ) {

        ApiResponse<Object> response = ApiResponse.builder()
                .status(false)
                .errorCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {

        StringBuilder messages = new StringBuilder();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {

                    String fieldName = ((FieldError) error).getField();

                    String message = error.getDefaultMessage();

                    messages
                            .append(fieldName)
                            .append(": ")
                            .append(message)
                            .append(" | ");
                });

        ApiResponse<Object> response = ApiResponse.builder()
                .status(false)
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .message(messages.toString())
                .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }
}