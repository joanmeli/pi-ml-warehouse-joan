package br.com.group9.pimlwarehouse.exception;

import javax.persistence.EntityNotFoundException;

public class SectionNotFoundException extends EntityNotFoundException {
    public SectionNotFoundException(String message) {
        super(message);
    }
}
