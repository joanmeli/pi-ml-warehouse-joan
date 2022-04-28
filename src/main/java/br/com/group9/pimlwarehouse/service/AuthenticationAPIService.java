package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.dto.AgentDTO;
import br.com.group9.pimlwarehouse.exception.ProductNotFoundException;
import br.com.group9.pimlwarehouse.exception.WarehouseNotFoundException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationAPIService {
    private static final String AUTH_API_URI = "https://5bb63d0d-cc34-49d7-9440-7248d665c6dd.mock.pstmn.io";
    private static final String AUTH_RESOUCE = "/api/v1/user";
    private final RestTemplate restTemplate;
    private WarehouseService warehouseService;

    public AuthenticationAPIService(RestTemplateBuilder restTemplateBuilder, WarehouseService warehouseService) {
        this.restTemplate = restTemplateBuilder
                .build();
        this.warehouseService = warehouseService;
    }

    public AgentDTO createAgent(AgentDTO agentDTO) {
        String resourceURI = AUTH_API_URI.concat(AUTH_RESOUCE).concat("/");

        if(!this.warehouseService.exists(agentDTO.getWarehouseId()))
            throw new WarehouseNotFoundException("WAREHOUSE_NOT_FOUND");

        // TODO: 27/04/22 Create custom validations on Response for Authentication API.
        try {
            ResponseEntity<AgentDTO> result = restTemplate.postForEntity(resourceURI, agentDTO, AgentDTO.class);
            return result.getBody();
        } catch (RuntimeException ex) {
            throw new ProductNotFoundException("PRODUCT_NOT_FOUND");
        }
    }
}
