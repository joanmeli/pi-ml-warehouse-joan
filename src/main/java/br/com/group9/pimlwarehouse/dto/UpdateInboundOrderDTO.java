package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.InboundOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UpdateInboundOrderDTO {
    private  Long orderNumber;
    private InboundOrderSectionDTO section;
    @JsonProperty(value = "batchStock")
    private List<UpdateBatchStockDTO> batchStockList;

    public InboundOrder convert(){

        return InboundOrder.builder()
                .section(section.convert())
                .build();
    }
}
