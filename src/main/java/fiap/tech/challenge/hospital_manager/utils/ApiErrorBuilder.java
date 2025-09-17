package fiap.tech.challenge.hospital_manager.utils;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import fiap.tech.challenge.hospital_manager.controller.exceptions.ApiErrorArray;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Utilitário para construção padronizada de objetos {@link ApiErrorArray}
 * para respostas de erro em APIs REST.
 */
public class ApiErrorBuilder {

    public static ResponseEntity<ApiErrorArray> build(HttpStatus status, String message, HttpServletRequest request) {
        return build(status, List.of(message), request);
    }

    public static ResponseEntity<ApiErrorArray> build(HttpStatus status, List<String> messages,
            HttpServletRequest request) {
        ApiErrorArray error = new ApiErrorArray(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                messages,
                request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}