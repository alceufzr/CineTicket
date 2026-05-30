package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Sessao;
import br.com.CineTicket.repositories.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessoes")
public class SessaoController {

    @Autowired
    private SessaoRepository sessaoRepository;

    @GetMapping
    public List<Sessao> listarTodos() {
        return sessaoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Sessao buscarPorId(@PathVariable Integer id) {
        return sessaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada com o ID: " + id));
    }

    @PostMapping
    public Sessao salvar(@RequestBody Sessao sessao) {
        return sessaoRepository.save(sessao);
    }

    @PutMapping("/{id}")
    public Sessao atualizar(@PathVariable Integer id, @RequestBody Sessao sessaoAtualizada) {
        return sessaoRepository.findById(id)
                .map(sessaoExistente -> {
                    sessaoExistente.setDataHora(sessaoAtualizada.getDataHora());
                    sessaoExistente.setSala(sessaoAtualizada.getSala());
                    sessaoExistente.setValorIngresso(sessaoAtualizada.getValorIngresso());
                    sessaoExistente.setFilme(sessaoAtualizada.getFilme());
                    sessaoExistente.setFuncionario(sessaoAtualizada.getFuncionario());
                    return sessaoRepository.save(sessaoExistente);
                }).orElseThrow(() -> new RuntimeException("Sessão não encontrada com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Sessao sessao = sessaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada com o ID: " + id));

        sessaoRepository.delete(sessao);
    }
}