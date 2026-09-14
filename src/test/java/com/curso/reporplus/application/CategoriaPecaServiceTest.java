package com.curso.reporplus.application;

import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.repository.CategoriaPecaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoriaPecaServiceTest {

    @Autowired
    private CategoriaPecaService service;

    @Autowired
    private CategoriaPecaRepository repository;

    @Test
    void deveCadastrarCategoria() {
        CategoriaPeca cadastrada = service.cadastrar("Freios");

        assertNotNull(cadastrada.getId());
        assertEquals("Freios", cadastrada.getNome());
        assertEquals(cadastrada.getId(),
                repository.findByNomeIgnoreCase("freios").orElseThrow().getId());
    }

    @Test
    void deveImpedirNomeDuplicadoIgnorandoMaiusculas() {
        service.cadastrar("Motor");

        assertThrows(
                RecursoDuplicadoException.class,
                () -> service.cadastrar("motor"));
    }

    @Test
    void deveInformarCategoriaNaoEncontrada() {
        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.buscarPorId(Long.MAX_VALUE));
    }

    @Test
    void deveListarCategorias() {
        CategoriaPeca eletrica = service.cadastrar("Elétrica");

        List<CategoriaPeca> categorias = service.listar();

        assertEquals(1, categorias.size());
        assertEquals(eletrica.getId(), categorias.getFirst().getId());
    }
}
