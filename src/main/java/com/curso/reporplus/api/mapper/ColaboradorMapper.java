package com.curso.reporplus.api.mapper;

import com.curso.reporplus.api.dto.ColaboradorResponse;
import com.curso.reporplus.domain.Colaborador;
import com.curso.reporplus.domain.Pessoa;
import org.springframework.stereotype.Component;

@Component
public class ColaboradorMapper {
    public ColaboradorResponse toResponse(Colaborador colaborador) {
        Pessoa pessoa = colaborador.getPessoa();
        return new ColaboradorResponse(colaborador.getId(), pessoa.getId(), pessoa.getNome(), pessoa.getEmail(),
                pessoa.getCpf(), pessoa.getStatus(), colaborador.getMatricula(), colaborador.getFuncao(),
                colaborador.getDataAdmissao());
    }
}
