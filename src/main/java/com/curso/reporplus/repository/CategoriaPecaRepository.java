package com.curso.reporplus.repository;

import com.curso.reporplus.domain.CategoriaPeca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoriaPecaRepository extends
        JpaRepository<CategoriaPeca, Long>,
        JpaSpecificationExecutor<CategoriaPeca> {
    boolean existsByNomeIgnoreCase(String nome);

    Optional<CategoriaPeca> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
