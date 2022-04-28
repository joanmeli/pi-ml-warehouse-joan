package br.com.group9.pimlwarehouse.exception;

import javax.persistence.EntityNotFoundException;

public class SectionProductNotFoundException extends EntityNotFoundException {
    public SectionProductNotFoundException(String message) {
        super(message);
    }
}
