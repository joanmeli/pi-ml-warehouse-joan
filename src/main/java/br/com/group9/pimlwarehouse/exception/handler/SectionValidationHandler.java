package br.com.group9.pimlwarehouse.exception.handler;

import br.com.group9.pimlwarehouse.exception.ProductDoesNotMatchSectionException;
import br.com.group9.pimlwarehouse.exception.ProductNotFoundException;
import br.com.group9.pimlwarehouse.exception.SectionNotFoundException;
import br.com.group9.pimlwarehouse.exception.SectionProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SectionValidationHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    protected ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException exception) {
        String bodyOfResponse = exception.getMessage();
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }
    @ExceptionHandler(SectionNotFoundException.class)
    protected ResponseEntity<Object> handleSectionNotFoundException(SectionNotFoundException exception) {
        String bodyOfResponse = exception.getMessage();
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }
    @ExceptionHandler(SectionProductNotFoundException.class)
    protected ResponseEntity<Object> handleSectionProductNotFoundException(SectionProductNotFoundException exception) {
        String bodyOfResponse = exception.getMessage();
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }
    @ExceptionHandler(ProductDoesNotMatchSectionException.class)
    protected ResponseEntity<Object> handleProductDoesNotMatchSectionException(ProductDoesNotMatchSectionException exception) {
        String bodyOfResponse = exception.getMessage();
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }
}
