package com.curso.reporplus.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PecaTest {

    @Test
    void deveCriarPecaValidaAtiva() {
        Peca peca = novaPeca(10, "48.90");

        assertEquals("MOT-FIL-001", peca.getCodigo());
        assertEquals(Status.ATIVO, peca.getStatus());
    }

    @Test
    void deveCalcularValorDoEstoque() {
        Peca peca = novaPeca(12, "48.90");

        assertEquals(new BigDecimal("586.80"), peca.calcularValorEstoque());
    }

    @Test
    void deveReceberERetirarEstoque() {
        Peca peca = novaPeca(5, "10.00");

        peca.receberEstoque(3);
        peca.retirarEstoque(2);

        assertEquals(6, peca.getQuantidadeEstoque());
    }

    @Test
    void deveRejeitarEstoqueInicialNegativo() {
        assertThrows(IllegalArgumentException.class, () -> novaPeca(-1, "10.00"));
    }

    @Test
    void deveRejeitarCustoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> novaPeca(1, "-0.01"));
    }

    @Test
    void deveRejeitarRetiradaComEstoqueInsuficiente() {
        Peca peca = novaPeca(2, "10.00");

        assertThrows(IllegalArgumentException.class, () -> peca.retirarEstoque(3));
    }

    @Test
    void construtorAntigoDeveManterCompatibilidade() {
        Peca peca = novaPeca(2, "10.00");

        assertEquals(0, peca.getEstoqueMinimo());
        assertNull(peca.getFornecedor());
    }

    @Test
    void deveIdentificarEstoqueAbaixoDoMinimo() {
        Peca peca = new Peca(
                "MOT-FIL-002", "Filtro de ar", 2,
                new BigDecimal("25.00"), 5,
                LocalDate.of(2026, 9, 14));

        assertTrue(peca.estaAbaixoDoEstoqueMinimo());
        peca.receberEstoque(3);
        assertFalse(peca.estaAbaixoDoEstoqueMinimo());
    }

    @Test
    void deveRejeitarEstoqueMinimoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Peca(
                "MOT-FIL-003", "Filtro", 2,
                new BigDecimal("25.00"), -1,
                LocalDate.of(2026, 9, 14)));
    }

    private static Peca novaPeca(int quantidade, String custo) {
        return new Peca(
                "MOT-FIL-001",
                "Filtro de óleo",
                quantidade,
                new BigDecimal(custo),
                LocalDate.of(2026, 9, 12));
    }
}
