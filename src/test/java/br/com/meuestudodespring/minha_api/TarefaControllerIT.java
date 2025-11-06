package br.com.meuestudodespring.minha_api;

// Imports do MockMvc para simular requisições HTTP
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is; // Para o jsonPath

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser; // Para "fingir" o login
import org.springframework.test.web.servlet.MockMvc;

// 1. @SpringBootTest: Sobe a APLICAÇÃO INTEIRA
//    (Conecta no banco, sobe o Tomcat, etc)
@SpringBootTest
// 2. @AutoConfigureMockMvc: Nos dá uma ferramenta (MockMvc)
//    para fazer requisições HTTP falsas para nossa API
@AutoConfigureMockMvc
public class TarefaControllerIT {

    // 3. Injeta o "Navegador Falso" (MockMvc)
    @Autowired
    private MockMvc mockMvc;

    // 4. Injeta o REPOSITÓRIO REAL
    //    Não é um @Mock! É o repositório de verdade.
    @Autowired
    private TarefaRepository tarefaRepository;

    // [ ADICIONE ESTE MÉTODO ]
    // Garante que o banco esteja LIMPO *ANTES* de cada teste rodar
    @BeforeEach
    public void limparBancoAntes() {
        tarefaRepository.deleteAll();
    }
    
    // O método @AfterEach que já tínhamos pode até ser removido
    // se você usar o @BeforeEach, mas podemos manter os dois.
    @AfterEach
    public void limparBancoDepois() {
        tarefaRepository.deleteAll();
    }

    // 5. [ IMPORTANTE ] Limpa o banco de dados DEPOIS de cada teste
    //    Isso evita que um teste "suje" o próximo.
    @AfterEach
    public void limparBanco() {
        tarefaRepository.deleteAll();
    }

    // 6. O Teste
    @Test
    // 7. [ A MÁGICA DA SEGURANÇA ]
    //    Diz ao Spring Security: "Para este teste, finja que
    //    um usuário chamado 'user' está logado".
    //    Isso bypassa nosso filtro de JWT!
    @WithMockUser 
    public void deveListarTarefasComSucesso() throws Exception {
        
        // --- ARRANGE (Arrumar / Preparar) ---
        
        // 8. Como estamos com o banco real, vamos SALVAR uma tarefa de verdade
        Tarefa tarefaReal = new Tarefa();
        tarefaReal.setTitulo("Minha Tarefa de IT");
        tarefaReal.setDescricao("Descricao de teste");
        tarefaReal.setConcluida(false);
        tarefaRepository.save(tarefaReal);

        // --- ACT (Agir / Executar) ---
        
        // 9. Executa uma requisição GET HTTP para "/api/tarefas"
        //    e espera as verificações (Asserts)
        mockMvc.perform(get("/api/tarefas"))
        
        // --- ASSERT (Verificar / Afirmar) ---
        
                // 10. Verifica se o status HTTP foi 200 OK
                .andExpect(status().isOk())
                
                // 11. Verifica se o JSON de resposta (no primeiro item da lista, $[0])
                //     tem um campo "titulo" com o valor "Minha Tarefa de IT"
                .andExpect(jsonPath("$[0].titulo", is("Minha Tarefa de IT")));
    }
}