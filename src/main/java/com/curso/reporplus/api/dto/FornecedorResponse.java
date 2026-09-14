package com.curso.reporplus.api.dto;

import com.curso.reporplus.domain.Status;

public record FornecedorResponse(Long id, String razaoSocial, String cnpj, Status status) {
}
