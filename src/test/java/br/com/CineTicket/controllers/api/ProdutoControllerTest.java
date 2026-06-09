package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Categoria;
import br.com.CineTicket.models.Produto;
import br.com.CineTicket.repositories.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoRepository produtoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todos os produtos com sucesso")
    void deveListarTodosOsProdutos() throws Exception {
        Produto p1 = new Produto(1, new Categoria(), "Pipoca Grande", 25.00, 100, 2.50);
        Produto p2 = new Produto(2, new Categoria(), "Refrigerante 500ml", 12.00, 200, 1.20);

        when(produtoRepository.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idProduto").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Pipoca Grande"))
                .andExpect(jsonPath("$[0].valor").value(25.00))
                .andExpect(jsonPath("$[1].idProduto").value(2))
                .andExpect(jsonPath("$[1].descricao").value("Refrigerante 500ml"))
                .andExpect(jsonPath("$[1].valor").value(12.00));
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void deveBuscarProdutoPorId() throws Exception {
        Produto produto = new Produto(1, new Categoria(), "Chocolate M&Ms", 10.00, 150, 1.00);

        when(produtoRepository.findById(1)).thenReturn(Optional.of(produto));

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduto").value(1))
                .andExpect(jsonPath("$.descricao").value("Chocolate M&Ms"))
                .andExpect(jsonPath("$.valor").value(10.00));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do produto não registrar na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(produtoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/produtos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar um produto com sucesso")
    void deveSalvarUmProduto() throws Exception {
        Produto produto = new Produto(1, new Categoria(), "Combo Casal", 55.00, 50, 5.50);

        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduto").value(1))
                .andExpect(jsonPath("$.descricao").value("Combo Casal"))
                .andExpect(jsonPath("$.valor").value(55.00));
    }

    @Test
    @DisplayName("Deve atualizar dados do produto com sucesso")
    void deveAtualizarUmaCompra() throws Exception {
        Produto existente = new Produto(1, new Categoria(), "Água Mineral", 5.00, 80, 0.50);
        Produto atualizado = new Produto(1, new Categoria(), "Água Mineral com Gás", 6.00, 75, 0.60);

        when(produtoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(any(Produto.class))).thenReturn(atualizado);

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Água Mineral com Gás"))
                .andExpect(jsonPath("$.valor").value(6.00));
    }

    @Test
    @DisplayName("Deve deletar produto por ID com sucesso")
    void deveDeletarUmaCompra() throws Exception {
        Produto produto = new Produto(1, new Categoria(), "Nacho com Queijo", 18.00, 40, 1.80);

        when(produtoRepository.findById(1)).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepository).delete(produto);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do produto não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(produtoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/produtos/99"))
                .andExpect(status().isNotFound());
    }
}