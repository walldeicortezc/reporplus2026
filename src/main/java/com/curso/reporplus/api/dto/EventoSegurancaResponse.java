package com.curso.reporplus.api.dto;

import com.curso.reporplus.domain.TipoEventoSeguranca;

import java.time.Instant;

public record EventoSegurancaResponse(
        Long id,
        TipoEventoSeguranca tipo,
        Instant ocorridoEm,
        Long atorContaId,
        Long alvoContaId,
        String enderecoOrigem) {
}
