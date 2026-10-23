package com.curso.reporplus.application;

import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.domain.Fornecedor;
import com.curso.reporplus.domain.Peca;
import com.curso.reporplus.domain.Status;
import com.curso.reporplus.repository.CategoriaPecaRepository;
import com.curso.reporplus.repository.FornecedorRepository;
import com.curso.reporplus.repository.PecaRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Set;

@Service
public class PecaService {

    private final PecaRepository pecaRepository;
    private final CategoriaPecaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    public PecaService(
            PecaRepository pecaRepository,
            CategoriaPecaRepository categoriaRepository,
            FornecedorRepository fornecedorRepository) {
        this.pecaRepository = pecaRepository;
        this.categoriaRepository = categoriaRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional
    public Peca cadastrar(Peca peca, Long categoriaId) {
        return cadastrar(peca, categoriaId, null);
    }

    @Transactional
    public Peca cadastrar(Peca peca, Long categoriaId, Long fornecedorId) {
        if (pecaRepository.existsByCodigo(peca.getCodigo())) {
            throw new RecursoDuplicadoException("Código da peça já cadastrado");
        }

        CategoriaPeca categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria de peça não encontrada"));

        categoria.adicionarPeca(peca);

        if (fornecedorId != null) {
            peca.associarFornecedor(fornecedorRepository.findById(fornecedorId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Fornecedor não encontrado")));
        }

        return pecaRepository.save(peca);
    }

    @Transactional(readOnly = true)
    public Peca buscarPorId(Long id) {
        return pecaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Peça não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Peca> listarPorCategoria(Long categoriaId) {
        return pecaRepository.findByCategoriaId(categoriaId);
    }

    @Transactional(readOnly = true)
    public List<Peca> listarPorStatus(Status status) {
        return pecaRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Peca> listar() {
        return pecaRepository.findAll();
    }

    @Transactional
    public Peca receberEstoque(Long id, int quantidade) {
        Peca peca = buscarEntidade(id);
        peca.receberEstoque(quantidade);
        return peca;
    }

    @Transactional
    public Peca retirarEstoque(Long id, int quantidade) {
        Peca peca = buscarEntidade(id);
        peca.retirarEstoque(quantidade);
        return peca;
    }

    @Transactional
    public Peca alterar(
            Long id,
            String descricao,
            BigDecimal custoUnitario,
            int estoqueMinimo,
            Long categoriaId,
            Long fornecedorId) {
        Peca peca = buscarEntidade(id);
        CategoriaPeca categoria = buscarCategoria(categoriaId);
        Fornecedor fornecedor = fornecedorId == null
                ? null
                : buscarFornecedor(fornecedorId);

        peca.alterarDescricao(descricao);
        peca.alterarCustoUnitario(custoUnitario);
        peca.alterarEstoqueMinimo(estoqueMinimo);
        peca.alterarCategoria(categoria);
        peca.alterarFornecedor(fornecedor);
        return peca;
    }

    @Transactional
    public Peca alterarStatus(Long id, Status status) {
        Peca peca = buscarEntidade(id);
        if (status == Status.ATIVO) {
            peca.ativar();
        } else {
            peca.inativar();
        }
        return peca;
    }

    @Transactional
    public void excluir(Long id) {
        Peca peca = buscarEntidade(id);
        pecaRepository.delete(peca);
        pecaRepository.flush();
    }

    @Transactional(readOnly = true)
    public Page<Peca> pesquisar(
            String descricao,
            Status status,
            Long categoriaId,
            Long fornecedorId,
            Boolean abaixoEstoqueMinimo,
            Pageable pageable) {
        validarOrdenacao(pageable, Set.of(
                "id", "codigo", "descricao", "quantidadeEstoque",
                "custoUnitario", "estoqueMinimo", "dataCadastro", "status"));

        Specification<Peca> filtros = (root, query, cb) -> cb.conjunction();
        if (descricao != null && !descricao.isBlank()) {
            String trecho = "%" + descricao.trim().toLowerCase(Locale.ROOT) + "%";
            filtros = filtros.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("descricao")), trecho));
        }
        if (status != null) {
            filtros = filtros.and((root, query, cb) ->
                    cb.equal(root.get("status"), status));
        }
        if (categoriaId != null) {
            filtros = filtros.and((root, query, cb) ->
                    cb.equal(root.get("categoria").get("id"), categoriaId));
        }
        if (fornecedorId != null) {
            filtros = filtros.and((root, query, cb) ->
                    cb.equal(root.get("fornecedor").get("id"), fornecedorId));
        }
        if (Boolean.TRUE.equals(abaixoEstoqueMinimo)) {
            filtros = filtros.and((root, query, cb) ->
                    cb.lessThan(root.get("quantidadeEstoque"), root.get("estoqueMinimo")));
        }
        return pecaRepository.findAll(filtros, pageable);
    }

    private Peca buscarEntidade(Long id) {
        return pecaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));
    }

    private CategoriaPeca buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria de peça não encontrada"));
    }

    private Fornecedor buscarFornecedor(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Fornecedor não encontrado"));
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
