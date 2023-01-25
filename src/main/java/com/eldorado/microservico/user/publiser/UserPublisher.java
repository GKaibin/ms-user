package com.eldorado.microservico.user.publiser;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPublisher {

    private final RabbitTemplate rabbitTemplate;

    private final Queue queue;

    public void sendToQueue(final String message) {
        rabbitTemplate.convertAndSend("create", "create-routing", message);
        rabbitTemplate.convertAndSend("", "update-routing", message);
    }
}
