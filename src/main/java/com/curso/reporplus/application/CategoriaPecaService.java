package com.curso.reporplus.application;

import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.domain.Status;
import com.curso.reporplus.repository.CategoriaPecaRepository;
import com.curso.reporplus.repository.PecaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class CategoriaPecaService {

    private final CategoriaPecaRepository repository;
    private final PecaRepository pecaRepository;

    public CategoriaPecaService(
            CategoriaPecaRepository repository,
            PecaRepository pecaRepository) {
        this.repository = repository;
        this.pecaRepository = pecaRepository;
    }

    @Transactional
    public CategoriaPeca cadastrar(String nome) {
        String nomeNormalizado = nome == null ? null : nome.trim();
        if (repository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RecursoDuplicadoException("Nome da categoria já cadastrado");
        }

        return repository.save(new CategoriaPeca(nomeNormalizado));
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

    @Transactional
    public CategoriaPeca alterar(Long id, String nome) {
        String nomeNormalizado = nome == null ? null : nome.trim();
        if (repository.existsByNomeIgnoreCaseAndIdNot(nomeNormalizado, id)) {
            throw new RecursoDuplicadoException("Nome da categoria já cadastrado");
        }

        CategoriaPeca categoria = buscarEntidade(id);
        categoria.alterarNome(nomeNormalizado);
        return categoria;
    }

    @Transactional
    public CategoriaPeca alterarStatus(Long id, Status status) {
        CategoriaPeca categoria = buscarEntidade(id);
        if (status == Status.ATIVO) {
            categoria.ativar();
        } else {
            categoria.inativar();
        }
        return categoria;
    }

    @Transactional
    public void excluir(Long id) {
        CategoriaPeca categoria = buscarEntidade(id);
        if (pecaRepository.existsByCategoriaId(id)) {
            throw new RecursoEmUsoException(
                    "Categoria de peça não pode ser excluída porque possui peças");
        }
        repository.delete(categoria);
        repository.flush();
    }

    @Transactional(readOnly = true)
    public Page<CategoriaPeca> pesquisar(
            String nome,
            Status status,
            Pageable pageable) {
        validarOrdenacao(pageable, Set.of("id", "nome", "status"));

        Specification<CategoriaPeca> filtros =
                (root, query, cb) -> cb.conjunction();
        if (nome != null && !nome.isBlank()) {
            String trecho = "%" + nome.trim().toLowerCase(Locale.ROOT) + "%";
            filtros = filtros.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nome")), trecho));
        }
        if (status != null) {
            filtros = filtros.and((root, query, cb) ->
                    cb.equal(root.get("status"), status));
        }
        return repository.findAll(filtros, pageable);
    }

    private CategoriaPeca buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria de peça não encontrada"));
    }

    private static void validarOrdenacao(Pageable pageable, Set<String> camposPermitidos) {
        pageable.getSort().forEach(ordem -> {
            if (!camposPermitidos.contains(ordem.getProperty())) {
                throw new IllegalArgumentException(
                        "Campo de ordenação inválido: " + ordem.getProperty());
            }
        });
    }
}
