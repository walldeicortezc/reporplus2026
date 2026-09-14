package com.curso.reporplus.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PecaRequest(
        @NotBlank(message = "Código é obrigatório")
        @Size(max = 50, message = "Código deve possuir no máximo 50 caracteres")
        String codigo,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 150, message = "Descrição deve possuir no máximo 150 caracteres")
        String descricao,

        @NotNull(message = "Quantidade em estoque é obrigatória")
        @PositiveOrZero(message = "Quantidade em estoque não pode ser negativa")
        Integer quantidadeEstoque,

        @NotNull(message = "Custo unitário é obrigatório")
        @PositiveOrZero(message = "Custo unitário não pode ser negativo")
        BigDecimal custoUnitario,

        @NotNull(message = "Estoque mínimo é obrigatório")
        @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
        Integer estoqueMinimo,

        @NotNull(message = "Categoria é obrigatória")
        @Positive(message = "Identificador da categoria deve ser positivo")
        Long categoriaId,

        @Positive(message = "Identificador do fornecedor deve ser positivo")
        Long fornecedorId) {
}
