package com.curso.reporplus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categoria_peca")
public class CategoriaPeca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Peca> pecas = new ArrayList<>();

    protected CategoriaPeca() {
    }

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

    public Long getId() {
        return id;
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
