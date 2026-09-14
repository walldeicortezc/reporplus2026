package com.curso.reporplus.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FornecedorTest {

    @Test
    void deveCriarFornecedorAtivo() {
        Fornecedor fornecedor = new Fornecedor("Peças Brasil", "12345678000199");

        assertEquals("Peças Brasil", fornecedor.getRazaoSocial());
        assertEquals(Status.ATIVO, fornecedor.getStatus());
    }

    @Test
    void deveRejeitarCnpjForaDoFormato() {
        assertThrows(IllegalArgumentException.class,
                () -> new Fornecedor("Peças Brasil", "12.345/0001"));
    }

    @Test
    void deveInativarEAtivarFornecedor() {
        Fornecedor fornecedor = new Fornecedor("Peças Brasil", "12345678000199");

        fornecedor.inativar();
        assertEquals(Status.INATIVO, fornecedor.getStatus());

        fornecedor.ativar();
        assertEquals(Status.ATIVO, fornecedor.getStatus());
    }
}
