package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BatchStockDTO {
    private Long productId;
    private Integer batchNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime manufacturingDateTime;
    private Integer initialQuantity;
    private Integer currentQuantity;

    private BatchStock convert(InboundOrder order, ProductDTO productDTO){

        return BatchStock.builder()
                .inboundOrder(order)
                .productSize(productDTO.getSize())
                .productId(this.productId)
                .batchNumber(this.batchNumber)
                .dueDate(this.dueDate)
                .manufacturingDate(this.manufacturingDateTime)
                .initialQuantity(this.initialQuantity)
                .currentQuantity(this.currentQuantity)
                .category(productDTO.getCategory())
                .build();
    }

    public static List<BatchStock> convert(List<Map<ProductDTO, BatchStockDTO>> batchStockDTOS, InboundOrder order){
        return batchStockDTOS.stream().map(
                e -> e.entrySet().stream().map(
                        a -> a.getValue().convert(order, a.getKey())
                ).collect(Collectors.toList())
        ).flatMap(List::stream).collect(Collectors.toList());
    }
    private static BatchStockDTO convert(BatchStock batchStock){

        return BatchStockDTO.builder()
                .productId(batchStock.getProductId())
                .batchNumber(batchStock.getBatchNumber())
                .dueDate(batchStock.getDueDate())
                .manufacturingDateTime(batchStock.getManufacturingDate())
                .initialQuantity(batchStock.getInitialQuantity())
                .currentQuantity(batchStock.getCurrentQuantity())
                .build();
    }

    public static List<BatchStockDTO> convert(List<BatchStock> batchStocks) {
        return batchStocks.stream().map(BatchStockDTO::convert).collect(Collectors.toList());
    }
}
