package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.Warehouse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    private List<SectionDTO> sectors;

    public Warehouse map() {
        return Warehouse.builder()
                .id(this.id)
                .name(this.name)
                .sections(sectors.stream().map(SectionDTO::map).collect(Collectors.toList()))
                .build();
    }

    public static WarehouseDTO map(Warehouse warehouse) {
        return WarehouseDTO.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .sectors(warehouse.getSections().stream().map(SectionDTO::map).collect(Collectors.toList()))
                .build();
    }
}
