package com.curso.reporplus.repository;

import com.curso.reporplus.domain.Peca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PecaRepository extends JpaRepository<Peca, Long> {
    boolean existsByCodigo(String codigo);
    List<Peca> findByCategoriaIdOrderById(Long categoriaId);
}
