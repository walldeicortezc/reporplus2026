package com.curso.reporplus.application;

import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.domain.Peca;
import com.curso.reporplus.domain.Status;
import com.curso.reporplus.repository.CategoriaPecaRepository;
import com.curso.reporplus.repository.PecaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PecaServiceTest {

    @Autowired
    private PecaService service;

    @Autowired
    private PecaRepository pecaRepository;

    @Autowired
    private CategoriaPecaRepository categoriaRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void deveCadastrarPecaComCategoria() {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Motor"));

        Peca cadastrada = service.cadastrar(novaPeca("MOT-001"), categoria.getId());

        assertNotNull(cadastrada.getId());
        assertEquals(categoria.getId(), cadastrada.getCategoria().getId());
    }

    @Test
    void deveImpedirCodigoDuplicado() {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Freios"));
        service.cadastrar(novaPeca("FRE-001"), categoria.getId());

        assertThrows(
                RecursoDuplicadoException.class,
                () -> service.cadastrar(novaPeca("FRE-001"), categoria.getId()));
    }

    @Test
    void deveFazerRollbackQuandoCategoriaNaoExiste() {
        Peca peca = novaPeca("SEM-CATEGORIA");

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.cadastrar(peca, Long.MAX_VALUE));

        assertFalse(pecaRepository.existsByCodigo(peca.getCodigo()));
    }

    @Test
    void deveAtualizarEstoquePorDirtyChecking() {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Hidráulica"));
        Peca cadastrada = service.cadastrar(novaPeca("HID-001"), categoria.getId());

        service.receberEstoque(cadastrada.getId(), 5);
        entityManager.flush();
        entityManager.clear();

        Peca recuperada = pecaRepository.findById(cadastrada.getId()).orElseThrow();
        assertEquals(15, recuperada.getQuantidadeEstoque());
    }

    @Test
    void deveConsultarPorRelacionamentoEStatus() {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Elétrica"));
        Peca cadastrada = service.cadastrar(novaPeca("ELE-001"), categoria.getId());

        assertEquals(cadastrada.getId(),
                service.listarPorCategoria(categoria.getId()).getFirst().getId());
        assertEquals(cadastrada.getId(),
                service.listarPorStatus(Status.ATIVO).getFirst().getId());
    }

    private Peca novaPeca(String codigo) {
        return new Peca(
                codigo,
                "Peça de teste",
                10,
                new BigDecimal("25.90"),
                LocalDate.of(2026, 9, 14));
    }
}
