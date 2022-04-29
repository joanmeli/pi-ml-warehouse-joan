package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.enums.CategoryENUM;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class ProductDTO {
    private Long id;
    private Double minimumTemperature;
    private Double size;
    private CategoryENUM category;
}
