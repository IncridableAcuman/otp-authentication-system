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

    @Bean
    Queue queue(){
        return new Queue(Endpoint.QUEUE_NAME);
    }
    @Bean
    DirectExchange exchange(){
        return new DirectExchange(Endpoint.EXCHANGE);
    }
    @Bean
    Binding binding(Queue queue,DirectExchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(Endpoint.ROUTING_KEY);
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
