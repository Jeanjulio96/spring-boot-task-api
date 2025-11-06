package br.com.meuestudodespring.minha_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    // 1. Injeção de Dependência:
    // O Spring vai automaticamente criar uma instância do TarefaRepository
    // e "injetá-la" aqui para nós.
    @Autowired
    private TarefaRepository tarefaRepository;

    // 2. Endpoint para CRIAR uma nova tarefa (HTTP POST)
    // @RequestBody diz ao Spring para converter o JSON do corpo da requisição
    // em um objeto Tarefa.
    @PostMapping
    public Tarefa criarTarefa(@RequestBody Tarefa tarefa) {
        // Usa o método .save() que ganhamos de graça do JpaRepository
        return tarefaRepository.save(tarefa);
    }

    // 3. Endpoint para LISTAR todas as tarefas (HTTP GET)
    @GetMapping
    public List<Tarefa> listarTarefas() {
        // Usa o método .findAll()
        return tarefaRepository.findAll();
    }
    
    // (Pode apagar aquele método @GetMapping("/hello") de antes)
}