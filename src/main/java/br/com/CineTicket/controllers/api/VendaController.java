package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Venda;
import br.com.CineTicket.repositories.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping
    public List<Venda> listarTodos() {
        return vendaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Venda buscarPorId(@PathVariable Integer id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada com o ID: " + id));
    }

    @PostMapping
    public Venda salvar(@RequestBody Venda venda) {
        return vendaRepository.save(venda);
    }

    @PutMapping("/{id}")
    public Venda atualizar(@PathVariable Integer id, @RequestBody Venda vendaAtualizada) {
        return vendaRepository.findById(id)
                .map(vendaExistente -> {
                    vendaExistente.setDataVenda(vendaAtualizada.getDataVenda());
                    vendaExistente.setValorTotal(vendaAtualizada.getValorTotal());
                    vendaExistente.setMetodoPagamento(vendaAtualizada.getMetodoPagamento());
                    vendaExistente.setCliente(vendaAtualizada.getCliente());
                    vendaExistente.setFuncionario(vendaAtualizada.getFuncionario());
                    return vendaRepository.save(vendaExistente);
                }).orElseThrow(() -> new RuntimeException("Venda não encontrada com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada com o ID: " + id));

        vendaRepository.delete(venda);
    }
}