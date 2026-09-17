package com.curso.reporplus.repository;

import com.curso.reporplus.domain.Peca;
import com.curso.reporplus.domain.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PecaRepository extends JpaRepository<Peca, Long> {
    boolean existsByCodigo(String codigo);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    @Override
    Optional<Peca> findById(Long id);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    @Override
    List<Peca> findAll();

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    Optional<Peca> findByCodigo(String codigo);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    List<Peca> findByCategoriaId(Long categoriaId);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    List<Peca> findByStatus(Status status);
}
