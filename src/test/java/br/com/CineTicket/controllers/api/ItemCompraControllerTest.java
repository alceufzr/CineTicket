package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Compra;
import br.com.CineTicket.models.ItemCompra;
import br.com.CineTicket.models.Produto;
import br.com.CineTicket.repositories.ItemCompraRepository;
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

@WebMvcTest(ItemCompraController.class)
public class ItemCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemCompraRepository itemCompraRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todos os itens de compra com sucesso")
    void deveListarTodasAsCompras() throws Exception {
        ItemCompra i1 = new ItemCompra(1, new Compra(), new Produto(), "Ingresso Inteira", 2, 30.00, 3.00);
        ItemCompra i2 = new ItemCompra(2, new Compra(), new Produto(), "Combo Pipoca Doce", 1, 45.00, 4.50);

        when(itemCompraRepository.findAll()).thenReturn(List.of(i1, i2));

        mockMvc.perform(get("/itens-compra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idItemCompra").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Ingresso Inteira"))
                .andExpect(jsonPath("$[0].valor").value(30.00))
                .andExpect(jsonPath("$[1].idItemCompra").value(2))
                .andExpect(jsonPath("$[1].descricao").value("Combo Pipoca Doce"))
                .andExpect(jsonPath("$[1].valor").value(45.00));
    }

    @Test
    @DisplayName("Deve buscar item de compra por ID com sucesso")
    void deveBuscarCompraPorId() throws Exception {
        ItemCompra itemCompra = new ItemCompra(1, new Compra(), new Produto(), "Ingresso Meia", 1, 15.00, 1.50);

        when(itemCompraRepository.findById(1)).thenReturn(Optional.of(itemCompra));

        mockMvc.perform(get("/itens-compra/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idItemCompra").value(1))
                .andExpect(jsonPath("$.descricao").value("Ingresso Meia"))
                .andExpect(jsonPath("$.valor").value(15.00));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do item de compra não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(itemCompraRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/itens-compra/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar um item de compra com sucesso")
    void deveSalvarUmaCompra() throws Exception {
        ItemCompra itemCompra = new ItemCompra(1, new Compra(), new Produto(), "Nachos", 1, 22.00, 2.20);

        when(itemCompraRepository.save(any(ItemCompra.class))).thenReturn(itemCompra);

        mockMvc.perform(post("/itens-compra")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCompra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idItemCompra").value(1))
                .andExpect(jsonPath("$.descricao").value("Nachos"))
                .andExpect(jsonPath("$.valor").value(22.00));
    }

    @Test
    @DisplayName("Deve atualizar dados do item de compra com sucesso")
    void deveAtualizarUmaCompra() throws Exception {
        ItemCompra existente = new ItemCompra(1, new Compra(), new Produto(), "Suco Natural", 1, 8.00, 0.80);
        ItemCompra atualizado = new ItemCompra(1, new Compra(), new Produto(), "Suco Natural Duplo", 2, 16.00, 1.60);

        when(itemCompraRepository.findById(1)).thenReturn(Optional.of(existente));
        when(itemCompraRepository.save(any(ItemCompra.class))).thenReturn(atualizado);

        mockMvc.perform(put("/itens-compra/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Suco Natural Duplo"))
                .andExpect(jsonPath("$.valor").value(16.00));
    }

    @Test
    @DisplayName("Deve deletar item de compra por ID com sucesso")
    void deveDeletarUmaCompra() throws Exception {
        ItemCompra itemCompra = new ItemCompra(1, new Compra(), new Produto(), "Chocolate", 1, 7.00, 0.70);

        when(itemCompraRepository.findById(1)).thenReturn(Optional.of(itemCompra));
        doNothing().when(itemCompraRepository).delete(itemCompra);

        mockMvc.perform(delete("/itens-compra/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do item de compra não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(itemCompraRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/itens-compra/99"))
                .andExpect(status().isNotFound());
    }
}