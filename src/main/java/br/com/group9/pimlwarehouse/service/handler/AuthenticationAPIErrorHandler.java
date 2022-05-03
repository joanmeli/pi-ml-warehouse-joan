package br.com.group9.pimlwarehouse.service.handler;

import br.com.group9.pimlwarehouse.dto.ErrorMessageDTO;
import br.com.group9.pimlwarehouse.exception.AgentValidationException;
import br.com.group9.pimlwarehouse.exception.ProductNotFoundException;
import br.com.group9.pimlwarehouse.exception.UnavailableException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Component
public class AuthenticationAPIErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

        return (httpResponse.getStatusCode().series() == CLIENT_ERROR
                    || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {

        if (httpResponse.getStatusCode().series() == SERVER_ERROR) {
            // handle SERVER_ERROR
        } else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            String resultString = "";

            try {
                resultString = new BufferedReader(new InputStreamReader(httpResponse.getBody()))
                        .lines().collect(Collectors.joining("\n"));
            } catch (JsonParseException ex) { }

            if (httpResponse.getStatusCode() == HttpStatus.CONFLICT) {
                throw new AgentValidationException("AGENT_ALREADY_EXISTS", resultString);
            }

            if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new AgentValidationException("MALFORMED_AGENT_DATA", resultString);
            }
        }

        throw new UnavailableException("AUTHENTICATION_API_UNAVAILABLE");
    }
}