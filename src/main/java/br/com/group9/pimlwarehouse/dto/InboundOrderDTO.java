package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.InboundOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InboundOrderDTO {
    private  Long orderNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime orderDate;
    private InboundOrderSectionDTO section;
    @JsonProperty(value = "batchStock")
    private List<BatchStockDTO> batchStockList;

    public InboundOrder convert(){

        return InboundOrder.builder()
                .section(section.convert())
                .id(orderNumber)
                .orderDate(orderDate)
                .build();
    }

}
