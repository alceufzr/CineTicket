package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Filme;
import br.com.CineTicket.repositories.FilmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    @Autowired
    private FilmeRepository filmeRepository;

    @GetMapping
    public List<Filme> listarTodos() {
        return filmeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Filme buscarPorId(@PathVariable Integer id) {
        return filmeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado com o ID: " + id));
    }

    @PostMapping
    public Filme salvar(@RequestBody Filme filme) {
        return filmeRepository.save(filme);
    }

    @PutMapping("/{id}")
    public Filme atualizar(@PathVariable Integer id, @RequestBody Filme filmeAtualizado) {
        return filmeRepository.findById(id)
                .map(filmeExistente -> {
                    filmeExistente.setTitulo(filmeAtualizado.getTitulo());
                    filmeExistente.setDuracao(filmeAtualizado.getDuracao());
                    filmeExistente.setClassificacao(filmeAtualizado.getClassificacao());
                    filmeExistente.setCategoria(filmeAtualizado.getCategoria());
                    return filmeRepository.save(filmeExistente);
                }).orElseThrow(() -> new RuntimeException("Filme não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado com o ID: " + id));

        filmeRepository.delete(filme);
    }
}