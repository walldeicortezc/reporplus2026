package com.curso.reporplus.api.dto;

import com.curso.reporplus.domain.Status;

public record ClienteResponse(Long id, Long pessoaId, String nome, String email,
                              String cpf, String telefone, Status status) { }
