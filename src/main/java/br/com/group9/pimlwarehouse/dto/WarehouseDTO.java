package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class WarehouseDTO {

    private Long id;

    @NotBlank(message = "Informar o nome do Armazém.")
    private String name;

    @Valid
    @NotEmpty(message = "Informar ao menos 1 Setor no Armazém.")
    @JsonIgnoreProperties({"allowedProducts"})
    private List<SectionDTO> sections;

    public Warehouse map() {
        return Warehouse.builder()
                .id(this.id)
                .name(this.name)
                .sections(sections.stream().map(SectionDTO::map).collect(Collectors.toList()))
                .build();
    }

    public static WarehouseDTO map(Warehouse warehouse) {
        List<SectionDTO> sectionDTOS = new ArrayList<>();
        warehouse.getSections().forEach(s -> sectionDTOS.add(SectionDTO.simpleMap(s)));
        return WarehouseDTO.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .sections(sectionDTOS)
                .build();
    }
}
