package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.dto.BatchStockDTO;
import br.com.group9.pimlwarehouse.dto.ProductDTO;
import br.com.group9.pimlwarehouse.exception.ProductNotFoundException;
import br.com.group9.pimlwarehouse.service.handler.ProductAPIErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductAPIService {
    private static final String PRODUCT_API_URI = "https://4bf27f8c-fe37-4752-b67f-a9aba01d33a4.mock.pstmn.io";
    private static final String PRODUCTS_RESOURCE = "/api/v1/fresh-products";
    private final RestTemplate restTemplate;

    public ProductAPIService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new ProductAPIErrorHandler())
                .build();
    }

    /**
     * Fetch for product by Id in the products API.
     * @param id receives a productId to associate a section.
     * @return will perform a communication with the products API and returns the result.
     * If an exception occurs, returns "PRODUCT_NOT_FOUND".
     */

    public ProductDTO fetchProductById(Long id) {
        String resourceURI = PRODUCT_API_URI.concat(PRODUCTS_RESOURCE).concat("/").concat(id.toString());
        ResponseEntity<ProductDTO> result = restTemplate.getForEntity(resourceURI, ProductDTO.class);
        return result.getBody();
    }

    public ProductDTO fetchProductsById(Map<String, List<Long>> ids) {
        String resourceURI = PRODUCT_API_URI.concat(PRODUCTS_RESOURCE).concat("/");
        // TODO: 27/04/22 Create custom validations on Response for Product API.
        try {
            ResponseEntity<ProductDTO> result = restTemplate.getForEntity(resourceURI, ProductDTO.class );
            return result.getBody();
        } catch (RuntimeException ex) {
            throw new ProductNotFoundException("PRODUCT_NOT_FOUND");
        }
    }

    /**
     * Search in Product API information about a products inside a list of batch stock.
     * @param batchStockList receives a List<BatchStockDTO>.
     * @return returns a list of products from Product API.
     */
    public List<Map<ProductDTO, BatchStockDTO>> getProductInfo(List<BatchStockDTO> batchStockList) {
        return batchStockList.stream().map(batchStockDTO ->
                Map.of(fetchProductById(batchStockDTO.getProductId()), batchStockDTO)
        ).collect(Collectors.toList());
    }
}
