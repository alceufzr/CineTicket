package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Ingresso;
import br.com.CineTicket.models.ItemCompra;
import br.com.CineTicket.models.Sessao;
import br.com.CineTicket.repositories.IngressoRepository;
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

@WebMvcTest(IngressoController.class)
public class IngressoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngressoRepository ingressoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todos os ingressos com sucesso")
    void deveListarTodasAsCompras() throws Exception {
        Ingresso i1 = new Ingresso(1, new Sessao(), new ItemCompra(), "INTEIRA");
        Ingresso i2 = new Ingresso(2, new Sessao(), new ItemCompra(), "MEIA");

        when(ingressoRepository.findAll()).thenReturn(List.of(i1, i2));

        mockMvc.perform(get("/ingressos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idIngresso").value(1))
                .andExpect(jsonPath("$[0].tipoIngresso").value("INTEIRA"))
                .andExpect(jsonPath("$[1].idIngresso").value(2))
                .andExpect(jsonPath("$[1].tipoIngresso").value("MEIA"));
    }

    @Test
    @DisplayName("Deve buscar ingresso por ID com sucesso")
    void deveBuscarCompraPorId() throws Exception {
        Ingresso ingresso = new Ingresso(1, new Sessao(), new ItemCompra(), "VIP");

        when(ingressoRepository.findById(1)).thenReturn(Optional.of(ingresso));

        mockMvc.perform(get("/ingressos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idIngresso").value(1))
                .andExpect(jsonPath("$.tipoIngresso").value("VIP"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do ingresso não existir na busca")
    void deveRetornar404CleanQuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(ingressoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ingressos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar um ingresso com sucesso")
    void deveSalvarUmaCompra() throws Exception {
        Ingresso ingresso = new Ingresso(1, new Sessao(), new ItemCompra(), "INTEIRA");

        when(ingressoRepository.save(any(Ingresso.class))).thenReturn(ingresso);

        mockMvc.perform(post("/ingressos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingresso)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idIngresso").value(1))
                .andExpect(jsonPath("$.tipoIngresso").value("INTEIRA"));
    }

    @Test
    @DisplayName("Deve atualizar dados do ingresso com sucesso")
    void deveAtualizarUmaCompra() throws Exception {
        Ingresso existente = new Ingresso(1, new Sessao(), new ItemCompra(), "INTEIRA");
        Ingresso atualizado = new Ingresso(1, new Sessao(), new ItemCompra(), "MEIA");

        when(ingressoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(ingressoRepository.save(any(Ingresso.class))).thenReturn(atualizado);

        mockMvc.perform(put("/ingressos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoIngresso").value("MEIA"));
    }

    @Test
    @DisplayName("Deve deletar ingresso por ID com sucesso")
    void deveDeletarUmaCompra() throws Exception {
        Ingresso ingresso = new Ingresso(1, new Sessao(), new ItemCompra(), "CORTESIA");

        when(ingressoRepository.findById(1)).thenReturn(Optional.of(ingresso));
        doNothing().when(ingressoRepository).delete(ingresso);

        mockMvc.perform(delete("/ingressos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do ingresso não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(ingressoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/ingressos/99"))
                .andExpect(status().isNotFound());
    }
}