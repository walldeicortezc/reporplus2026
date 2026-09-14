package com.curso.reporplus.api.mapper;

import com.curso.reporplus.api.dto.FornecedorResponse;
import com.curso.reporplus.domain.Fornecedor;
import org.springframework.stereotype.Component;

@Component
public class FornecedorMapper {

    public FornecedorResponse toResponse(Fornecedor fornecedor) {
        return new FornecedorResponse(
                fornecedor.getId(),
                fornecedor.getRazaoSocial(),
                fornecedor.getCnpj(),
                fornecedor.getStatus());
    }
}
