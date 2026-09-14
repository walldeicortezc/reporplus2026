package com.curso.reporplus.api.controller;

import com.curso.reporplus.api.dto.CategoriaPecaRequest;
import com.curso.reporplus.api.dto.CategoriaPecaResponse;
import com.curso.reporplus.api.mapper.CategoriaPecaMapper;
import com.curso.reporplus.application.CategoriaPecaService;
import com.curso.reporplus.domain.CategoriaPeca;
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
@RequestMapping("/api/categorias-pecas")
public class CategoriaPecaController {

    private final CategoriaPecaService service;
    private final CategoriaPecaMapper mapper;

    public CategoriaPecaController(CategoriaPecaService service, CategoriaPecaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CategoriaPecaResponse> cadastrar(
            @Valid @RequestBody CategoriaPecaRequest request) {
        CategoriaPeca cadastrada = service.cadastrar(request.nome());
        URI location = URI.create("/api/categorias-pecas/" + cadastrada.getId());
        return ResponseEntity.created(location).body(mapper.toResponse(cadastrada));
    }

    @GetMapping("/{id}")
    public CategoriaPecaResponse buscarPorId(@PathVariable Long id) {
        return mapper.toResponse(service.buscarPorId(id));
    }

    @GetMapping
    public List<CategoriaPecaResponse> listar() {
        return service.listar().stream().map(mapper::toResponse).toList();
    }
}
