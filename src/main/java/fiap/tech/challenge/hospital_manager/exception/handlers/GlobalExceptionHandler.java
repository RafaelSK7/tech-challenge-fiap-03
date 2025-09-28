package fiap.tech.challenge.hospital_manager.exception.handlers;

import fiap.tech.challenge.hospital_manager.controller.exceptions.ApiErrorArray;
import fiap.tech.challenge.hospital_manager.utils.ApiErrorBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manipulador global de exceções para erros técnicos genéricos
 * como validações, violação de constraints e erros de persistência.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<String, String> CONSTRAINT_MESSAGES = Map.of(
            "paciente_id", "Paciente com o ID informado já existe."
    );

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorArray> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {

        List<String> errors = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());

        return ApiErrorBuilder.build(HttpStatus.BAD_REQUEST, errors, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorArray> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ApiErrorBuilder.build(HttpStatus.BAD_REQUEST, errors, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorArray> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        String message = extractConstraintMessage(ex);
        return ApiErrorBuilder.build(HttpStatus.CONFLICT, message, request);
    }

    private String extractConstraintMessage(DataIntegrityViolationException ex) {
        String rawMessage = ex.getMessage();

        if (rawMessage != null && rawMessage.contains("Unique index or primary key violation")) {
            for (Map.Entry<String, String> entry : CONSTRAINT_MESSAGES.entrySet()) {
                if (rawMessage.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }

        return "Violação de integridade de dados";
    }
}