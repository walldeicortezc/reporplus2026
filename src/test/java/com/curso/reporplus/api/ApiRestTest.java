package com.curso.reporplus.api;

import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.domain.Fornecedor;
import com.curso.reporplus.repository.CategoriaPecaRepository;
import com.curso.reporplus.repository.FornecedorRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ApiRestTest {

    @Autowired private WebApplicationContext context;
    @Autowired private CategoriaPecaRepository categoriaRepository;
    @Autowired private FornecedorRepository fornecedorRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void deveCadastrarCategoriaERetornar201() throws Exception {
        mockMvc.perform(post("/api/categorias-pecas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Suspensao API\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nome").value("Suspensao API"));
    }

    @Test
    void deveCadastrarFornecedorERetornar201() throws Exception {
        mockMvc.perform(post("/api/fornecedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"razaoSocial":"Fornecedor API","cnpj":"22222222000192"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.cnpj").value("22222222000192"));
    }

    @Test
    void deveCadastrarPecaRelacionadaERetornar201() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Motor API"));
        Fornecedor fornecedor = fornecedorRepository.save(
                new Fornecedor("Distribuidora API", "33333333000191"));

        String json = """
                {
                  "codigo":"API-001",
                  "descricao":"Filtro criado pela API",
                  "quantidadeEstoque":10,
                  "custoUnitario":49.90,
                  "estoqueMinimo":2,
                  "categoriaId":%d,
                  "fornecedorId":%d
                }
                """.formatted(categoria.getId(), fornecedor.getId());

        mockMvc.perform(post("/api/pecas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.codigo").value("API-001"))
                .andExpect(jsonPath("$.categoriaId").value(categoria.getId()))
                .andExpect(jsonPath("$.fornecedorId").value(fornecedor.getId()));
    }

    @Test
    void deveListarPecasCom200() throws Exception {
        mockMvc.perform(get("/api/pecas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveRetornar400ComCamposInvalidos() throws Exception {
        mockMvc.perform(post("/api/pecas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "codigo":"",
                                  "descricao":"",
                                  "quantidadeEstoque":-1,
                                  "custoUnitario":-1,
                                  "estoqueMinimo":-1,
                                  "categoriaId":0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fields.descricao").exists());
    }

    @Test
    void deveRetornar404ParaPecaInexistente() throws Exception {
        mockMvc.perform(get("/api/pecas/999999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/pecas/999999999"));
    }

    @Test
    void deveRetornar409ParaCategoriaDuplicada() throws Exception {
        categoriaRepository.save(new CategoriaPeca("Freios API"));

        mockMvc.perform(post("/api/categorias-pecas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"freios api\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void deveRetornar400ParaJsonMalformado() throws Exception {
        mockMvc.perform(post("/api/fornecedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"razaoSocial\":\"Sem fechamento\""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("JSON inválido"));
    }
}
