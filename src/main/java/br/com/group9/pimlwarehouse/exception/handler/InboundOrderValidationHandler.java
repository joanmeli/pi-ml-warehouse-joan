package br.com.group9.pimlwarehouse.exception.handler;

import br.com.group9.pimlwarehouse.dto.ErrorMessageDTO;
import br.com.group9.pimlwarehouse.exception.InboundOrderValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InboundOrderValidationHandler {
    @ExceptionHandler(value = InboundOrderValidationException.class)
    protected ResponseEntity<ErrorMessageDTO> handleInboundOrderValidationException(InboundOrderValidationException ex) {
        ErrorMessageDTO error = new ErrorMessageDTO(ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
