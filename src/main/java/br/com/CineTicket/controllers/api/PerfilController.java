package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Perfil;
import br.com.CineTicket.repositories.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfis") // Plural de Perfil
public class PerfilController {

    @Autowired
    private PerfilRepository perfilRepository;

    @GetMapping
    public List<Perfil> listarTodos() {
        return perfilRepository.findAll();
    }

    @GetMapping("/{id}")
    public Perfil buscarPorId(@PathVariable Integer id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado com o ID: " + id));
    }

    @PostMapping
    public Perfil salvar(@RequestBody Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    @PutMapping("/{id}")
    public Perfil atualizar(@PathVariable Integer id, @RequestBody Perfil perfilAtualizado) {
        return perfilRepository.findById(id)
                .map(perfilExistente -> {
                    perfilExistente.setCargo(perfilAtualizado.getCargo());
                    perfilExistente.setPermBancoDados(perfilAtualizado.getPermBancoDados());
                    return perfilRepository.save(perfilExistente);
                }).orElseThrow(() -> new RuntimeException("Perfil não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado com o ID: " + id));

        perfilRepository.delete(perfil);
    }
}