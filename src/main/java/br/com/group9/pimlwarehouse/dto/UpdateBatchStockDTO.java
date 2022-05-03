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
public class UpdateBatchStockDTO {
    private Integer batchNumber;
    private Integer currentQuantity;

    private BatchStock convert(InboundOrder order){

        return BatchStock.builder()
                .inboundOrder(order)
                .batchNumber(this.batchNumber)
                .currentQuantity(this.currentQuantity)
                .build();
    }

    public static List<BatchStock> convert(List<UpdateBatchStockDTO> batchStockDTOS, InboundOrder order){
        return batchStockDTOS.stream().map(e -> e.convert(order)).collect(Collectors.toList());
    }
    private static UpdateBatchStockDTO convert(BatchStock batchStock){

        return UpdateBatchStockDTO.builder()
                .batchNumber(batchStock.getBatchNumber())
                .currentQuantity(batchStock.getCurrentQuantity())
                .build();
    }

    public static List<UpdateBatchStockDTO> convert(List<BatchStock> batchStocks) {
        return batchStocks.stream().map(UpdateBatchStockDTO::convert).collect(Collectors.toList());
    }
}
