package com.curso.reporplus.api.dto;

import java.math.BigDecimal;

public record ItemVendaResponse(Long id, Long pecaId, String pecaDescricao,
                                int quantidade, BigDecimal custoUnitario,
                                BigDecimal subtotal) { }
