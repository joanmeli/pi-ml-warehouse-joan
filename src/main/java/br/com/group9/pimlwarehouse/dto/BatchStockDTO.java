package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    private BatchStock convert(InboundOrder order){

        return BatchStock.builder()
                .inboundOrder(order)
                .productSize(10L) // TODO: add the real size of the product here
                .productId(this.productId)
                .batchNumber(this.batchNumber)
                .dueDate(this.dueDate)
                .manufacturingDate(this.manufacturingDateTime)
                .initialQuantity(this.initialQuantity)
                .currentQuantity(this.currentQuantity)
                .build();
    }

    public static List<BatchStock> convert(List<BatchStockDTO> batchStockDTOS, InboundOrder order){
        return batchStockDTOS.stream().map(e -> e.convert(order)).collect(Collectors.toList());
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
        return batchStocks.stream().map(e -> convert(e)).collect(Collectors.toList());
    }
}
