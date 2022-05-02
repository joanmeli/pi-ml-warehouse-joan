package br.com.group9.pimlwarehouse.exception.handler;

import br.com.group9.pimlwarehouse.dto.ErrorMessageDTO;
import br.com.group9.pimlwarehouse.exception.UnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class APIValidationHandler {
    @ExceptionHandler(value = UnavailableException.class)
    protected ResponseEntity<ErrorMessageDTO> handleUnavailableException(UnavailableException ex) {
        ErrorMessageDTO error = new ErrorMessageDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
}
