package com.pruebaTecnica.PruebaTecnicaLinkTic.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//Clase encargada de manipular las excepciones del sistema de manera global
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExistenciaDeProductoException.class)
    public ResponseEntity<Map<String, Object>> handleExistenciaDeProducto(ExistenciaDeProductoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InventarioInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleInventarioInsuficiente(InventarioInsuficienteException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FondosInsuficientesException.class)
    public ResponseEntity<Map<String, Object>> handleFondosInsuficientes(FondosInsuficientesException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}

