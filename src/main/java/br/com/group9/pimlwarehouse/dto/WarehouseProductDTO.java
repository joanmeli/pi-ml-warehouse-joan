package br.com.group9.pimlwarehouse.dto;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class WarehouseProductDTO {

    private Long warehouseId;
    private Integer productAmount;

    public static List<WarehouseProductDTO> convert(Map<Long, Integer> warehouses){
        return warehouses.entrySet()
                .stream()
                .map(w -> new WarehouseProductDTO(w.getKey(), w.getValue()))
                .collect(Collectors.toList());
    }

}
