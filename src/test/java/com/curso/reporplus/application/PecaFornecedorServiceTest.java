package com.curso.reporplus.application;

import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.domain.Fornecedor;
import com.curso.reporplus.domain.Peca;
import com.curso.reporplus.repository.CategoriaPecaRepository;
import com.curso.reporplus.repository.FornecedorRepository;
import com.curso.reporplus.repository.PecaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PecaFornecedorServiceTest {

    @Autowired private PecaService pecaService;
    @Autowired private CategoriaPecaRepository categoriaRepository;
    @Autowired private FornecedorRepository fornecedorRepository;
    @Autowired private PecaRepository pecaRepository;

    @Test
    void devePermitirPecaSemFornecedor() {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Filtros"));

        Peca cadastrada = pecaService.cadastrar(novaPeca("FIL-001"), categoria.getId());

        assertNull(cadastrada.getFornecedor());
    }

    @Test
    void deveAssociarFornecedorNaPeca() {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Rolamentos"));
        Fornecedor fornecedor = fornecedorRepository.save(
                new Fornecedor("Peças Brasil", "12345678000199"));

        Peca cadastrada = pecaService.cadastrar(
                novaPeca("ROL-001"), categoria.getId(), fornecedor.getId());

        assertEquals(fornecedor.getId(), cadastrada.getFornecedor().getId());
    }

    @Test
    void deveFazerRollbackQuandoFornecedorNaoExiste() {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Correias"));
        Peca peca = novaPeca("COR-001");

        assertThrows(RecursoNaoEncontradoException.class,
                () -> pecaService.cadastrar(peca, categoria.getId(), Long.MAX_VALUE));
        assertFalse(pecaRepository.existsByCodigo("COR-001"));
    }

    private static Peca novaPeca(String codigo) {
        return new Peca(codigo, "Peça de teste", 3,
                new BigDecimal("19.90"), 5,
                LocalDate.of(2026, 9, 14));
    }
}
