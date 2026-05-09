package com.sms.authentication.producer;

import com.sms.authentication.dto.mail.EmailPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

<<<<<<< HEAD
import static com.sms.authentication.config.RabbitMQConfig.EXCHANGE;
import static com.sms.authentication.config.RabbitMQConfig.ROUTING_KEY;
=======
import static com.sms.authentication.config.RabbitMQConfig.*;

>>>>>>> develop

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {
    private final RabbitTemplate template;

    public void sendMailWithRabbitMQ(EmailPayload payload){
        template
                .convertAndSend(
                        EXCHANGE,
                        ROUTING_KEY,
                        payload
                );
    }
}
