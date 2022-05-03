package br.com.group9.pimlwarehouse.exception.handler;


import br.com.group9.pimlwarehouse.dto.ErrorMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class BeanValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<List<ErrorMessageDTO>> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<ObjectError> erros = e.getBindingResult().getAllErrors();
        List<ErrorMessageDTO> results = new ArrayList<>();
        erros.forEach(x -> {
            ErrorMessageDTO error = new ErrorMessageDTO(x.getDefaultMessage());
            results.add(error);
        });
        return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
    }
}
