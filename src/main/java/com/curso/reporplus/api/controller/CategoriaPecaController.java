package com.curso.reporplus.api.controller;

import com.curso.reporplus.api.dto.CategoriaPecaRequest;
import com.curso.reporplus.api.dto.CategoriaPecaAtualizacaoRequest;
import com.curso.reporplus.api.dto.CategoriaPecaResponse;
import com.curso.reporplus.api.dto.PaginaResponse;
import com.curso.reporplus.api.dto.StatusRequest;
import com.curso.reporplus.api.mapper.CategoriaPecaMapper;
import com.curso.reporplus.application.CategoriaPecaService;
import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.domain.Status;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

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
    public PaginaResponse<CategoriaPecaResponse> pesquisar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Status status,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return PaginaResponse.de(service.pesquisar(nome, status, pageable), mapper::toResponse);
    }

    @PutMapping("/{id}")
    public CategoriaPecaResponse alterar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaPecaAtualizacaoRequest request) {
        return mapper.toResponse(service.alterar(id, request.nome()));
    }

    @PutMapping("/{id}/status")
    public CategoriaPecaResponse alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusRequest request) {
        return mapper.toResponse(service.alterarStatus(id, request.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
