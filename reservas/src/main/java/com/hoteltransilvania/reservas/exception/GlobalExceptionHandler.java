package com.hoteltransilvania.reservas.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) //Cuando Exista un error de Validacion ejecuta el siguiente Metodo
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>(); //Es el modelo de como se muestra el error Ejemplo "nombre":"El nombre no puede estar vacio"

        // Recorremos todos los errores que encontró Spring
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEntry(org.springframework.dao.DataIntegrityViolationException ex) {
        Map<String, String> errores = new HashMap<>();
        
        // Aquí personalizamos el mensaje
        errores.put("error", "Conflicto de datos");
        errores.put("mensaje", "Error de integridad: Ya existe un registro con estos datos únicos en el sistema");
        
        // Devolvemos un 409 Conflict o un 400 Bad Request
        return new ResponseEntity<>(errores, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoDuplicado(DuplicateResourceException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Conflicto");
        error.put("mensaje", ex.getMessage()); 
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(org.springframework.web.client.HttpClientErrorException.NotFound.class)
    public ResponseEntity<Map<String, String>> manejarNoEncontradoExterno() {
        Map<String, String> errores = new HashMap<>();
        errores.put("error", "No encontrado");
        errores.put("mensaje", "El recurso solicitado en el otro microservicio no existe");
        return new ResponseEntity<>(errores, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(ResourceNotFoundException ex) {
        Map<String, String> errores = new HashMap<>();
        errores.put("error", "Recurso no encontrado");
        errores.put("mensaje", ex.getMessage());
        return new ResponseEntity<>(errores, HttpStatus.NOT_FOUND);
    }   

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarRuntime(RuntimeException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "Error de Validación");
        cuerpo.put("mensaje", ex.getMessage()); // Aquí saldrá tu "La Habitación no existe"
        
        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_REQUEST);
    }
}
