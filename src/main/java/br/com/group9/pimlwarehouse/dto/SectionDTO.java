package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.Section;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class SectionDTO {

    private Long id;

    @NotNull(message = "Informar a temperatura mínima de armazenamento no Setor.")
    private Double minimalTemperature;

    @NotNull(message = "Informar a temperatura máxima de armazenamento no Setor.")
    private Double maximalTemperature;

    @NotNull(message = "Informar a capacidade de armazenamento do Setor.")
    private Integer size;

    private List<SectionProductDTO> allowedProducts;

    public Section map() {
        return Section.builder()
                .id(this.id)
                .minimalTemperature(this.minimalTemperature)
                .maximalTemperature(this.maximalTemperature)
                .size(this.size)
                .build();
    }

    public static SectionDTO simpleMap(Section section){
        return SectionDTO.builder()
                .id(section.getId())
                .minimalTemperature(section.getMinimalTemperature())
                .maximalTemperature(section.getMaximalTemperature())
                .size(section.getSize())
                .build();
    }

    public static SectionDTO map(Section section){
        List<SectionProductDTO> sectionProductDTOS = new ArrayList<>();
        section.getSectionProducts().forEach(p -> sectionProductDTOS.add(SectionProductDTO.map(p)));
        return SectionDTO.builder()
                .id(section.getId())
                .minimalTemperature(section.getMinimalTemperature())
                .maximalTemperature(section.getMaximalTemperature())
                .size(section.getSize())
                .allowedProducts(sectionProductDTOS)
                .build();
    }
}
