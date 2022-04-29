package br.com.group9.pimlwarehouse.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductWarehouseDTO {

    private Long productId;

    private List<WarehouseProductDTO> warehouses;

    public static ProductWarehouseDTO convert(Long productId, List<WarehouseProductDTO> warehouses){
        return ProductWarehouseDTO.builder()
                .productId(productId)
                .warehouses(warehouses)
                .build();
    }
}