package com.curso.reporplus.application;

public class RefreshTokenInvalidoException extends CredenciaisInvalidasException {

    public RefreshTokenInvalidoException() {
        super("Refresh token inválido ou expirado");
    }
}
