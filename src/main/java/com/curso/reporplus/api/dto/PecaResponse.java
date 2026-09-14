package com.curso.reporplus.api.dto;

import com.curso.reporplus.domain.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PecaResponse(
        Long id,
        String codigo,
        String descricao,
        int quantidadeEstoque,
        BigDecimal custoUnitario,
        int estoqueMinimo,
        BigDecimal valorEstoque,
        LocalDate dataCadastro,
        Status status,
        Long categoriaId,
        String categoriaNome,
        Long fornecedorId,
        String fornecedorRazaoSocial) {
}
