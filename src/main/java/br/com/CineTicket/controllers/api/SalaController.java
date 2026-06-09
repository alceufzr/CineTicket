package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Sala;
import br.com.CineTicket.repositories.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salas")
public class SalaController {

    @Autowired
    private SalaRepository salaRepository;

    @GetMapping
    public List<Sala> listarTodos() {
        return salaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Sala buscarPorId(@PathVariable Integer id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada com o ID: " + id));
    }

    @PostMapping
    public Sala salvar(@RequestBody Sala sala) {
        return salaRepository.save(sala);
    }

    @PutMapping("/{id}")
    public Sala atualizar(@PathVariable Integer id, @RequestBody Sala salaAtualizada) {
        return salaRepository.findById(id)
                .map(salaExistente -> {
                    salaExistente.setAtivo(salaAtualizada.getAtivo());
                    return salaRepository.save(salaExistente);
                }).orElseThrow(() -> new RuntimeException("Sala não encontrada com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada com o ID: " + id));

        salaRepository.delete(sala);
    }
}