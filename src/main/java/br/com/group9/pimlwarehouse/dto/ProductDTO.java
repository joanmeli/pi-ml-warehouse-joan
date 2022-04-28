package br.com.group9.pimlwarehouse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class ProductDTO {
    private Long id;
    private Double minimumTemperature;
}
