package com.curso.reporplus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
        name = "peca",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_peca_codigo",
                columnNames = "codigo"))
public class Peca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column(name = "quantidade_estoque", nullable = false)
    private int quantidadeEstoque;

    @Column(name = "custo_unitario", nullable = false, precision = 18, scale = 2)
    private BigDecimal custoUnitario;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "categoria_peca_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_peca_categoria_peca"))
    private CategoriaPeca categoria;

    protected Peca() {
    }

    public Peca(
            String codigo,
            String descricao,
            int quantidadeEstoque,
            BigDecimal custoUnitario,
            LocalDate dataCadastro) {
        this.codigo = validarTexto(codigo, "Código é obrigatório");
        this.descricao = validarTexto(descricao, "Descrição é obrigatória");
        this.quantidadeEstoque = validarNaoNegativo(
                quantidadeEstoque,
                "Quantidade em estoque não pode ser negativa");
        this.custoUnitario = validarNaoNegativo(
                custoUnitario,
                "Custo unitário não pode ser negativo");
        this.dataCadastro = Objects.requireNonNull(
                dataCadastro,
                "Data de cadastro é obrigatória");
        this.status = Status.ATIVO;
    }

    public BigDecimal calcularValorEstoque() {
        return custoUnitario
                .multiply(BigDecimal.valueOf(quantidadeEstoque))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void receberEstoque(int quantidade) {
        validarPositivo(quantidade, "Quantidade recebida deve ser maior que zero");
        quantidadeEstoque += quantidade;
    }

    public void retirarEstoque(int quantidade) {
        validarPositivo(quantidade, "Quantidade retirada deve ser maior que zero");

        if (quantidade > quantidadeEstoque) {
            throw new IllegalArgumentException("Estoque insuficiente");
        }

        quantidadeEstoque -= quantidade;
    }

    public void alterarDescricao(String novaDescricao) {
        this.descricao = validarTexto(novaDescricao, "Descrição é obrigatória");
    }

    public void alterarCustoUnitario(BigDecimal novoCusto) {
        this.custoUnitario = validarNaoNegativo(
                novoCusto,
                "Custo unitário não pode ser negativo");
    }

    public void ativar() {
        this.status = Status.ATIVO;
    }

    public void inativar() {
        this.status = Status.INATIVO;
    }

    void associarA(CategoriaPeca categoria) {
        Objects.requireNonNull(categoria, "Categoria é obrigatória");

        if (this.categoria != null && this.categoria != categoria) {
            throw new IllegalStateException("Peça já pertence a outra categoria");
        }

        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public BigDecimal getCustoUnitario() {
        return custoUnitario;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public Status getStatus() {
        return status;
    }

    public CategoriaPeca getCategoria() {
        return categoria;
    }

    private static String validarTexto(String texto, String mensagem) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return texto.trim();
    }

    private static int validarNaoNegativo(int valor, String mensagem) {
        if (valor < 0) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor;
    }

    private static BigDecimal validarNaoNegativo(BigDecimal valor, String mensagem) {
        Objects.requireNonNull(valor, mensagem);
        if (valor.signum() < 0) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor;
    }

    private static void validarPositivo(int valor, String mensagem) {
        if (valor <= 0) {
            throw new IllegalArgumentException(mensagem);
        }
    }
}
