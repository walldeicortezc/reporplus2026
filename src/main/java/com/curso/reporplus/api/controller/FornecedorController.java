package com.curso.reporplus.api.controller;

import com.curso.reporplus.api.dto.FornecedorRequest;
import com.curso.reporplus.api.dto.FornecedorResponse;
import com.curso.reporplus.api.mapper.FornecedorMapper;
import com.curso.reporplus.application.FornecedorService;
import com.curso.reporplus.domain.Fornecedor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorService service;
    private final FornecedorMapper mapper;

    public FornecedorController(FornecedorService service, FornecedorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<FornecedorResponse> cadastrar(
            @Valid @RequestBody FornecedorRequest request) {
        Fornecedor cadastrado = service.cadastrar(request.razaoSocial(), request.cnpj());
        URI location = URI.create("/api/fornecedores/" + cadastrado.getId());
        return ResponseEntity.created(location).body(mapper.toResponse(cadastrado));
    }

    @GetMapping("/{id}")
    public FornecedorResponse buscarPorId(@PathVariable Long id) {
        return mapper.toResponse(service.buscarPorId(id));
    }

    @GetMapping
    public List<FornecedorResponse> listar() {
        return service.listar().stream().map(mapper::toResponse).toList();
    }
}
