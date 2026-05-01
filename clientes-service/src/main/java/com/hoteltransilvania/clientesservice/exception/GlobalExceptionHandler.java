package com.hoteltransilvania.clientesservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de errores de validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException ex) {

        Map<String, String> error = new HashMap<>();

        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        error.put("error", mensaje);

        return ResponseEntity.badRequest().body(error);
    }

    //  Manejo de errores generales
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> manejarRuntime(RuntimeException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}