package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Avaliacao;
import br.com.CineTicket.repositories.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @GetMapping
    public List<Avaliacao> listarTodas() {
        return avaliacaoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Avaliacao buscarPorId(@PathVariable Integer id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com o ID: " + id));
    }

    @PostMapping
    public Avaliacao salvar(@RequestBody Avaliacao avaliacao) {
        return avaliacaoRepository.save(avaliacao);
    }

    @PutMapping("/{id}")
    public Avaliacao atualizar(@PathVariable Integer id, @RequestBody Avaliacao avaliacaoAtualizada) {
        return avaliacaoRepository.findById(id)
                .map(avaliacaoExistente -> {
                    avaliacaoExistente.setCliente(avaliacaoAtualizada.getCliente());
                    avaliacaoExistente.setFilme(avaliacaoAtualizada.getFilme());
                    avaliacaoExistente.setNotaAvaliacao(avaliacaoAtualizada.getNotaAvaliacao());
                    avaliacaoExistente.setComentario(avaliacaoAtualizada.getComentario());
                    avaliacaoExistente.setDataCompra(avaliacaoAtualizada.getDataCompra());
                    return avaliacaoRepository.save(avaliacaoExistente);
                }).orElseThrow(() -> new RuntimeException("Avaliação não encontrada com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com o ID: " + id));

        avaliacaoRepository.delete(avaliacao);
    }
}