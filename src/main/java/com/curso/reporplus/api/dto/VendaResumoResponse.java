package com.curso.reporplus.api.dto;

import com.curso.reporplus.domain.StatusVenda;

import java.time.LocalDateTime;

public record VendaResumoResponse(Long id, Long clienteId, String clienteNome,
                                  Long vendedorId, String vendedorNome,
                                  LocalDateTime dataVenda, StatusVenda status) { }
