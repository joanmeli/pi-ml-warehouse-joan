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

    public AgentDTO createAgent(AgentDTO agentDTO) {
        String resourceURI = AUTH_API_URI.concat(AUTH_RESOURCE).concat("/");

        if(!this.warehouseService.exists(agentDTO.getWarehouseId()))
            throw new WarehouseNotFoundException("WAREHOUSE_NOT_FOUND");

        ResponseEntity<AgentDTO> result = restTemplate.postForEntity(resourceURI, agentDTO, AgentDTO.class);
        return result.getBody();
    }
}
