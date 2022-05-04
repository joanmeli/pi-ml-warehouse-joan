package br.com.group9.pimlwarehouse.exception.handler;

import br.com.group9.pimlwarehouse.dto.ErrorMessageDTO;
import br.com.group9.pimlwarehouse.exception.ProductDoesNotMatchSectionException;
import br.com.group9.pimlwarehouse.exception.ProductNotFoundException;
import br.com.group9.pimlwarehouse.exception.SectionNotFoundException;
import br.com.group9.pimlwarehouse.exception.SectionProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SectionValidationHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    protected ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException exception) {
        ErrorMessageDTO error = new ErrorMessageDTO(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(SectionNotFoundException.class)
    protected ResponseEntity<Object> handleSectionNotFoundException(SectionNotFoundException exception) {
        ErrorMessageDTO error = new ErrorMessageDTO(exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    @ExceptionHandler(SectionProductNotFoundException.class)
    protected ResponseEntity<Object> handleSectionProductNotFoundException(SectionProductNotFoundException exception) {
        ErrorMessageDTO error = new ErrorMessageDTO(exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    @ExceptionHandler(ProductDoesNotMatchSectionException.class)
    protected ResponseEntity<Object> handleProductDoesNotMatchSectionException(ProductDoesNotMatchSectionException exception) {
        ErrorMessageDTO error = new ErrorMessageDTO(exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
