package br.com.CineTicket.controllers.api;

import br.com.CineTicket.models.Funcionario;
import br.com.CineTicket.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping
    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Funcionario buscarPorId(@PathVariable Integer id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario não encontrado com o ID: " + id));
    }

    @PostMapping
    public Funcionario salvar(@RequestBody Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    @PutMapping("/{id}")
    public Funcionario atualizar(@PathVariable Integer id, @RequestBody Funcionario funcionarioAtualizado) {
        return funcionarioRepository.findById(id)
                .map(funcionarioExistente -> {
                    funcionarioExistente.setNome(funcionarioAtualizado.getNome());
                    funcionarioExistente.setComissao(funcionarioAtualizado.getComissao());
                    funcionarioExistente.setAtivo(funcionarioAtualizado.getAtivo());
                    funcionarioExistente.setLogin(funcionarioAtualizado.getLogin());
                    funcionarioExistente.setSenha(funcionarioAtualizado.getSenha());
                    funcionarioExistente.setPerfil(funcionarioAtualizado.getPerfil());
                    return funcionarioRepository.save(funcionarioExistente);
                }).orElseThrow(() -> new RuntimeException("Funcionário não encontrado com o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com o ID: " + id));

        funcionarioRepository.delete(funcionario);
    }
}