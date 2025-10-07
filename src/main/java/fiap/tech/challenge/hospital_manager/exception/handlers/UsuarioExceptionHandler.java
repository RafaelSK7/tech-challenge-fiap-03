package fiap.tech.challenge.hospital_manager.exception.handlers;

import fiap.tech.challenge.hospital_manager.exception.custom.UsuarioNaoAutorizadoException;
import fiap.tech.challenge.hospital_manager.utils.ApiErrorBuilder;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler global para exceções relacionadas ao Usuário.
 */
@RestControllerAdvice
public class UsuarioExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioExceptionHandler.class);

    @ExceptionHandler(UsuarioNaoAutorizadoException.class)
    public ResponseEntity<ApiErrorArray> handleUsuarioNaoAutorizado(UsuarioNaoAutorizadoException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorArray> handleUnexpected(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    private ResponseEntity<ApiErrorArray> buildErrorResponse(HttpStatus status, Throwable ex,
            HttpServletRequest request) {
        logger.error("Error [{}] at {}: {}", status.value(), request.getRequestURI(), ex.getMessage(), ex);
        return ApiErrorBuilder.build(status, ex.getMessage(), request);
    }
}