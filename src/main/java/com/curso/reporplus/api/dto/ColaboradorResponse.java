package com.curso.reporplus.api.dto;

import com.curso.reporplus.domain.FuncaoColaborador;
import com.curso.reporplus.domain.Status;

import java.time.LocalDate;

public record ColaboradorResponse(Long id, Long pessoaId, String nome, String email,
                                  String cpf, Status status, String matricula,
                                  FuncaoColaborador funcao, LocalDate dataAdmissao) { }
