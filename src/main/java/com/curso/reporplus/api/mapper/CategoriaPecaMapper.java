package com.curso.reporplus.api.mapper;

import com.curso.reporplus.api.dto.CategoriaPecaResponse;
import com.curso.reporplus.domain.CategoriaPeca;
import org.springframework.stereotype.Component;

@Component
public class CategoriaPecaMapper {

    public CategoriaPecaResponse toResponse(CategoriaPeca categoria) {
        return new CategoriaPecaResponse(
                categoria.getId(), categoria.getNome(), categoria.getStatus());
    }
}
