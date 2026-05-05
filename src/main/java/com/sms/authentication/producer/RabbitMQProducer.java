package com.sms.authentication.producer;

import com.sms.authentication.constant.Endpoint;
import com.sms.authentication.dto.mail.EmailPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {
    private final RabbitTemplate template;

    public void sendMailWithRabbitMQ(EmailPayload payload){
        template
                .convertAndSend(
                        Endpoint.QUEUE_NAME,
                        Endpoint.ROUTING_KEY,
                        payload
                );
    }
}
