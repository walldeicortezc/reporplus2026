package com.curso.reporplus.api.error;

import com.curso.reporplus.application.RecursoDuplicadoException;
import com.curso.reporplus.application.RecursoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiError> naoEncontrado(
            RecursoNaoEncontradoException exception, HttpServletRequest request) {
        return resposta(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ApiError> conflito(
            RecursoDuplicadoException exception, HttpServletRequest request) {
        return resposta(HttpStatus.CONFLICT, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacao(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fields.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return resposta(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos são inválidos",
                request,
                fields);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> jsonMalformado(
            HttpMessageNotReadableException exception, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, "JSON inválido", request, Map.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> regraInvalida(
            IllegalArgumentException exception, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, exception.getMessage(), request, Map.of());
    }

    private ResponseEntity<ApiError> resposta(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            Map<String, String> fields) {
        ApiError error = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fields);
        return ResponseEntity.status(status).body(error);
    }
}
