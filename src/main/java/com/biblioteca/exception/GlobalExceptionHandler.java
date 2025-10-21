package com.biblioteca.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());
        
        Map<String, Object> body = createErrorBody(
            HttpStatus.NOT_FOUND, 
            "Recurso no encontrado", 
            ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<Map<String, Object>> handleOperationNotAllowed(OperationNotAllowedException ex) {
        logger.warn("Operación no permitida: {}", ex.getMessage());
        
        Map<String, Object> body = createErrorBody(
            HttpStatus.BAD_REQUEST, 
            "Operación no permitida", 
            ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        logger.warn("Recurso ya existe: {}", ex.getMessage());
        
        Map<String, Object> body = createErrorBody(
            HttpStatus.CONFLICT, 
            "Recurso ya existe", 
            ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        logger.warn("Credenciales inválidas: {}", ex.getMessage());
        
        Map<String, Object> body = createErrorBody(
            HttpStatus.UNAUTHORIZED, 
            "Credenciales inválidas", 
            "Email o contraseña incorrectos"
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        logger.warn("Acceso denegado: {}", ex.getMessage());
        
        Map<String, Object> body = createErrorBody(
            HttpStatus.FORBIDDEN, 
            "Acceso denegado", 
            "No tiene permisos para realizar esta acción"
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn("Error de validación: {}", ex.getMessage());
        
        Map<String, Object> body = createErrorBody(
            HttpStatus.BAD_REQUEST, 
            "Error de validación", 
            "Errores en los datos de entrada"
        );
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        body.put("errors", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("Error interno del servidor: {}", ex.getMessage(), ex);
        
        Map<String, Object> body = createErrorBody(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "Error interno del servidor", 
            "Ocurrió un error inesperado. Por favor, contacte al administrador."
        );
        
        if (isDevelopmentEnvironment()) {
            body.put("debugMessage", ex.getMessage());
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private Map<String, Object> createErrorBody(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return body;
    }

    private boolean isDevelopmentEnvironment() {
        return true;
    }
}