package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.Product;
import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class SectionProductDTO {
    @NotNull(message = "Informar o número do Produto.")
    private Long productId;

    public static SectionProductDTO map(Product product){
        return SectionProductDTO.builder()
                .productId(product.getId())
                .build();
    }
}
