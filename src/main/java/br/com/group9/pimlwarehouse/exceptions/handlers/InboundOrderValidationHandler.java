package br.com.group9.pimlwarehouse.exceptions.handlers;

import br.com.group9.pimlwarehouse.exceptions.InboundOrderValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InboundOrderValidationHandler {
    @ExceptionHandler(value = InboundOrderValidationException.class)
    protected ResponseEntity<Object> handleInboundOrderValidationException(InboundOrderValidationException ex) {
        String bodyOfResponse = ex.getMessage();
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }
}
