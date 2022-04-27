package br.com.group9.pimlwarehouse.handler;

import br.com.group9.pimlwarehouse.exception.ProductNotFoundException;
import br.com.group9.pimlwarehouse.exception.SectionNotFoundException;
import br.com.group9.pimlwarehouse.exception.SectionProductNotFoundException;
import br.com.group9.pimlwarehouse.exception.WarehouseNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WarehouseValidationHandler {

    @ExceptionHandler(WarehouseNotFoundException.class)
    protected ResponseEntity<Object> handleWarehouseNotFoundException(WarehouseNotFoundException exception) {
        String bodyOfResponse = exception.getMessage();
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }
}
