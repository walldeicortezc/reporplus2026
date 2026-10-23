package com.curso.reporplus.api.controller;

import com.curso.reporplus.api.dto.PecaRequest;
import com.curso.reporplus.api.dto.PecaAtualizacaoRequest;
import com.curso.reporplus.api.dto.PecaResponse;
import com.curso.reporplus.api.dto.MovimentacaoEstoqueRequest;
import com.curso.reporplus.api.dto.PaginaResponse;
import com.curso.reporplus.api.dto.StatusRequest;
import com.curso.reporplus.api.mapper.PecaMapper;
import com.curso.reporplus.application.PecaService;
import com.curso.reporplus.domain.Peca;
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
    public PaginaResponse<PecaResponse> pesquisar(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long fornecedorId,
            @RequestParam(required = false) Boolean abaixoEstoqueMinimo,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return PaginaResponse.de(
                service.pesquisar(descricao, status, categoriaId, fornecedorId,
                        abaixoEstoqueMinimo, pageable),
                mapper::toResponse);
    }

    @PutMapping("/{id}")
    public PecaResponse alterar(
            @PathVariable Long id,
            @Valid @RequestBody PecaAtualizacaoRequest request) {
        return mapper.toResponse(service.alterar(
                id,
                request.descricao(),
                request.custoUnitario(),
                request.estoqueMinimo(),
                request.categoriaId(),
                request.fornecedorId()));
    }

    @PutMapping("/{id}/status")
    public PecaResponse alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusRequest request) {
        return mapper.toResponse(service.alterarStatus(id, request.status()));
    }

    @PostMapping("/{id}/estoque/entradas")
    public PecaResponse receberEstoque(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoEstoqueRequest request) {
        return mapper.toResponse(service.receberEstoque(id, request.quantidade()));
    }

    @PostMapping("/{id}/estoque/saidas")
    public PecaResponse retirarEstoque(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoEstoqueRequest request) {
        return mapper.toResponse(service.retirarEstoque(id, request.quantidade()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
