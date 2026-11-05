package com.curso.reporplus.repository;

import com.curso.reporplus.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {
    boolean existsByPessoaId(Long pessoaId);

    @EntityGraph(attributePaths = "pessoa")
    Optional<Cliente> findOneById(Long id);

    @Override
    @EntityGraph(attributePaths = "pessoa")
    Page<Cliente> findAll(Specification<Cliente> specification, Pageable pageable);
}
