package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.dto.ProductDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductAPIService {
    private static final String PRODUCT_API_URI = "https://4bf27f8c-fe37-4752-b67f-a9aba01d33a4.mock.pstmn.io";
    private static final String PRODUCTS_RESOUCE = "/api/v1/fresh-products";
    private final RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public ProductAPIService(@Lazy RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductDTO getProductById(Long id) {
        String resourceURI = PRODUCT_API_URI.concat(PRODUCTS_RESOUCE).concat("/").concat(id.toString());
        ResponseEntity<ProductDTO> result = restTemplate.getForEntity(resourceURI, ProductDTO.class);
        if(!result.getStatusCode().is2xxSuccessful())
            throw new RuntimeException("Chamada para API não sucedeu.");
        return result.getBody();
    }
}
