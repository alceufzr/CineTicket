package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Categoria;
import br.com.CineTicket.repositories.CategoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarTodasAsCategorias() throws Exception {
        List<Categoria> categorias = List.of(
                new Categoria(1, "Ação"),
                new Categoria(2, "Comédia")
        );

        when(categoriaRepository.findAll()).thenReturn(categorias);

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].descricao").value("Ação"))
                .andExpect(jsonPath("$[1].descricao").value("Comédia"));
    }

    @Test
    void deveBuscarCategoriaPorId() throws Exception {
        Categoria categoria = new Categoria(1, "Ação");

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Ação"));
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/categorias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveSalvarUmaCategoria() throws Exception {
        Categoria categoria = new Categoria(1, "Terror");

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Terror"));
    }

    @Test
    void deveAtualizarUmaCategoria() throws Exception {
        Categoria existente = new Categoria(1, "Ação");
        Categoria atualizada = new Categoria(1, "Ação Atualizada");

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(atualizada);

        mockMvc.perform(put("/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Ação Atualizada"));
    }

    @Test
    void deveDeletarUmaCategoria() throws Exception {
        Categoria categoria = new Categoria(1, "Ação");

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        doNothing().when(categoriaRepository).delete(categoria);

        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/categorias/99"))
                .andExpect(status().isNotFound());
    }
}