package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Filme;
import br.com.CineTicket.models.Funcionario;
import br.com.CineTicket.models.Cliente;
import br.com.CineTicket.models.Sessao;
import br.com.CineTicket.models.Venda;
import br.com.CineTicket.models.Ingresso;
import br.com.CineTicket.repositories.IngressoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngressoController.class)
public class IngressoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngressoRepository ingressoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Métodos auxiliares para reaproveitar a criação das dependências nos testes
    private Sessao criarSessaoMock() {
        return new Sessao(10, new Filme(), new Funcionario(), LocalDateTime.now(), 3, new BigDecimal("25.00"));
    }

    private Venda criarVendaMock() {
        return new Venda(20, new Cliente(), new Funcionario(), LocalDateTime.now(), new BigDecimal("50.00"), "Pix");
    }

    @Test
    @DisplayName("Deve listar todos os ingressos com o contexto de sessão e venda preenchidos")
    void deveListarTodosOsIngressos() throws Exception {
        Sessao sessao = criarSessaoMock();
        Venda venda = criarVendaMock();

        Ingresso i1 = new Ingresso(1, sessao, venda, "A1");
        Ingresso i2 = new Ingresso(2, sessao, venda, "A2");

        when(ingressoRepository.findAll()).thenReturn(List.of(i1, i2));

        mockMvc.perform(get("/ingressos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].assento").value("A1"))
                .andExpect(jsonPath("$[0].sessao.sala").value(3)) // Validando o contexto de Sessão
                .andExpect(jsonPath("$[0].venda.metodoPagamento").value("Pix")) // Validando o contexto de Venda
                .andExpect(jsonPath("$[1].assento").value("A2"));
    }

    @Test
    @DisplayName("Deve buscar ingresso por ID com sucesso trazendo os relacionamentos")
    void deveBuscarIngressoPorId() throws Exception {
        Sessao sessao = criarSessaoMock();
        Venda venda = criarVendaMock();
        Ingresso ingresso = new Ingresso(1, sessao, venda, "B5");

        when(ingressoRepository.findById(1)).thenReturn(Optional.of(ingresso));

        mockMvc.perform(get("/ingressos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assento").value("B5"))
                .andExpect(jsonPath("$.sessao.idSessao").value(10))
                .andExpect(jsonPath("$.venda.idVenda").value(20));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do ingresso não existir na busca")
    void deveRetornar404QuandoIdNaoEncontradoNoBuscar() throws Exception {
        when(ingressoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ingressos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve salvar um ingresso com sucesso e retornar os dados populados")
    void deveSalvarUmIngresso() throws Exception {
        Sessao sessao = criarSessaoMock();
        Venda venda = criarVendaMock();
        Ingresso ingresso = new Ingresso(1, sessao, venda, "C3");

        when(ingressoRepository.save(any(Ingresso.class))).thenReturn(ingresso);

        mockMvc.perform(post("/ingressos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingresso)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assento").value("C3"))
                .andExpect(jsonPath("$.sessao.sala").value(3))
                .andExpect(jsonPath("$.venda.metodoPagamento").value("Pix"));
    }

    @Test
    @DisplayName("Deve atualizar dados do ingresso com sucesso")
    void deveAtualizarUmIngresso() throws Exception {
        Sessao sessao = criarSessaoMock();
        Venda venda = criarVendaMock();

        Ingresso existente = new Ingresso(1, sessao, venda, "H1");
        Ingresso atualizado = new Ingresso(1, sessao, venda, "H2");

        when(ingressoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(ingressoRepository.save(any(Ingresso.class))).thenReturn(atualizado);

        mockMvc.perform(put("/ingressos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assento").value("H2"));
    }

    @Test
    @DisplayName("Deve deletar ingresso por ID com sucesso")
    void deveDeletarUmIngresso() throws Exception {
        Sessao sessao = criarSessaoMock();
        Venda venda = criarVendaMock();
        Ingresso ingresso = new Ingresso(1, sessao, venda, "D1");

        when(ingressoRepository.findById(1)).thenReturn(Optional.of(ingresso));
        doNothing().when(ingressoRepository).delete(ingresso);

        mockMvc.perform(delete("/ingressos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do ingresso não existir na deleção")
    void deveRetornar404QuandoIdNaoEncontradoNoDeletar() throws Exception {
        when(ingressoRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/ingressos/99"))
                .andExpect(status().isNotFound());
    }
}