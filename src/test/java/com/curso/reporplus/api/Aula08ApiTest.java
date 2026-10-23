package com.curso.reporplus.api;

import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.domain.Peca;
import com.curso.reporplus.repository.CategoriaPecaRepository;
import com.curso.reporplus.repository.PecaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class Aula08ApiTest {

    @Autowired private WebApplicationContext context;
    @Autowired private CategoriaPecaRepository categoriaRepository;
    @Autowired private PecaRepository pecaRepository;
    @Autowired private EntityManager entityManager;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void deveAlterarCategoriaComPut() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Categoria antiga"));

        mockMvc.perform(put("/api/categorias-pecas/{id}", categoria.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Categoria atualizada\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Categoria atualizada"));
    }

    @Test
    void deveRetornar409AoRenomearCategoriaParaNomeExistente() throws Exception {
        categoriaRepository.save(new CategoriaPeca("Motor"));
        CategoriaPeca freios = categoriaRepository.save(new CategoriaPeca("Freios"));

        mockMvc.perform(put("/api/categorias-pecas/{id}", freios.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"motor\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void deveAlterarPecaComPut() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Motor"));
        Peca peca = salvarPeca("A08-PUT", "Filtro antigo", 10, 2, categoria);

        String json = """
                {
                  "descricao":"Filtro atualizado",
                  "custoUnitario":59.90,
                  "estoqueMinimo":4,
                  "categoriaId":%d,
                  "fornecedorId":null
                }
                """.formatted(categoria.getId());

        mockMvc.perform(put("/api/pecas/{id}", peca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Filtro atualizado"))
                .andExpect(jsonPath("$.estoqueMinimo").value(4));
    }

    @Test
    void deveRetornar404AoAlterarPecaInexistente() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Motor"));
        String json = """
                {"descricao":"Inexistente","custoUnitario":1.00,
                 "estoqueMinimo":1,"categoriaId":%d,"fornecedorId":null}
                """.formatted(categoria.getId());

        mockMvc.perform(put("/api/pecas/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAlterarStatusDaPeca() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Motor"));
        Peca peca = salvarPeca("A08-STATUS", "Filtro", 10, 2, categoria);

        mockMvc.perform(put("/api/pecas/{id}/status", peca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INATIVO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INATIVO"));
    }

    @Test
    void deveRetornar400ParaStatusInvalido() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Motor"));
        Peca peca = salvarPeca("A08-STATUS-ERRO", "Filtro", 10, 2, categoria);

        mockMvc.perform(put("/api/pecas/{id}/status", peca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DESCONHECIDO\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRegistrarEntradaDeEstoque() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Motor"));
        Peca peca = salvarPeca("A08-ENTRADA", "Filtro", 10, 2, categoria);

        mockMvc.perform(post("/api/pecas/{id}/estoque/entradas", peca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidadeEstoque").value(15));
    }

    @Test
    void saidaMaiorQueSaldoDeveRetornar400ESemAlterarEstoque() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Motor"));
        Peca peca = salvarPeca("A08-SAIDA", "Filtro", 10, 2, categoria);

        mockMvc.perform(post("/api/pecas/{id}/estoque/saidas", peca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\":11}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Estoque insuficiente"));

        entityManager.clear();
        assertEquals(10, pecaRepository.findById(peca.getId()).orElseThrow()
                .getQuantidadeEstoque());
    }

    @Test
    void devePesquisarPecasComFiltrosEPaginacao() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Filtros"));
        salvarPeca("A08-FILTRO-1", "Filtro de óleo premium", 1, 5, categoria);
        salvarPeca("A08-FILTRO-2", "Correia dentada", 10, 2, categoria);

        mockMvc.perform(get("/api/pecas")
                        .param("descricao", "óleo")
                        .param("abaixoEstoqueMinimo", "true")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "descricao,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo.length()").value(1))
                .andExpect(jsonPath("$.conteudo[0].codigo").value("A08-FILTRO-1"))
                .andExpect(jsonPath("$.pagina").value(0))
                .andExpect(jsonPath("$.tamanho").value(5))
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    void deveRetornar400ParaCampoDeOrdenacaoInvalido() throws Exception {
        mockMvc.perform(get("/api/pecas").param("sort", "fornecedor.razaoSocial,asc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Campo de ordenação inválido: fornecedor.razaoSocial"));
    }

    @Test
    void deveExcluirPecaCom204SemCorpo() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Motor"));
        Peca peca = salvarPeca("A08-DELETE", "Filtro", 10, 2, categoria);

        mockMvc.perform(delete("/api/pecas/{id}", peca.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        mockMvc.perform(get("/api/pecas/{id}", peca.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar409AoExcluirCategoriaComPecas() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Motor"));
        salvarPeca("A08-CAT-USO", "Filtro", 10, 2, categoria);

        mockMvc.perform(delete("/api/categorias-pecas/{id}", categoria.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Categoria de peça não pode ser excluída porque possui peças"));
    }

    private Peca salvarPeca(
            String codigo,
            String descricao,
            int quantidade,
            int estoqueMinimo,
            CategoriaPeca categoria) {
        Peca peca = new Peca(
                codigo,
                descricao,
                quantidade,
                new BigDecimal("25.90"),
                estoqueMinimo,
                LocalDate.of(2026, 10, 1));
        categoria.adicionarPeca(peca);
        return pecaRepository.save(peca);
    }
}
