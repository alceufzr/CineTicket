package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Ingresso;
import br.com.CineTicket.repositories.IngressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingressos")
public class IngressoController {

    @Autowired
    private IngressoRepository ingressoRepository;

    @GetMapping
    public List<Ingresso> listarTodos() {
        return ingressoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ingresso buscarPorId(@PathVariable Integer id) {
        return ingressoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingresso não encontrado com o ID: " + id));
    }

    @PostMapping
    public Ingresso salvar(@RequestBody Ingresso ingresso) {
        return ingressoRepository.save(ingresso);
    }

    @PutMapping("/{id}")
    public Ingresso atualizar(@PathVariable Integer id, @RequestBody Ingresso ingressoAtualizado) {
        return ingressoRepository.findById(id)
                .map(ingressoExistente -> {
                    ingressoExistente.setSessao(ingressoAtualizado.getSessao());
                    ingressoExistente.setItemCompra(ingressoAtualizado.getItemCompra());
                    ingressoExistente.setTipoIngresso(ingressoAtualizado.getTipoIngresso());
                    return ingressoRepository.save(ingressoExistente);
                }).orElseThrow(() -> new RuntimeException("Ingresso não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Ingresso ingresso = ingressoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingresso não encontrado com o ID: " + id));

        ingressoRepository.delete(ingresso);
    }
}