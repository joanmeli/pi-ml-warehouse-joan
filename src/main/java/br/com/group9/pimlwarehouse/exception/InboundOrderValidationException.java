package br.com.group9.pimlwarehouse.exception;

public class InboundOrderValidationException extends RuntimeException{
    private static final long serialVersionUID = -1957137827452037216L;

    public InboundOrderValidationException(String mensagem) {
        super(mensagem);
    }
}
