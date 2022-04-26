package br.com.group9.pimlwarehouse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class ProductDTO {
    private Long productId;
    private String productName;
}
