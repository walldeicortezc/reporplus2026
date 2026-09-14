package com.curso.reporplus.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class PersistenciaJpaTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    void devePersistirERelerCategoriaComPeca() {
        CategoriaPeca categoria = new CategoriaPeca("Motor");
        Peca peca = new Peca(
                "MOT-FIL-001",
                "Filtro de óleo",
                12,
                new BigDecimal("48.90"),
                LocalDate.of(2026, 9, 13));
        categoria.adicionarPeca(peca);

        entityManager.persist(categoria);
        entityManager.persist(peca);
        entityManager.flush();
        Long pecaId = peca.getId();
        entityManager.clear();

        Peca recuperada = entityManager.find(Peca.class, pecaId);

        assertNotNull(recuperada);
        assertEquals("Filtro de óleo", recuperada.getDescricao());
        assertEquals("Motor", recuperada.getCategoria().getNome());
    }

    @Test
    @Transactional
    void bancoDeveImpedirQuantidadeNegativa() {
        jdbcTemplate.update(
                "INSERT INTO categoria_peca (nome, status) VALUES ('Teste', 'ATIVO')");
        Long categoriaId = jdbcTemplate.queryForObject(
                "SELECT id FROM categoria_peca WHERE nome = 'Teste'",
                Long.class);

        assertThrows(DataIntegrityViolationException.class, () -> jdbcTemplate.update(
                """
                INSERT INTO peca
                    (codigo, descricao, quantidade_estoque, custo_unitario,
                     data_cadastro, status, categoria_peca_id)
                VALUES ('INVALIDA', 'Inválida', -1, 10.00,
                        DATE '2026-09-13', 'ATIVO', ?)
                """,
                categoriaId));
    }
}
