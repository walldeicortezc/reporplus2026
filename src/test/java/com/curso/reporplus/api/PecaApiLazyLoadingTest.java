package com.curso.reporplus.api;

import com.curso.reporplus.domain.CategoriaPeca;
import com.curso.reporplus.domain.Fornecedor;
import com.curso.reporplus.domain.Peca;
import com.curso.reporplus.repository.CategoriaPecaRepository;
import com.curso.reporplus.repository.FornecedorRepository;
import com.curso.reporplus.repository.PecaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PecaApiLazyLoadingTest {

    @Autowired private WebApplicationContext context;
    @Autowired private CategoriaPecaRepository categoriaRepository;
    @Autowired private FornecedorRepository fornecedorRepository;
    @Autowired private PecaRepository pecaRepository;

    @Test
    void deveBuscarPecaComRelacionamentosForaDeTransacao() throws Exception {
        CategoriaPeca categoria = categoriaRepository.save(new CategoriaPeca("Categoria lazy"));
        Fornecedor fornecedor = fornecedorRepository.save(
                new Fornecedor("Fornecedor lazy", "44444444000190"));

        Peca peca = new Peca(
                "LAZY-001",
                "Peça para testar relacionamentos",
                5,
                new BigDecimal("20.00"),
                LocalDate.now());
        categoria.adicionarPeca(peca);
        peca.associarFornecedor(fornecedor);
        Long pecaId = pecaRepository.save(peca).getId();

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(get("/api/pecas/{id}", pecaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoriaNome").value("Categoria lazy"))
                .andExpect(jsonPath("$.fornecedorRazaoSocial").value("Fornecedor lazy"));
    }
}
