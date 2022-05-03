package br.com.group9.pimlwarehouse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class AgentDTO {
    private Long id;

    @NotBlank(message = "Informar o nome do cadastro.")
    private String name;

    @NotBlank(message = "Informar o número de CPF do cadastro.")
    private String cpf;

    @NotBlank(message = "Informar uma senha para login.")
    private String username;

    @NotBlank(message = "Informar uma senha para login.")
    private String password;

    @Email(message = "Informar um e-mail válido.")
    private String email;

    private final String role = "agent";

    @NotNull(message = "Informar o ID do Armazém do cadastro.")
    private Long warehouseId;
}
