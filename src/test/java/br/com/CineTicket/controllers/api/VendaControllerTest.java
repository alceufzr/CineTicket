package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Cliente;
import br.com.CineTicket.models.Funcionario;
import br.com.CineTicket.models.Venda;
import br.com.CineTicket.repositories.VendaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendaController.class)
public class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendaRepository vendaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todas as vendas com sucesso")
    void deveListarTodasAsVendas() throws Exception {
        Cliente cliente = new Cliente();
        Funcionario funcionario = new Funcionario();

        Venda v1 = new Venda(1, cliente, funcionario, LocalDateTime.now(), new BigDecimal("50.00"), "Cartão de Crédito");
        Venda v2 = new Venda(2, cliente, funcionario, LocalDateTime.now(), new BigDecimal("30.00"), "Pix");

        when(vendaRepository.findAll()).thenReturn(List.of(v1, v2));

        mockMvc.perform(get("/vendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].metodoPagamento").value("Cartão de Crédito"))
                .andExpect(jsonPath("$[1].metodoPagamento").value("Pix"));
    }

    @Test
    @DisplayName("Deve buscar venda por ID com sucesso")
    void deveBuscarVendaPorId() throws Exception {
        Venda venda = new Venda(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("45.00"), "Pix");

        when(vendaRepository.findById(1)).thenReturn(Optional.of(venda));

        mockMvc.perform(get("/vendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metodoPagamento").value("Pix"))
                .andExpect(jsonPath("$.valorTotal").value(45.00));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao buscar ID de venda inexistente")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(vendaRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/vendas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar uma venda com sucesso")
    void deveSalvarUmaVenda() throws Exception {
        Venda venda = new Venda(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("60.00"), "Dinheiro");

        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);

        mockMvc.perform(post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venda)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metodoPagamento").value("Dinheiro"));
    }

    @Test
    @DisplayName("Deve atualizar uma venda com sucesso")
    void deveAtualizarUmaVenda() throws Exception {
        Venda existente = new Venda(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("60.00"), "Dinheiro");
        Venda atualizada = new Venda(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("60.00"), "Pix"); // mudou o método

        when(vendaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(vendaRepository.save(any(Venda.class))).thenReturn(atualizada);

        mockMvc.perform(put("/vendas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metodoPagamento").value("Pix"));
    }

    @Test
    @DisplayName("Deve deletar uma venda por ID")
    void deveDeletarUmaVenda() throws Exception {
        Venda venda = new Venda(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("40.00"), "Pix");

        when(vendaRepository.findById(1)).thenReturn(Optional.of(venda));
        doNothing().when(vendaRepository).delete(venda);

        mockMvc.perform(delete("/vendas/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao tentar deletar ID de venda inexistente")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(vendaRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/vendas/99"))
                .andExpect(status().isNotFound());
    }
}