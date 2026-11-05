package com.curso.reporplus.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import java.util.Objects;

@Entity
@Table(
        name = "item_venda",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_item_venda_peca",
                columnNames = {"venda_id", "peca_id"}))
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "venda_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_venda_venda"))
    private Venda venda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "peca_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_venda_peca"))
    private Peca peca;

    @Column(nullable = false)
    private int quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 18, scale = 2)
    private BigDecimal custoUnitario;

    protected ItemVenda() {
    }

    ItemVenda(
            Venda venda,
            Peca peca,
            int quantidade,
            BigDecimal custoUnitario) {
        this.venda = Objects.requireNonNull(venda, "Venda é obrigatória");
        this.peca = Objects.requireNonNull(peca, "Peca é obrigatório");
        this.quantidade = validarPositivo(quantidade, "Quantidade deve ser maior que zero");
        this.custoUnitario = validarNaoNegativo(
                custoUnitario,
                "Custo unitário não pode ser negativo");
    }

    public BigDecimal calcularSubtotal() {
        return quantidade
                == 0 ? BigDecimal.ZERO.setScale(2) : custoUnitario
                .multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Long getId() {
        return id;
    }

    public Venda getVenda() {
        return venda;
    }

    public Peca getPeca() {
        return peca;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValorUnitario() {
        return custoUnitario;
    }

    private static int validarPositivo(int valor, String mensagem) {
        if (valor <= 0) {
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
}
