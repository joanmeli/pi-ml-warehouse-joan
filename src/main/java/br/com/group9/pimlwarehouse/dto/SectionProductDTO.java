package br.com.group9.pimlwarehouse.dto;

import br.com.group9.pimlwarehouse.entity.SectionProduct;
import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class SectionProductDTO {
    @NotNull(message = "Informar o número do Produto.")
    private Long productId;

    public static SectionProductDTO map(SectionProduct sectionProduct){
        return SectionProductDTO.builder()
                .productId(sectionProduct.getProductId())
                .build();
    }
}
