package br.com.group9.pimlwarehouse.exception;

public class ProductDoesNotMatchSectionException extends RuntimeException {
    public ProductDoesNotMatchSectionException(String message) {
        super(message);
    }
}
