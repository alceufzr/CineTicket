package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Genero;
import br.com.CineTicket.repositories.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generos")
public class GeneroController {

    @Autowired
    private GeneroRepository generoRepository;

    @GetMapping
    public List<Genero> listarTodos() {
        return generoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Genero buscarPorId(@PathVariable Integer id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gênero não encontrado com o ID: " + id));
    }

    @PostMapping
    public Genero salvar(@RequestBody Genero genero) {
        return generoRepository.save(genero);
    }

    @PutMapping("/{id}")
    public Genero atualizar(@PathVariable Integer id, @RequestBody Genero generoAtualizado) {
        return generoRepository.findById(id)
                .map(generoExistente -> {
                    generoExistente.setGenero(generoAtualizado.getGenero());
                    generoExistente.setDescricao(generoAtualizado.getDescricao());
                    return generoRepository.save(generoExistente);
                }).orElseThrow(() -> new RuntimeException("Gênero não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gênero não encontrado com o ID: " + id));

        generoRepository.delete(genero);
    }
}