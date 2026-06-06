package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Categoria;
import br.com.CineTicket.models.Filme;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmeController.class)
public class FilmeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmeRepository filmeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todos os filmes com sucesso e retornar 200 OK")
    void deveListarTodosOsFilmes() throws Exception {
        // Criando a categoria necessária para o relacionamento
        Categoria acao = new Categoria(1, "Ação");

        // Instanciando os filmes usando o construtor do Lombok na ordem correta
        Filme f1 = new Filme(1, acao, "Batman: O Cavaleiro das Trevas", 152, "12 anos");
        Filme f2 = new Filme(2, acao, "Mad Max: Estrada da Fúria", 120, "16 anos");

        List<Filme> filmes = List.of(f1, f2);

        when(filmeRepository.findAll()).thenReturn(filmes);

        mockMvc.perform(get("/filmes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Batman: O Cavaleiro das Trevas"))
                .andExpect(jsonPath("$[0].categoria.descricao").value("Ação"))
                .andExpect(jsonPath("$[1].titulo").value("Mad Max: Estrada da Fúria"));
    }

    @Test
    @DisplayName("Deve buscar filme por ID com sucesso")
    void deveBuscarFilmePorId() throws Exception {
        Categoria acao = new Categoria(1, "Ação");
        Filme filme = new Filme(1, acao, "Batman: O Cavaleiro das Trevas", 152, "12 anos");

        when(filmeRepository.findById(1)).thenReturn(Optional.of(filme));

        mockMvc.perform(get("/filmes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Batman: O Cavaleiro das Trevas"))
                .andExpect(jsonPath("$.categoria.descricao").value("Ação"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do filme não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(filmeRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/filmes/99"))
                .andExpect(status().isNotFound()); // Capturado pelo GlobalExceptionHandler!
    }

    @Test
    @DisplayName("Deve salvar um filme com sucesso e retornar 200 OK")
    void deveSalvarUmFilme() throws Exception {
        Categoria terror = new Categoria(2, "Terror");
        Filme filme = new Filme(1, terror, "Invocação do Mal", 112, "14 anos");

        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);

        mockMvc.perform(post("/filmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filme)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Invocação do Mal"))
                .andExpect(jsonPath("$.categoria.descricao").value("Terror"));
    }

    @Test
    @DisplayName("Deve atualizar dados do filme com sucesso")
    void deveAtualizarUmFilme() throws Exception {
        Categoria acao = new Categoria(1, "Ação");
        Filme existente = new Filme(1, acao, "Batman", 152, "12 anos");
        Filme atualizado = new Filme(1, acao, "Batman: O Cavaleiro das Trevas (Atualizado)", 155, "12 anos");

        when(filmeRepository.findById(1)).thenReturn(Optional.of(existente));
        when(filmeRepository.save(any(Filme.class))).thenReturn(atualizado);

        mockMvc.perform(put("/filmes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Batman: O Cavaleiro das Trevas (Atualizado)"));
    }

    @Test
    @DisplayName("Deve deletar filme por ID com sucesso")
    void deveDeletarUmFilme() throws Exception {
        Categoria acao = new Categoria(1, "Ação");
        Filme filme = new Filme(1, acao, "Batman", 152, "12 anos");

        when(filmeRepository.findById(1)).thenReturn(Optional.of(filme));
        doNothing().when(filmeRepository).delete(filme);

        mockMvc.perform(delete("/filmes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do filme não existir na deleção")
    void deveRetornar404PermanenteQuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(filmeRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/filmes/99"))
                .andExpect(status().isNotFound());
    }
}