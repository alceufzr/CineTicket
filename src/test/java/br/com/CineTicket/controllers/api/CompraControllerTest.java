package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Cliente;
import br.com.CineTicket.models.Compra;
import br.com.CineTicket.models.Funcionario;
import br.com.CineTicket.repositories.CompraRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompraController.class)
public class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraRepository compraRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todas as compras com sucesso")
    void deveListarTodasAsCompras() throws Exception {
        Compra c1 = new Compra(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("45.00"));
        Compra c2 = new Compra(2, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("90.00"));

        when(compraRepository.findAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/compras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idCompra").value(1))
                .andExpect(jsonPath("$[0].valorTotal").value(45.00))
                .andExpect(jsonPath("$[1].idCompra").value(2))
                .andExpect(jsonPath("$[1].valorTotal").value(90.00));
    }

    @Test
    @DisplayName("Deve buscar compra por ID com sucesso")
    void deveBuscarCompraPorId() throws Exception {
        Compra compra = new Compra(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("35.50"));

        when(compraRepository.findById(1)).thenReturn(Optional.of(compra));

        mockMvc.perform(get("/compras/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCompra").value(1))
                .andExpect(jsonPath("$.valorTotal").value(35.50));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID da compra não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(compraRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/compras/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar uma compra com sucesso")
    void deveSalvarUmaCompra() throws Exception {
        Compra compra = new Compra(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("60.00"));

        when(compraRepository.save(any(Compra.class))).thenReturn(compra);

        mockMvc.perform(post("/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(compra)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCompra").value(1))
                .andExpect(jsonPath("$.valorTotal").value(60.00));
    }

    @Test
    @DisplayName("Deve atualizar dados da compra com sucesso")
    void deveAtualizarUmaCompra() throws Exception {
        Compra existente = new Compra(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("50.00"));
        Compra atualizada = new Compra(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("75.00"));

        when(compraRepository.findById(1)).thenReturn(Optional.of(existente));
        when(compraRepository.save(any(Compra.class))).thenReturn(atualizada);

        mockMvc.perform(put("/compras/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotal").value(75.00));
    }

    @Test
    @DisplayName("Deve deletar compra por ID com sucesso")
    void deveDeletarUmaCompra() throws Exception {
        Compra compra = new Compra(1, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("40.00"));

        when(compraRepository.findById(1)).thenReturn(Optional.of(compra));
        doNothing().when(compraRepository).delete(compra);

        mockMvc.perform(delete("/compras/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID da compra não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(compraRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/compras/99"))
                .andExpect(status().isNotFound());
    }
}