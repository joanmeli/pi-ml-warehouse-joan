package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BatchStockByDuaDateDTO {
    private Long productId;
    private Integer batchNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private Integer currentQuantity;

    private static BatchStockByDuaDateDTO convert(BatchStock batchStock){

        return BatchStockByDuaDateDTO.builder()
                .productId(batchStock.getProductId())
                .batchNumber(batchStock.getBatchNumber())
                .dueDate(batchStock.getDueDate())
                .currentQuantity(batchStock.getCurrentQuantity())
                .build();
    }

    public static List<BatchStockByDuaDateDTO> convert(List<BatchStock> batchStocks) {
        return batchStocks.stream().map(BatchStockByDuaDateDTO::convert).collect(Collectors.toList());
    }
}
