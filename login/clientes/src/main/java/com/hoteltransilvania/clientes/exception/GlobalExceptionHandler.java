package com.hoteltransilvania.clientes.exception;

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
        errores.put("mensaje", "El correo electrónico ya se encuentra registrado en el sistema");
        
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
}
