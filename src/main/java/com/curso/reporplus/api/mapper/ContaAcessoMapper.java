package com.curso.reporplus.api.mapper;

import com.curso.reporplus.api.dto.ContaAcessoResponse;
import com.curso.reporplus.domain.ContaAcesso;
import org.springframework.stereotype.Component;

@Component
public class ContaAcessoMapper {

    public ContaAcessoResponse toResponse(ContaAcesso conta) {
        return new ContaAcessoResponse(
                conta.getId(),
                conta.getPessoa().getId(),
                conta.getPessoa().getNome(),
                conta.getLogin(),
                conta.getStatus(),
                conta.getPerfis());
    }
}
