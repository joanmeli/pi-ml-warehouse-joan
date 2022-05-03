package br.com.group9.pimlwarehouse.exception;

import lombok.Getter;

@Getter
public class AgentValidationException extends RuntimeException {
    String errorMessage;
    public AgentValidationException(String message, String errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }
}
