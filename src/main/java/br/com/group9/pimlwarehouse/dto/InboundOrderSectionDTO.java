package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.Section;
import br.com.group9.pimlwarehouse.entity.Warehouse;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InboundOrderSectionDTO {
    private Long sectionCode;
    private Long warehouseCode;

    public Section convert(){
        return Section.builder()
                .id(sectionCode)
                .warehouse(Warehouse.builder().id(warehouseCode).build())
                .build();

    }
}
