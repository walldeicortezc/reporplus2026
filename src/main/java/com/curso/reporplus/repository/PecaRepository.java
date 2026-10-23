package com.curso.reporplus.repository;

import com.curso.reporplus.domain.Peca;
import com.curso.reporplus.domain.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface PecaRepository extends
        JpaRepository<Peca, Long>,
        JpaSpecificationExecutor<Peca> {
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

    boolean existsByCategoriaId(Long categoriaId);

    @EntityGraph(attributePaths = {"categoria", "fornecedor"})
    @Override
    Page<Peca> findAll(Specification<Peca> specification, Pageable pageable);
}
