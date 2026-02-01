package com.chaquon.job_scheduler.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIError> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(new APIError("BAD_REQUEST", e.getMessage()));
    }

    @ExceptionHandler(java.sql.SQLException.class)
    public ResponseEntity<APIError> handleSql(SQLException e) {
        return ResponseEntity
                .status(500)
                .body(new APIError("DB_ERROR", "Database operation failed"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleOther(Exception e) {
        return ResponseEntity
                .status(500)
                .body(new APIError("INTERNAL_ERROR", "Unexpected server error"));
    }
}

