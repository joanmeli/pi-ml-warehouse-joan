package br.com.group9.pimlwarehouse.dto;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter @Setter
public class ProductBatchStockDTO {
    private Long productId;
    private Integer quantity;

    public static Map<Long, Integer> convert(List<ProductBatchStockDTO> products) {
        return products
                .stream()
                .collect(Collectors.toMap(
                        ProductBatchStockDTO::getProductId,
                        ProductBatchStockDTO::getQuantity,
                        (a, b) -> a+b)
                );
    }
}
