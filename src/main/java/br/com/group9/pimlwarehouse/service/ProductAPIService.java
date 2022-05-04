package br.com.group9.pimlwarehouse.service;

import br.com.group9.pimlwarehouse.dto.BatchStockDTO;
import br.com.group9.pimlwarehouse.dto.ProductDTO;
import br.com.group9.pimlwarehouse.exception.ProductNotFoundException;
import br.com.group9.pimlwarehouse.service.handler.ProductAPIErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductAPIService {
    private static final String PRODUCT_API_URI = "http://products:8081";
    private static final String PRODUCTS_RESOURCE = "/fresh-products/v1";
    private final RestTemplate restTemplate;

    public ProductAPIService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new ProductAPIErrorHandler())
                .build();
    }

    /**
     * Fetch for product by Id in the products API.
     * @param id receives a productId to search for.
     * @return will perform a communication with the products API and returns the result.
     * If an exception occurs, returns "PRODUCT_NOT_FOUND".
     */

    public ProductDTO fetchProductById(Long id) {
        String resourceURI = PRODUCT_API_URI.concat(PRODUCTS_RESOURCE).concat("/").concat(id.toString());
        ResponseEntity<ProductDTO> result = restTemplate.getForEntity(resourceURI, ProductDTO.class);
        return result.getBody();
    }

    /**
     * Fetch several products by Id List in the products API.
     * @param ids receives a List<Long> of productIds to search for.
     * @return will perform a communication with the products API and returns a Map of the result.
     * If an exception occurs, returns "PRODUCT_NOT_FOUND".
     */
    public Map<Long, ProductDTO> fetchProductsById(List<Long> ids) {
        String concatenatedIds = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        String resourceURI = PRODUCT_API_URI.concat(PRODUCTS_RESOURCE).concat("?products=").concat(concatenatedIds);
        ResponseEntity<ProductDTO[]> result = restTemplate.getForEntity(resourceURI, ProductDTO[].class );
        return ids.stream()
                .map(e -> Map.entry(
                        e,
                        Arrays.stream(result.getBody()).filter(p -> p.getId().equals(e))
                                .findFirst()
                                .orElseThrow(() -> new ProductNotFoundException("PRODUCT_NOT_FOUND: ".concat(e.toString())))
                ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b));
    }

    /**
     * Search in Product API information about a products inside a list of batch stock.
     * @param batchStockList receives a List<BatchStockDTO>.
     * @return returns a list of products from Product API.
     */
    public List<Map<ProductDTO, BatchStockDTO>> getProductInfo(List<BatchStockDTO> batchStockList) {
        List<Long> idList = batchStockList.stream().map(b -> b.getProductId()).collect(Collectors.toList());
        Map<Long, ProductDTO> foundProductsMap = fetchProductsById(idList);

        return batchStockList.stream().map(batchStockDTO ->
                    Map.of(foundProductsMap.get(batchStockDTO.getProductId()), batchStockDTO)
        ).collect(Collectors.toList());
    }
}
