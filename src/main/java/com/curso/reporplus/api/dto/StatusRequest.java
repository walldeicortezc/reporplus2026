package com.curso.reporplus.api.dto;

import com.curso.reporplus.domain.Status;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(
        @NotNull(message = "Status é obrigatório")
        Status status) {
}
