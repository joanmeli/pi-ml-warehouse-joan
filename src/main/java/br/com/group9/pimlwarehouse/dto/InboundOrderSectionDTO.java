package br.com.group9.pimlwarehouse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InboundOrderSectionDTO {
    private String sectionCode;
    private String warehouseCode;
}
