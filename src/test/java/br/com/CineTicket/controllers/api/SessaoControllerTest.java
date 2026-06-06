package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Filme;
import br.com.CineTicket.models.Funcionario;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void deveListarTodasAsSessoes() throws Exception {
        Filme filme = new Filme(); // Mocks vazios para o relacionamento
        Funcionario funcionario = new Funcionario();

        Sessao s1 = new Sessao(1, filme, funcionario, LocalDateTime.now(), 3, new BigDecimal("25.00"));
        Sessao s2 = new Sessao(2, filme, funcionario, LocalDateTime.now(), 5, new BigDecimal("30.00"));

        when(sessaoRepository.findAll()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/sessoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].sala").value(3))
                .andExpect(jsonPath("$[1].sala").value(5));
    }

    @Test
    @DisplayName("Deve buscar sessão por ID com sucesso")
    void deveBuscarSessaoPorId() throws Exception {
        Sessao sessao = new Sessao(1, new Filme(), new Funcionario(), LocalDateTime.now(), 2, new BigDecimal("22.50"));

        when(sessaoRepository.findById(1)).thenReturn(Optional.of(sessao));

        mockMvc.perform(get("/sessoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sala").value(2))
                .andExpect(jsonPath("$.valorIngresso").value(22.50));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao buscar ID de sessão inexistente")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(sessaoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/sessoes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar uma sessão com sucesso")
    void deveSalvarUmaSessao() throws Exception {
        Sessao sessao = new Sessao(1, new Filme(), new Funcionario(), LocalDateTime.now(), 1, new BigDecimal("20.00"));

        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        mockMvc.perform(post("/sessoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sala").value(1));
    }

    @Test
    @DisplayName("Deve atualizar uma sessão com sucesso")
    void deveAtualizarUmaSessao() throws Exception {
        Sessao existente = new Sessao(1, new Filme(), new Funcionario(), LocalDateTime.now(), 1, new BigDecimal("20.00"));
        Sessao atualizada = new Sessao(1, new Filme(), new Funcionario(), LocalDateTime.now(), 1, new BigDecimal("25.00")); // valor alterado

        when(sessaoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(atualizada);

        mockMvc.perform(put("/sessoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorIngresso").value(25.00));
    }

    @Test
    @DisplayName("Deve deletar uma sessão por ID")
    void deveDeletarUmaSessao() throws Exception {
        Sessao sessao = new Sessao(1, new Filme(), new Funcionario(), LocalDateTime.now(), 1, new BigDecimal("20.00"));

        when(sessaoRepository.findById(1)).thenReturn(Optional.of(sessao));
        doNothing().when(sessaoRepository).delete(sessao);

        mockMvc.perform(delete("/sessoes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao tentar deletar ID inexistente")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(sessaoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/sessoes/99"))
                .andExpect(status().isNotFound());
    }
}