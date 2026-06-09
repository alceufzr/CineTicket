package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Avaliacao;
import br.com.CineTicket.models.Cliente;
import br.com.CineTicket.models.Filme;
import br.com.CineTicket.repositories.AvaliacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(AvaliacaoController.class)
public class AvaliacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarTodasAsAvaliacoes() throws Exception {
        List<Avaliacao> avaliacoes = List.of(
                new Avaliacao(1, new Cliente(), new Filme(), new BigDecimal("4.5"), "Filme excelente!", LocalDateTime.now()),
                new Avaliacao(2, new Cliente(), new Filme(), new BigDecimal("3.0"), "Mediano.", LocalDateTime.now())
        );

        when(avaliacaoRepository.findAll()).thenReturn(avaliacoes);

        mockMvc.perform(get("/avaliacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].comentario").value("Filme excelente!"))
                .andExpect(jsonPath("$[1].comentario").value("Mediano."));
    }

    @Test
    void deveBuscarAvaliacaoPorId() throws Exception {
        Avaliacao avaliacao = new Avaliacao(1, new Cliente(), new Filme(), new BigDecimal("5.0"), "Obra prima!", LocalDateTime.now());

        when(avaliacaoRepository.findById(1)).thenReturn(Optional.of(avaliacao));

        mockMvc.perform(get("/avaliacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Obra prima!"));
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(avaliacaoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/avaliacoes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveSalvarUmaAvaliacao() throws Exception {
        Avaliacao avaliacao = new Avaliacao(1, new Cliente(), new Filme(), new BigDecimal("4.0"), "Muito bom", LocalDateTime.now());

        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avaliacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Muito bom"));
    }

    @Test
    void deveAtualizarUmaAvaliacao() throws Exception {
        Avaliacao existente = new Avaliacao(1, new Cliente(), new Filme(), new BigDecimal("4.0"), "Muito bom", LocalDateTime.now());
        Avaliacao atualizada = new Avaliacao(1, new Cliente(), new Filme(), new BigDecimal("4.5"), "Muito bom, revendo ficou melhor", LocalDateTime.now());

        when(avaliacaoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(atualizada);

        mockMvc.perform(put("/avaliacoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Muito bom, revendo ficou melhor"));
    }

    @Test
    void deveDeletarUmaAvaliacao() throws Exception {
        Avaliacao avaliacao = new Avaliacao(1, new Cliente(), new Filme(), new BigDecimal("2.0"), "Ruim", LocalDateTime.now());

        when(avaliacaoRepository.findById(1)).thenReturn(Optional.of(avaliacao));
        doNothing().when(avaliacaoRepository).delete(avaliacao);

        mockMvc.perform(delete("/avaliacoes/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(avaliacaoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/avaliacoes/99"))
                .andExpect(status().isNotFound());
    }
}