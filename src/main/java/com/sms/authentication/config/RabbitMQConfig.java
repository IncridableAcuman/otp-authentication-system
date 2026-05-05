package com.sms.authentication.config;

import com.sms.authentication.constant.Endpoint;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "auth_queue";
    public static final String EXCHANGE = "auth_exchange";
    public static final String ROUTING_KEY = "auth_routing";

    @Bean
    Queue queue(){
        return new Queue(QUEUE_NAME);
    }
    @Bean
    DirectExchange exchange(){
        return new DirectExchange(EXCHANGE);
    }
    @Bean
    Binding binding(Queue queue,DirectExchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new JacksonJsonMessageConverter();
    }
    @Bean
    public AmqpTemplate template(ConnectionFactory factory){
        final RabbitTemplate template =new RabbitTemplate(factory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
