package br.com.CineTicket.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Diz ao Spring que esta classe cuida dos erros de TODOS os controllers
public class GlobalExceptionHandler {

    // Sempre que qualquer classe disparar RuntimeException, esse metodo entra em ação
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> tratarResourceNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}