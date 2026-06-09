package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Genero;
import br.com.CineTicket.repositories.GeneroRepository;
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

@WebMvcTest(GeneroController.class)
public class GeneroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeneroRepository generoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todos os gêneros com sucesso")
    void deveListarTodosOsGeneros() throws Exception {
        Genero g1 = new Genero(1, "Ação", "Filmes com muita explosão e adrenalina");
        Genero g2 = new Genero(2, "Comédia", "Filmes focados em humor e piadas");

        when(generoRepository.findAll()).thenReturn(List.of(g1, g2));

        mockMvc.perform(get("/generos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idGenero").value(1))
                .andExpect(jsonPath("$[0].genero").value("Ação"))
                .andExpect(jsonPath("$[0].descricao").value("Filmes com muita explosão e adrenalina"))
                .andExpect(jsonPath("$[1].idGenero").value(2))
                .andExpect(jsonPath("$[1].genero").value("Comédia"))
                .andExpect(jsonPath("$[1].descricao").value("Filmes focados em humor e piadas"));
    }

    @Test
    @DisplayName("Deve buscar gênero por ID com sucesso")
    void deveBuscarGeneroPorId() throws Exception {
        Genero genero = new Genero(1, "Sci-Fi", "Ficção científica e tecnologia");

        when(generoRepository.findById(1)).thenReturn(Optional.of(genero));

        mockMvc.perform(get("/generos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idGenero").value(1))
                .andExpect(jsonPath("$.genero").value("Sci-Fi"))
                .andExpect(jsonPath("$.descricao").value("Ficção científica e tecnologia"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do gênero não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(generoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/generos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar um gênero com sucesso")
    void deveSalvarUmGenero() throws Exception {
        Genero genero = new Genero(1, "Terror", "Filmes de suspense e sustos");

        when(generoRepository.save(any(Genero.class))).thenReturn(genero);

        mockMvc.perform(post("/generos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genero)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idGenero").value(1))
                .andExpect(jsonPath("$.genero").value("Terror"))
                .andExpect(jsonPath("$.descricao").value("Filmes de suspense e sustos"));
    }

    @Test
    @DisplayName("Deve atualizar dados do gênero com sucesso")
    void deveAtualizarUmGenero() throws Exception {
        Genero existente = new Genero(1, "Romance", "Filmes sobre amor");
        Genero atualizada = new Genero(1, "Romance Drama", "Filmes sobre amor com muito drama");

        when(generoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(generoRepository.save(any(Genero.class))).thenReturn(atualizada);

        mockMvc.perform(put("/generos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genero").value("Romance Drama"))
                .andExpect(jsonPath("$.descricao").value("Filmes sobre amor com muito drama"));
    }

    @Test
    @DisplayName("Deve deletar gênero por ID com sucesso")
    void deveDeletarUmGenero() throws Exception {
        Genero genero = new Genero(1, "Animação", "Desenhos animados infantis ou adultos");

        when(generoRepository.findById(1)).thenReturn(Optional.of(genero));
        doNothing().when(generoRepository).delete(genero);

        mockMvc.perform(delete("/generos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do gênero não existir na deleção")
    void deveRetornar404親QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(generoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/generos/99"))
                .andExpect(status().isNotFound());
    }
}