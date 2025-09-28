package fiap.tech.challenge.hospital_manager.exception.handlers;

import fiap.tech.challenge.hospital_manager.exception.custom.PacienteNotFoundException;
import fiap.tech.challenge.hospital_manager.controller.exceptions.ApiErrorArray;
import fiap.tech.challenge.hospital_manager.utils.ApiErrorBuilder;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler global para exceções relacionadas ao domínio de Pacientes.
 */
@RestControllerAdvice
public class PacienteExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(PacienteExceptionHandler.class);

    @ExceptionHandler(PacienteNotFoundException.class)
    public ResponseEntity<ApiErrorArray> handlePacienteNotFound(PacienteNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorArray> handleUnexpected(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    private ResponseEntity<ApiErrorArray> buildErrorResponse(HttpStatus status, Exception ex, HttpServletRequest request) {
        logger.error("Error [{}] at {}: {}", status.value(), request.getRequestURI(), ex.getMessage(), ex);
        return ApiErrorBuilder.build(status, ex.getMessage(), request);
    }
}
