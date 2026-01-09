package com.toie.tutorialRabbitMq.service;

import com.toie.tutorialRabbitMq.domain.TutorialModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.toie.tutorialRabbitMq.config.RabbitMqConfig.QUEUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(TutorialModel tutorialModel) {
        log.info("RabbitMqProducer will send the following message {} to the queue {}", tutorialModel, QUEUE);
        rabbitTemplate.convertAndSend(QUEUE, tutorialModel);
    }
}
