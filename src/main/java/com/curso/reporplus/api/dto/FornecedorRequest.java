package com.curso.reporplus.api.dto;

import com.curso.reporplus.api.validation.CnpjValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastrar um fornecedor")
public record FornecedorRequest(
        @Schema(example = "Peças Agrícolas Central Ltda.")
        @NotBlank(message = "Razão social é obrigatória")
        @Size(max = 150, message = "Razão social deve possuir no máximo 150 caracteres")
        String razaoSocial,

        @Schema(example = "11444777000161")
        @NotBlank(message = "CNPJ é obrigatório")
        @CnpjValido
        String cnpj) {
}
