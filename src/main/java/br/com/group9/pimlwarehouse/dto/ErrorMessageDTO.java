package br.com.group9.pimlwarehouse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter @Setter
public class ErrorMessageDTO {
    private String errorMessage = "PRODUCT_OUT_OF_STOCK";
}
