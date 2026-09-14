package com.curso.reporplus.migration;

import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MigracaoAula06Test {

    @Test
    void devePreservarPecaAntigaAoAplicarAula06() throws Exception {
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:h2:mem:migracao_aula06;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
                "sa", "", true);
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        aplicar(dataSource,
                "classpath:db/changelog/db.changelog-teste-aula-06.yaml");

        assertEquals(0, jdbc.queryForObject(
                "SELECT estoque_minimo FROM peca WHERE codigo = 'MOT-ANTIGA'", Integer.class));
        assertNull(jdbc.queryForObject(
                "SELECT fornecedor_id FROM peca WHERE codigo = 'MOT-ANTIGA'", Long.class));
    }

    private static void aplicar(DataSource dataSource, String changelog) throws Exception {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(changelog);
        liquibase.afterPropertiesSet();
    }
}
