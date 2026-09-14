package com.curso.reporplus.application;

import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.domain.Peca;
import com.curso.reporplus.domain.Status;
import com.curso.reporplus.repository.CategoriaPecaRepository;
import com.curso.reporplus.repository.FornecedorRepository;
import com.curso.reporplus.repository.PecaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Peca peca = buscarPorId(id);
        peca.receberEstoque(quantidade);
        return peca;
    }
}
