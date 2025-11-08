package br.com.meuestudodespring.minha_api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "email.exchange";
    public static final String QUEUE_NAME = "email.boas-vindas.fila";
    public static final String ROUTING_KEY = "email.boas-vindas.key";

    // 1. Cria a Fila (Onde as mensagens ficam)
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true); // 'true' = fila durável
    }

    // 2. Cria o Roteador (Para onde o Produtor envia)
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    // 3. Cria a Ligação (Binding)
    //    Diz: "Mensagens enviadas para o 'email.exchange' (2)
    //    com a chave 'email.boas-vindas.key' (Routing Key)
    //    devem ser entregues na 'email.boas-vindas.fila' (1)."
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
