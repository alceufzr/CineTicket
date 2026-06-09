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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve listar todos os clientes com sucesso")
    void deveListarTodasAsCompras() throws Exception {
        Cliente c1 = new Cliente(1, "Carlos Silva", true, 50.00, true, "carlos.silva", "senha123");
        Cliente c2 = new Cliente(2, "Ana Souza", false, 15.00, true, "ana.souza", "senha456");

        when(clienteRepository.findAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idCliente").value(1))
                .andExpect(jsonPath("$[0].nome").value("Carlos Silva"))
                .andExpect(jsonPath("$[0].saldo").value(50.00))
                .andExpect(jsonPath("$[1].idCliente").value(2))
                .andExpect(jsonPath("$[1].nome").value("Ana Souza"))
                .andExpect(jsonPath("$[1].saldo").value(15.00));
    }

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void deveBuscarCompraPorId() throws Exception {
        Cliente cliente = new Cliente(1, "Carlos Silva", true, 50.00, true, "carlos.silva", "senha123");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.nome").value("Carlos Silva"))
                .andExpect(jsonPath("$.saldo").value(50.00));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do cliente não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar um cliente com sucesso")
    void deveSalvarUmaCompra() throws Exception {
        Cliente cliente = new Cliente(1, "Marcos Oliveira", false, 100.00, true, "marcos.o", "senha789");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.nome").value("Marcos Oliveira"))
                .andExpect(jsonPath("$.saldo").value(100.00));
    }

    @Test
    @DisplayName("Deve atualizar dados do cliente com sucesso")
    void deveAtualizarUmaCompra() throws Exception {
        Cliente existente = new Cliente(1, "Carlos Silva", true, 50.00, true, "carlos.silva", "senha123");
        Cliente atualizado = new Cliente(1, "Carlos Silva Santos", true, 75.00, true, "carlos.silva", "novaSenha123");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(atualizado);

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Silva Santos"))
                .andExpect(jsonPath("$.saldo").value(75.00));
    }

    @Test
    @DisplayName("Deve deletar cliente por ID com sucesso")
    void deveDeletarUmaCompra() throws Exception {
        Cliente cliente = new Cliente(1, "Carlos Silva", true, 50.00, true, "carlos.silva", "senha123");

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