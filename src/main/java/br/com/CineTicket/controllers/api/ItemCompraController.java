package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.ItemCompra;
import br.com.CineTicket.repositories.ItemCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens-compra")
public class ItemCompraController {

    @Autowired
    private ItemCompraRepository itemCompraRepository;

    @GetMapping
    public List<ItemCompra> listarTodos() {
        return itemCompraRepository.findAll();
    }

    @GetMapping("/{id}")
    public ItemCompra buscarPorId(@PathVariable Integer id) {
        return itemCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de compra não encontrado com o ID: " + id));
    }

    @PostMapping
    public ItemCompra salvar(@RequestBody ItemCompra itemCompra) {
        return itemCompraRepository.save(itemCompra);
    }

    @PutMapping("/{id}")
    public ItemCompra atualizar(@PathVariable Integer id, @RequestBody ItemCompra itemCompraAtualizado) {
        return itemCompraRepository.findById(id)
                .map(itemCompraExistente -> {
                    itemCompraExistente.setCompra(itemCompraAtualizado.getCompra());
                    itemCompraExistente.setProduto(itemCompraAtualizado.getProduto());
                    itemCompraExistente.setDescricao(itemCompraAtualizado.getDescricao());
                    itemCompraExistente.setQuantidade(itemCompraAtualizado.getQuantidade());
                    itemCompraExistente.setValor(itemCompraAtualizado.getValor());
                    itemCompraExistente.setImposto(itemCompraAtualizado.getImposto());
                    return itemCompraRepository.save(itemCompraExistente);
                }).orElseThrow(() -> new RuntimeException("Item de compra não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        ItemCompra itemCompra = itemCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de compra não encontrado com o ID: " + id));

        itemCompraRepository.delete(itemCompra);
    }
}