package com.curso.reporplus.repository;

import com.curso.reporplus.domain.Peca;
import com.curso.reporplus.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PecaRepository extends JpaRepository<Peca, Long> {
    boolean existsByCodigo(String codigo);

    Optional<Peca> findByCodigo(String codigo);

    List<Peca> findByCategoriaId(Long categoriaId);

    List<Peca> findByStatus(Status status);
}
