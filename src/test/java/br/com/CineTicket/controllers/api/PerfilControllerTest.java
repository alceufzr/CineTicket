package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Perfil;
import br.com.CineTicket.repositories.PerfilRepository;
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

@WebMvcTest(PerfilController.class)
public class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PerfilRepository perfilRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todos os perfis com sucesso")
    void deveListarTodosOsPerfis() throws Exception {
        Perfil p1 = new Perfil(1, "Administrador", true);
        Perfil p2 = new Perfil(2, "Atendente", false);

        when(perfilRepository.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/perfis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idPerfil").value(1))
                .andExpect(jsonPath("$[0].cargo").value("Administrador"))
                .andExpect(jsonPath("$[0].permBancoDados").value(true))
                .andExpect(jsonPath("$[1].idPerfil").value(2))
                .andExpect(jsonPath("$[1].cargo").value("Atendente"))
                .andExpect(jsonPath("$[1].permBancoDados").value(false));
    }

    @Test
    @DisplayName("Deve buscar perfil por ID com sucesso")
    void deveBuscarPerfilPorId() throws Exception {
        Perfil perfil = new Perfil(1, "Gerente", true);

        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil));

        mockMvc.perform(get("/perfis/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPerfil").value(1))
                .andExpect(jsonPath("$.cargo").value("Gerente"))
                .andExpect(jsonPath("$.permBancoDados").value(true));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do perfil não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(perfilRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/perfis/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar um perfil com sucesso")
    void deveSalvarUmPerfil() throws Exception {
        Perfil perfil = new Perfil(1, "Bilheteiro", false);

        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        mockMvc.perform(post("/perfis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfil)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPerfil").value(1))
                .andExpect(jsonPath("$.cargo").value("Bilheteiro"))
                .andExpect(jsonPath("$.permBancoDados").value(false));
    }

    @Test
    @DisplayName("Deve atualizar dados do perfil com sucesso")
    void deveAtualizarUmPerfil() throws Exception {
        Perfil existente = new Perfil(1, "Suporte", false);
        Perfil atualizado = new Perfil(1, "Suporte Avançado", true);

        when(perfilRepository.findById(1)).thenReturn(Optional.of(existente));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(atualizado);

        mockMvc.perform(put("/perfis/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cargo").value("Suporte Avançado"))
                .andExpect(jsonPath("$.permBancoDados").value(true));
    }

    @Test
    @DisplayName("Deve deletar perfil por ID com sucesso")
    void deveDeletarUmPerfil() throws Exception {
        Perfil perfil = new Perfil(1, "Temporário", false);

        when(perfilRepository.findById(1)).thenReturn(Optional.of(perfil));
        doNothing().when(perfilRepository).delete(perfil);

        mockMvc.perform(delete("/perfis/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do perfil não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(perfilRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/perfis/99"))
                .andExpect(status().isNotFound());
    }
}