package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.dto.AgentDTO;
import br.com.group9.pimlwarehouse.exception.ProductNotFoundException;
import br.com.group9.pimlwarehouse.exception.WarehouseNotFoundException;
import br.com.group9.pimlwarehouse.service.handler.AuthenticationAPIErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationAPIService {
    private static final String AUTH_API_URI = "http://gandalf:8080";
    private static final String AUTH_RESOURCE = "/user/v1";
    private final RestTemplate restTemplate;
    private WarehouseService warehouseService;

    public AuthenticationAPIService(RestTemplateBuilder restTemplateBuilder, WarehouseService warehouseService) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new AuthenticationAPIErrorHandler())
                .build();
        this.warehouseService = warehouseService;
    }

    /**
     * Create a new agent if it has not linked to a warehouse.
     * @param agentDTO receives agent data to do validation.
     * @return warehouse Id will be informed, if it does not exist, returns "WAREHOUSE_NOT-FOUND".
     * Will perform validation with the authentication API and will return, if the given agent already
     * exists, returns "AGENT_ALREADY_EXISTS" or "MALFORMED_AGENT_DATA" if any other response from auth API.
     * In case the validation call doesn't suceeds, returns "AUTHENTICATION_API_UNAVAILABLE" UnavailableException.
     */
    public AgentDTO createAgent(AgentDTO agentDTO) {
        String resourceURI = AUTH_API_URI.concat(AUTH_RESOURCE).concat("/");

        if(!this.warehouseService.exists(agentDTO.getWarehouseId()))
            throw new WarehouseNotFoundException("WAREHOUSE_NOT_FOUND");

        ResponseEntity<AgentDTO> result = restTemplate.postForEntity(resourceURI, agentDTO, AgentDTO.class);
        return result.getBody();
    }
}
