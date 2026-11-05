package com.curso.reporplus.application;

import java.time.Instant;

public record TokensAutenticacao(
        TokenAcesso acesso,
        String refreshToken,
        Instant refreshTokenExpiraEm) {
}
