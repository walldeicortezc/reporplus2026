package com.curso.reporplus.api.controller;

import com.curso.reporplus.api.dto.PecaRequest;
import com.curso.reporplus.api.dto.PecaResponse;
import com.curso.reporplus.api.mapper.PecaMapper;
import com.curso.reporplus.application.PecaService;
import com.curso.reporplus.domain.Peca;
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
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaService service;
    private final PecaMapper mapper;

    public PecaController(PecaService service, PecaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PecaResponse> cadastrar(@Valid @RequestBody PecaRequest request) {
        Peca cadastrada = service.cadastrar(
                mapper.toEntity(request), request.categoriaId(), request.fornecedorId());
        URI location = URI.create("/api/pecas/" + cadastrada.getId());
        return ResponseEntity.created(location).body(mapper.toResponse(cadastrada));
    }

    @GetMapping("/{id}")
    public PecaResponse buscarPorId(@PathVariable Long id) {
        return mapper.toResponse(service.buscarPorId(id));
    }

    @GetMapping
    public List<PecaResponse> listar() {
        return service.listar().stream().map(mapper::toResponse).toList();
    }
}
