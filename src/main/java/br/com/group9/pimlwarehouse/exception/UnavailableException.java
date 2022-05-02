package br.com.group9.pimlwarehouse.exception;

public class UnavailableException extends RuntimeException {
    public UnavailableException(String message) {
        super(message);
    }
}
