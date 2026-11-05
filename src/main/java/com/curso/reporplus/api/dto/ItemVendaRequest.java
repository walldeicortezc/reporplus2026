package com.curso.reporplus.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

public record ItemVendaRequest(
        @NotNull @Schema(example = "1") Long pecaId,
        @NotNull @Positive @Schema(example = "2") Integer quantidade) { }
