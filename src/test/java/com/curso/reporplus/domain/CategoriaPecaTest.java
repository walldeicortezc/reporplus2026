package com.curso.reporplus.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoriaPecaTest {

    @Test
    void deveManterOsDoisLadosDaAssociacao() {
        CategoriaPeca categoria = new CategoriaPeca("Motor");
        Peca peca = novaPeca("MOT-FIL-001");

        categoria.adicionarPeca(peca);

        assertSame(categoria, peca.getCategoria());
        assertSame(peca, categoria.getPecas().get(0));
    }

    @Test
    void deveProtegerAListaInterna() {
        CategoriaPeca categoria = new CategoriaPeca("Motor");

        assertThrows(
                UnsupportedOperationException.class,
                () -> categoria.getPecas().add(novaPeca("MOT-FIL-001")));
    }

    @Test
    void deveRejeitarCodigoDuplicadoNaCategoria() {
        CategoriaPeca categoria = new CategoriaPeca("Motor");
        categoria.adicionarPeca(novaPeca("MOT-FIL-001"));

        assertThrows(
                IllegalArgumentException.class,
                () -> categoria.adicionarPeca(novaPeca("MOT-FIL-001")));
    }

    private static Peca novaPeca(String codigo) {
        return new Peca(
                codigo,
                "Filtro de óleo",
                10,
                new BigDecimal("48.90"),
                LocalDate.of(2026, 9, 12));
    }
}
