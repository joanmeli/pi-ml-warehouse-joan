package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.BatchStock;
import br.com.group9.pimlwarehouse.entity.InboundOrder;
import br.com.group9.pimlwarehouse.entity.Section;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InboundOrderDTO {
    private  String orderNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime orderDate;
    private InboundOrderSectionDTO section;
    @JsonProperty(value = "batchStock")
    private List<BatchStockDTO> batchStockList;

    public InboundOrder convert(){

        return InboundOrder.builder()
                .sections(section.convert())
                .orderDate(orderDate)
                .build();
    }

}
