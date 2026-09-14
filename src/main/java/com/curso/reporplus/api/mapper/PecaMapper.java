package com.curso.reporplus.api.mapper;

import com.curso.reporplus.api.dto.PecaRequest;
import com.curso.reporplus.api.dto.PecaResponse;
import com.curso.reporplus.domain.Fornecedor;
import com.curso.reporplus.domain.Peca;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PecaMapper {

    public Peca toEntity(PecaRequest request) {
        return new Peca(
                request.codigo(),
                request.descricao(),
                request.quantidadeEstoque(),
                request.custoUnitario(),
                request.estoqueMinimo(),
                LocalDate.now());
    }

    public PecaResponse toResponse(Peca peca) {
        Fornecedor fornecedor = peca.getFornecedor();

        return new PecaResponse(
                peca.getId(),
                peca.getCodigo(),
                peca.getDescricao(),
                peca.getQuantidadeEstoque(),
                peca.getCustoUnitario(),
                peca.getEstoqueMinimo(),
                peca.calcularValorEstoque(),
                peca.getDataCadastro(),
                peca.getStatus(),
                peca.getCategoria().getId(),
                peca.getCategoria().getNome(),
                fornecedor == null ? null : fornecedor.getId(),
                fornecedor == null ? null : fornecedor.getRazaoSocial());
    }
}
