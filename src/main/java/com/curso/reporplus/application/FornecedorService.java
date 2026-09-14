package com.curso.reporplus.application;

import com.curso.reporplus.domain.Fornecedor;
import com.curso.reporplus.repository.FornecedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional
    public Fornecedor cadastrar(String razaoSocial, String cnpj) {
        if (fornecedorRepository.existsByCnpj(cnpj)) {
            throw new RecursoDuplicadoException("CNPJ já cadastrado");
        }
        return fornecedorRepository.save(new Fornecedor(razaoSocial, cnpj));
    }

    @Transactional(readOnly = true)
    public Fornecedor buscarPorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Fornecedor não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Fornecedor> listar() {
        return fornecedorRepository.findAll();
    }
}
