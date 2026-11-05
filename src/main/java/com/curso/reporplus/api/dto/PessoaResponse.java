package com.curso.reporplus.api.dto;

import com.curso.reporplus.domain.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PessoaResponse(Long id, String nome, String email, String cpf,
                             LocalDate dataCadastro, Status status) { }
