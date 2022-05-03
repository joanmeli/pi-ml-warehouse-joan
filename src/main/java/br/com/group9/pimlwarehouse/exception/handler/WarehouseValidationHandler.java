package br.com.group9.pimlwarehouse.exception.handler;

import br.com.group9.pimlwarehouse.dto.ErrorMessageDTO;
import br.com.group9.pimlwarehouse.exception.BatchStockWithdrawException;
import br.com.group9.pimlwarehouse.exception.WarehouseNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WarehouseValidationHandler {

    @ExceptionHandler(WarehouseNotFoundException.class)
    protected ResponseEntity<Object> handleWarehouseNotFoundException(WarehouseNotFoundException exception) {
        ErrorMessageDTO error = new ErrorMessageDTO(exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    @ExceptionHandler(BatchStockWithdrawException.class)
    protected ResponseEntity<Object> handleBatchStockWithdrawException(BatchStockWithdrawException exception) {
        ErrorMessageDTO error = new ErrorMessageDTO(exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
