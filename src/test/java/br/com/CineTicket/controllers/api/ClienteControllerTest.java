package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Cliente;
import br.com.CineTicket.repositories.ClienteRepository;
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

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todos os clientes com sucesso e retornar 200 OK")
    void deveListarTodosOsClientes() throws Exception {
        // Usando o construtor do Lombok (@AllArgsConstructor)
        Cliente c1 = new Cliente(1, "Alceu Rodrigues", "12345678900", "alceu@email.com", "alceu_dev", "senha123");
        Cliente c2 = new Cliente(2, "Fulano de Tal", "98765432111", "fulano@email.com", "fulano_test", "senha456");

        List<Cliente> clientes = List.of(c1, c2);

        when(clienteRepository.findAll()).thenReturn(clientes);

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Alceu Rodrigues"))
                .andExpect(jsonPath("$[1].nome").value("Fulano de Tal"));
    }

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void deveBuscarClientePorId() throws Exception {
        Cliente cliente = new Cliente(1, "Alceu Rodrigues", "12345678900", "alceu@email.com", "alceu_dev", "senha123");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Alceu Rodrigues"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do cliente não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar um cliente com sucesso e retornar 200 OK")
    void deveSalvarUmCliente() throws Exception {
        Cliente cliente = new Cliente(1, "Alceu Rodrigues", "12345678900", "alceu@email.com", "alceu_dev", "senha123");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Alceu Rodrigues"))
                .andExpect(jsonPath("$.login").value("alceu_dev"));
    }

    @Test
    @DisplayName("Deve atualizar dados do cliente com sucesso")
    void deveAtualizarUmCliente() throws Exception {
        Cliente existente = new Cliente(1, "Alceu Rodrigues", "12345678900", "alceu@email.com", "alceu_dev", "senha123");
        Cliente atualizado = new Cliente(1, "Alceu Fuzari", "12345678900", "alceu.novo@email.com", "alceu_dev", "senha123");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(atualizado);

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Alceu Fuzari"))
                .andExpect(jsonPath("$.email").value("alceu.novo@email.com"));
    }

    @Test
    @DisplayName("Deve deletar cliente por ID com sucesso")
    void deveDeletarUmCliente() throws Exception {
        Cliente cliente = new Cliente(1, "Alceu Rodrigues", "12345678900", "alceu@email.com", "alceu_dev", "senha123");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(cliente);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do cliente não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/clientes/99"))
                .andExpect(status().isNotFound());
    }
}