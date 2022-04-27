package br.com.group9.pimlwarehouse.exception;

import javax.persistence.EntityNotFoundException;

public class WarehouseNotFoundException extends EntityNotFoundException {
    public WarehouseNotFoundException(String message) {
        super(message);
    }
}
