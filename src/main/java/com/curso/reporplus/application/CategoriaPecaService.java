package com.curso.reporplus.application;

import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.repository.CategoriaPecaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaPecaService {

    private final CategoriaPecaRepository repository;

    public CategoriaPecaService(CategoriaPecaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CategoriaPeca cadastrar(String nome) {
        if (repository.existsByNomeIgnoreCase(nome)) {
            throw new RecursoDuplicadoException("Nome da categoria já cadastrado");
        }

        return repository.save(new CategoriaPeca(nome));
    }

    @Transactional(readOnly = true)
    public CategoriaPeca buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria de peça não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<CategoriaPeca> listar() {
        return repository.findAll();
    }
}
