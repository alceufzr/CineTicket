package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Sala;
import br.com.CineTicket.repositories.SalaRepository;
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

@WebMvcTest(SalaController.class)
public class SalaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalaRepository salaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todas as salas com sucesso")
    void deveListarTodasAsCompras() throws Exception {
        Sala s1 = new Sala(1, true);
        Sala s2 = new Sala(2, false);

        when(salaRepository.findAll()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/salas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idSala").value(1))
                .andExpect(jsonPath("$[0].ativo").value(true))
                .andExpect(jsonPath("$[1].idSala").value(2))
                .andExpect(jsonPath("$[1].ativo").value(false));
    }

    @Test
    @DisplayName("Deve buscar sala por ID com sucesso")
    void deveBuscarCompraPorId() throws Exception {
        Sala sala = new Sala(1, true);

        when(salaRepository.findById(1)).thenReturn(Optional.of(sala));

        mockMvc.perform(get("/salas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSala").value(1))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID da sala não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(salaRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/salas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar uma sala com sucesso")
    void deveSalvarUmaCompra() throws Exception {
        Sala sala = new Sala(1, true);

        when(salaRepository.save(any(Sala.class))).thenReturn(sala);

        mockMvc.perform(post("/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sala)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSala").value(1))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve atualizar dados da sala com sucesso")
    void deveAtualizarUmaCompra() throws Exception {
        Sala existente = new Sala(1, true);
        Sala atualizada = new Sala(1, false);

        when(salaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(salaRepository.save(any(Sala.class))).thenReturn(atualizada);

        mockMvc.perform(put("/salas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSala").value(1))
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    @DisplayName("Deve deletar sala por ID com sucesso")
    void deveDeletarUmaCompra() throws Exception {
        Sala sala = new Sala(1, true);

        when(salaRepository.findById(1)).thenReturn(Optional.of(sala));
        doNothing().when(salaRepository).delete(sala);

        mockMvc.perform(delete("/salas/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID da sala não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(salaRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/salas/99"))
                .andExpect(status().isNotFound());
    }
}