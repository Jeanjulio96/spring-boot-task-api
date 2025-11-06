package br.com.meuestudodespring.minha_api; // 1. O pacote de teste

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

// 2. Diz ao JUnit 5 para "ligar" o Mockito
@ExtendWith(MockitoExtension.class) 
class TarefaControllerTest {

    // 3. Cria um "dublê" (mock) para o repositório.
    //    Este objeto não acessa o banco de dados.
    @Mock
    private TarefaRepository tarefaRepository;

    // 4. Cria uma instância REAL do TarefaController
    //    e INJETA os mocks (@Mock) que estão nesta classe nele.
    @InjectMocks
    private TarefaController tarefaController;

    // 5. Esta é a definição do nosso teste.
    @Test
    public void deveListarTodasAsTarefas() {
        // --- ARRANGE (Arrumar / Preparar) ---
        
        // Criamos uma tarefa falsa
        Tarefa tarefaFalsa = new Tarefa();
        tarefaFalsa.setId(1L);
        tarefaFalsa.setTitulo("Tarefa Falsa de Teste");

        // Criamos uma lista falsa
        List<Tarefa> listaFalsa = List.of(tarefaFalsa);

        // 6. A "mágica" do Mockito:
        // Dizemos: "QUANDO (when) o 'tarefaRepository.findAll()' for chamado,
        // ENTÃO (thenReturn) retorne a 'listaFalsa'."
        Mockito.when(tarefaRepository.findAll()).thenReturn(listaFalsa);

        // --- ACT (Agir / Executar) ---
        
        // 7. Chamamos o método real do controller que queremos testar
        List<Tarefa> resultado = tarefaController.listarTarefas();

        // --- ASSERT (Verificar / Afirmar) ---
        
        // 8. Usamos o JUnit 5 para verificar se o resultado é o esperado
        
        // O resultado não pode ser nulo
        Assertions.assertNotNull(resultado);
        // O tamanho da lista deve ser 1
        Assertions.assertEquals(1, resultado.size());
        // O título da primeira tarefa deve ser o que esperamos
        Assertions.assertEquals("Tarefa Falsa de Teste", resultado.get(0).getTitulo());
        
        // 9. (Opcional) Verifica se o mock foi realmente chamado
        Mockito.verify(tarefaRepository, Mockito.times(1)).findAll();
    }
}