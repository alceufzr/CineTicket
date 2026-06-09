package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Funcionario;
import br.com.CineTicket.models.Perfil;
import br.com.CineTicket.repositories.FuncionarioRepository;
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

@WebMvcTest(FuncionarioController.class)
public class FuncionarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todos os funcionários com sucesso e retornar 200 OK")
    void deveListarTodosOsFuncionarios() throws Exception {
        // Usando o construtor correto do Perfil: idPerfil, cargo, permBancoDados
        Perfil gerente = new Perfil(1, "Gerente", true);

        Funcionario f1 = new Funcionario(1, gerente, "Alceu Rodrigues", 500.0, true, "alceu_func", "senha123");
        Funcionario f2 = new Funcionario(2, gerente, "João Silva", 350.0, true, "joao_func", "senha456");

        List<Funcionario> funcionarios = List.of(f1, f2);

        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        mockMvc.perform(get("/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Alceu Rodrigues"))
                .andExpect(jsonPath("$[0].perfil.cargo").value("Gerente")) // Validando o atributo correto (.cargo)
                .andExpect(jsonPath("$[1].nome").value("João Silva"));
    }

    @Test
    @DisplayName("Deve buscar funcionário por ID com sucesso")
    void deveBuscarFuncionarioPorId() throws Exception {
        Perfil caixa = new Perfil(2, "Caixa", false);
        Funcionario funcionario = new Funcionario(1, caixa, "Alceu Rodrigues", 400.0, true, "alceu_func", "senha123");

        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionario));

        mockMvc.perform(get("/funcionarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Alceu Rodrigues"))
                .andExpect(jsonPath("$.perfil.cargo").value("Caixa"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do funcionário não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(funcionarioRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/funcionarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar um funcionário com sucesso e retornar 200 OK")
    void deveSalvarUmFuncionario() throws Exception {
        Perfil gerente = new Perfil(1, "Gerente", true);
        Funcionario funcionario = new Funcionario(1, gerente, "Alceu Rodrigues", 500.0, true, "alceu_func", "senha123");

        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);

        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Alceu Rodrigues"))
                .andExpect(jsonPath("$.perfil.cargo").value("Gerente"));
    }

    @Test
    @DisplayName("Deve atualizar dados do funcionário com sucesso")
    void deveAtualizarUmFuncionario() throws Exception {
        Perfil gerente = new Perfil(1, "Gerente", true);
        Funcionario existente = new Funcionario(1, gerente, "Alceu Rodrigues", 500.0, true, "alceu_func", "senha123");
        Funcionario atualizado = new Funcionario(1, gerente, "Alceu Fuzari Rodrigues", 600.0, true, "alceu_func", "novaSenha123");

        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(atualizado);

        mockMvc.perform(put("/funcionarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Alceu Fuzari Rodrigues"))
                .andExpect(jsonPath("$.comissao").value(600.0));
    }

    @Test
    @DisplayName("Deve deletar funcionário por ID com sucesso")
    void deveDeletarUmFuncionario() throws Exception {
        Perfil gerente = new Perfil(1, "Gerente", true);
        Funcionario funcionario = new Funcionario(1, gerente, "Alceu Rodrigues", 500.0, true, "alceu_func", "senha123");

        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionario));
        doNothing().when(funcionarioRepository).delete(funcionario);

        mockMvc.perform(delete("/funcionarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do funcionário não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(funcionarioRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/funcionarios/99"))
                .andExpect(status().isNotFound());
    }
}