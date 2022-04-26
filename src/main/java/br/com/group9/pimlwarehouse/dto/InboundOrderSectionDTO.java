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
    private String sectionCode;
    private String warehouseCode;

    public Section convert(){
        return Section.builder()
                .id(Long.valueOf(sectionCode))
                .warehouse(Warehouse.builder().id(Long.valueOf(warehouseCode)).build())
                .build();

    }
}
