package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Categoria;
import br.com.CineTicket.models.Filme;
import br.com.CineTicket.models.Genero;
import br.com.CineTicket.repositories.FilmeRepository;
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

@WebMvcTest(FilmeController.class)
public class FilmeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmeRepository filmeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todos os filmes com sucesso")
    void deveListarTodosOsFilmes() throws Exception {
        Filme f1 = new Filme(1, new Genero(), new Categoria(), "Interestelar", 169, "2D", true);
        Filme f2 = new Filme(2, new Genero(), new Categoria(), "Avatar: O Caminho da Água", 192, "3D", true);

        when(filmeRepository.findAll()).thenReturn(List.of(f1, f2));

        mockMvc.perform(get("/filmes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idFilme").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Interestelar"))
                .andExpect(jsonPath("$[0].exibicao").value("2D"))
                .andExpect(jsonPath("$[1].idFilme").value(2))
                .andExpect(jsonPath("$[1].titulo").value("Avatar: O Caminho da Água"))
                .andExpect(jsonPath("$[1].exibicao").value("3D"));
    }

    @Test
    @DisplayName("Deve buscar filme por ID com sucesso")
    void deveBuscarFilmePorId() throws Exception {
        Filme filme = new Filme(1, new Genero(), new Categoria(), "Batman: O Cavaleiro das Trevas", 152, "2D", true);

        when(filmeRepository.findById(1)).thenReturn(Optional.of(filme));

        mockMvc.perform(get("/filmes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFilme").value(1))
                .andExpect(jsonPath("$.titulo").value("Batman: O Cavaleiro das Trevas"))
                .andExpect(jsonPath("$.duracao").value(152));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do filme não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(filmeRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/filmes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar um filme com sucesso")
    void deveSalvarUmFilme() throws Exception {
        Filme filme = new Filme(1, new Genero(), new Categoria(), "Inception", 148, "2D", true);

        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);

        mockMvc.perform(post("/filmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filme)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFilme").value(1))
                .andExpect(jsonPath("$.titulo").value("Inception"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve atualizar dados do filme com sucesso")
    void deveAtualizarUmFilme() throws Exception {
        Filme existente = new Filme(1, new Genero(), new Categoria(), "Matrix", 136, "2D", true);
        Filme atualizado = new Filme(1, new Genero(), new Categoria(), "Matrix Resurrections", 148, "3D", true);

        when(filmeRepository.findById(1)).thenReturn(Optional.of(existente));
        when(filmeRepository.save(any(Filme.class))).thenReturn(atualizado);

        mockMvc.perform(put("/filmes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Matrix Resurrections"))
                .andExpect(jsonPath("$.exibicao").value("3D"));
    }

    @Test
    @DisplayName("Deve deletar filme por ID com sucesso")
    void deveDeletarUmFilme() throws Exception {
        Filme filme = new Filme(1, new Genero(), new Categoria(), "Filme Antigo", 90, "2D", false);

        when(filmeRepository.findById(1)).thenReturn(Optional.of(filme));
        doNothing().when(filmeRepository).delete(filme);

        mockMvc.perform(delete("/filmes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do filme não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(filmeRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/filmes/99"))
                .andExpect(status().isNotFound());
    }
}