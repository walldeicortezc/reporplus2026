package com.curso.reporplus.application;

import com.curso.reporplus.domain.Fornecedor;
import com.curso.reporplus.repository.FornecedorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FornecedorServiceTest {

    @Autowired
    private FornecedorService service;

    @Autowired
    private FornecedorRepository repository;

    @Test
    void deveCadastrarEBuscarFornecedor() {
        Fornecedor cadastrado = service.cadastrar("Agro Peças", "12345678000199");

        assertNotNull(cadastrado.getId());
        assertEquals(cadastrado.getId(), service.buscarPorId(cadastrado.getId()).getId());
        assertEquals(cadastrado.getId(), repository.findByCnpj(cadastrado.getCnpj()).orElseThrow().getId());
    }

    @Test
    void deveImpedirCnpjDuplicado() {
        service.cadastrar("Agro Peças", "12345678000199");

        assertThrows(RecursoDuplicadoException.class,
                () -> service.cadastrar("Outro Fornecedor", "12345678000199"));
    }

    @Test
    void deveInformarFornecedorInexistente() {
        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarPorId(Long.MAX_VALUE));
    }
}
