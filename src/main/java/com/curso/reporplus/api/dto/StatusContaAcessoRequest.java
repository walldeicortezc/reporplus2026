package com.curso.reporplus.api.dto;

import com.curso.reporplus.domain.StatusContaAcesso;
import jakarta.validation.constraints.NotNull;

public record StatusContaAcessoRequest(
        @NotNull(message = "Status é obrigatório")
        StatusContaAcesso status) {
}
