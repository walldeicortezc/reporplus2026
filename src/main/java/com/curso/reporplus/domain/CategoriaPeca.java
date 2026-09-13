package com.curso.reporplus.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoriaPeca {

    private final String nome;
    private Status status;
    private final List<Peca> pecas = new ArrayList<>();

    public CategoriaPeca(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da categoria é obrigatório");
        }

        this.nome = nome.trim();
        this.status = Status.ATIVO;
    }

    public void adicionarPeca(Peca peca) {
        Objects.requireNonNull(peca, "Peça é obrigatória");

        boolean codigoJaUtilizado = pecas.stream()
                .anyMatch(item -> item != peca
                        && item.getCodigo().equals(peca.getCodigo()));

        if (codigoJaUtilizado) {
            throw new IllegalArgumentException("Código da peça já utilizado na categoria");
        }

        peca.associarA(this);

        if (!pecas.contains(peca)) {
            pecas.add(peca);
        }
    }

    public void ativar() {
        this.status = Status.ATIVO;
    }

    public void inativar() {
        this.status = Status.INATIVO;
    }

    public String getNome() {
        return nome;
    }

    public Status getStatus() {
        return status;
    }

    public List<Peca> getPecas() {
        return List.copyOf(pecas);
    }
}
