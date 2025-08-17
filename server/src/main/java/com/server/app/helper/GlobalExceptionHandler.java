package com.server.app.helper;

import com.server.app.enums.ResultMessages;
import com.server.app.helper.results.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<GenericResponse> handleBusinessException(BusinessException exception) {
        log.warn("Business rule violated: {}", exception.getMessage());

        return ResponseEntity.badRequest().body(
                GenericResponse.builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleGenericException(Exception exception) {
        log.error("Unexpected error occurred", exception);

        return ResponseEntity.internalServerError().body(
                GenericResponse.builder()
                        .success(false)
                        .message(ResultMessages.PROCESS_FAILED)
                        .build()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<GenericResponse> handleNullPointerException(NullPointerException exception) {
        log.error("NullPointerException error occurred", exception);

        return ResponseEntity.badRequest().body(
                GenericResponse.builder()
                        .success(false)
                        .message(ResultMessages.NULL_POINTER_REFERENCE)
                        .build()
        );
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<GenericResponse> handleClassCastException(ClassCastException exception) {
        log.error("ClassCastException error occurred", exception);

        return ResponseEntity.badRequest().body(
                GenericResponse.builder()
                        .success(false)
                        .message(ResultMessages.VALUES_NOT_MATCHED)
                        .build()
        );
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<GenericResponse> handleTypeMismatchException(TypeMismatchException exception) {
        log.error("TypeMismatchException error occurred", exception);

        return ResponseEntity.badRequest().body(
                GenericResponse.builder()
                        .success(false)
                        .message(ResultMessages.ILLEGAL_ELEMENT_DELIVERED)
                        .build()
        );
    }
}