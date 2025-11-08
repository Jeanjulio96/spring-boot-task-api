package br.com.meuestudodespring.minha_api.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    // 1. A "mágica" do Spring:
    //    Esta anotação diz: "Fique ouvindo a fila chamada 'email.boas-vindas.fila'.
    //    Quando uma mensagem (String) chegar, execute este método."
    @RabbitListener(queues = "email.boas-vindas.fila")
    public void ouvirEmailBoasVindas(String emailDoUsuario) {
        
        log.info("MENSAGEM RECEBIDA! Iniciando envio de e-mail para: " + emailDoUsuario);

        // 2. Simulação de trabalho LENTO
        try {
            // Finge que está conectando no servidor de e-mail (ex: Gmail)
            Thread.sleep(5000); // 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 3. O trabalho terminou
        log.info("E-mail de boas-vindas enviado com sucesso para: " + emailDoUsuario);
    }
}