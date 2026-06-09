package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Compra;
import br.com.CineTicket.repositories.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraRepository compraRepository;

    @GetMapping
    public List<Compra> listarTodos() {
        return compraRepository.findAll();
    }

    @GetMapping("/{id}")
    public Compra buscarPorId(@PathVariable Integer id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra não encontrada com o ID: " + id));
    }

    @PostMapping
    public Compra salvar(@RequestBody Compra compra) {
        return compraRepository.save(compra);
    }

    @PutMapping("/{id}")
    public Compra atualizar(@PathVariable Integer id, @RequestBody Compra compraAtualizada) {
        return compraRepository.findById(id)
                .map(compraExistente -> {
                    compraExistente.setDataCompra(compraAtualizada.getDataCompra());
                    compraExistente.setValorTotal(compraAtualizada.getValorTotal());
                    compraExistente.setCliente(compraAtualizada.getCliente());
                    compraExistente.setFuncionario(compraAtualizada.getFuncionario());
                    return compraRepository.save(compraExistente);
                }).orElseThrow(() -> new RuntimeException("Compra não encontrada com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra não encontrada com o ID: " + id));

        compraRepository.delete(compra);
    }
}