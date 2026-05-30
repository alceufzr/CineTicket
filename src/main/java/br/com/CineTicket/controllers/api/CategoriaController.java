package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Categoria;
import br.com.CineTicket.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));
    }

    @PostMapping
    public Categoria salvar(@RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @PutMapping("/{id}")
    public Categoria atualizar(@PathVariable Integer id, @RequestBody Categoria categoriaAtualizada) {
        return categoriaRepository.findById(id)
                .map(categoriaExistente -> {
                    categoriaExistente.setNome(categoriaAtualizada.getNome());
                    return categoriaRepository.save(categoriaExistente);
                }).orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));

        categoriaRepository.delete(categoria);
    }
}