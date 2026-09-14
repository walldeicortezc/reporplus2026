package com.curso.reporplus.repository;

import com.curso.reporplus.domain.CategoriaPeca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaPecaRepository extends JpaRepository<CategoriaPeca, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}
