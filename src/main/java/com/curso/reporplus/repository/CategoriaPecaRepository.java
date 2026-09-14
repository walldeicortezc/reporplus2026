package com.curso.reporplus.repository;

import com.curso.reporplus.domain.CategoriaPeca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaPecaRepository extends JpaRepository<CategoriaPeca, Long> {
    boolean existsByNomeIgnoreCase(String nome);

    Optional<CategoriaPeca> findByNomeIgnoreCase(String nome);
}
