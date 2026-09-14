package com.curso.reporplus.api.dto;

import com.curso.reporplus.domain.Status;

public record CategoriaPecaResponse(Long id, String nome, Status status) {
}
