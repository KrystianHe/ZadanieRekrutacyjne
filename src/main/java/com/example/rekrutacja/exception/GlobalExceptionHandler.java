package com.example.rekrutacja.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Obsługa błędów walidacji z adnotacji @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        response.put("status", "BŁĄD WALIDACJI");
        response.put("szczegóły", errors);
        
        log.error("Błąd walidacji: {}", errors);
        return ResponseEntity.badRequest().body(response);
    }

    // Obsługa błędów walidacji
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = ex.getConstraintViolations().stream()
            .collect(Collectors.toMap(
                violation -> violation.getPropertyPath().toString(),
                violation -> violation.getMessage(),
                (error1, error2) -> error1
            ));
        
        response.put("status", "BŁĄD WALIDACJI");
        response.put("szczegóły", errors);
        
        log.error("Błąd walidacji: {}", errors);
        return ResponseEntity.badRequest().body(response);
    }

    // Obsługa błędów walidacji
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleBindException(BindException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        response.put("status", "BŁĄD WALIDACJI");
        response.put("szczegóły", errors);
        
        log.error("Błąd walidacji: {}", errors);
        return ResponseEntity.badRequest().body(response);
    }
    
    // Obsługa błędów json
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "BŁĄD FORMATU");
        response.put("szczegóły", "Niepoprawny format danych. Sprawdź składnię JSON.");
        
        log.error("Błąd formatu danych: ", ex);
        return ResponseEntity.badRequest().body(response);
    }
    
    // Obsługa błędów dostępowych bazodanowych
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "BŁĄD BAZY DANYCH");
        response.put("szczegóły", "Problem z bazą danych: " + ex.getMessage());
        
        log.error("Błąd dostępu do bazy danych: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    // Obsługa wszystkich pozostałych wyjątków
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "BŁĄD SYSTEMU");
        response.put("szczegóły", ex.getMessage());
        
        log.error("Nieoczekiwany błąd: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 