package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Filme;
import br.com.CineTicket.models.Sala;
import br.com.CineTicket.models.Sessao;
import br.com.CineTicket.repositories.SessaoRepository;
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

@WebMvcTest(SessaoController.class)
public class SessaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessaoRepository sessaoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todas as sessões com sucesso")
    void deveListarTodasAsCompras() throws Exception {
        Sessao s1 = new Sessao(1, new Filme(), new Sala(), LocalDateTime.now(), new BigDecimal("25.00"), 120);
        Sessao s2 = new Sessao(2, new Filme(), new Sala(), LocalDateTime.now(), new BigDecimal("35.00"), 150);

        when(sessaoRepository.findAll()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/sessoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idSessao").value(1))
                .andExpect(jsonPath("$[0].valorIngresso").value(25.00))
                .andExpect(jsonPath("$[0].capacidade").value(120))
                .andExpect(jsonPath("$[1].idSessao").value(2))
                .andExpect(jsonPath("$[1].valorIngresso").value(35.00))
                .andExpect(jsonPath("$[1].capacidade").value(150));
    }

    @Test
    @DisplayName("Deve buscar sessão por ID com sucesso")
    void deveBuscarCompraPorId() throws Exception {
        Sessao sessao = new Sessao(1, new Filme(), new Sala(), LocalDateTime.now(), new BigDecimal("30.00"), 100);

        when(sessaoRepository.findById(1)).thenReturn(Optional.of(sessao));

        mockMvc.perform(get("/sessoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSessao").value(1))
                .andExpect(jsonPath("$.valorIngresso").value(30.00))
                .andExpect(jsonPath("$.capacidade").value(100));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID da sessão não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(sessaoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/sessoes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar uma sessão com sucesso")
    void deveSalvarUmaCompra() throws Exception {
        Sessao sessao = new Sessao(1, new Filme(), new Sala(), LocalDateTime.now(), new BigDecimal("20.00"), 80);

        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        mockMvc.perform(post("/sessoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSessao").value(1))
                .andExpect(jsonPath("$.valorIngresso").value(20.00))
                .andExpect(jsonPath("$.capacidade").value(80));
    }

    @Test
    @DisplayName("Deve atualizar dados da sessão com sucesso")
    void deveAtualizarUmaCompra() throws Exception {
        Sessao existente = new Sessao(1, new Filme(), new Sala(), LocalDateTime.now(), new BigDecimal("20.00"), 80);
        Sessao atualizada = new Sessao(1, new Filme(), new Sala(), LocalDateTime.now(), new BigDecimal("28.00"), 85);

        when(sessaoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(atualizada);

        mockMvc.perform(put("/sessoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorIngresso").value(28.00))
                .andExpect(jsonPath("$.capacidade").value(85));
    }

    @Test
    @DisplayName("Deve deletar sessão por ID com sucesso")
    void deveDeletarUmaCompra() throws Exception {
        Sessao sessao = new Sessao(1, new Filme(), new Sala(), LocalDateTime.now(), new BigDecimal("15.00"), 60);

        when(sessaoRepository.findById(1)).thenReturn(Optional.of(sessao));
        doNothing().when(sessaoRepository).delete(sessao);

        mockMvc.perform(delete("/sessoes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID da sessão não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(sessaoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/sessoes/99"))
                .andExpect(status().isNotFound());
    }
}