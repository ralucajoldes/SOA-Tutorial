package com.toie.tutorialRabbitMq.service;

import com.toie.tutorialRabbitMq.domain.TutorialModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.toie.tutorialRabbitMq.config.RabbitMqConfig.QUEUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {

    @RabbitListener(
            queues = QUEUE,
            autoStartup = "true"
    )
    public void processCustomerOrder(TutorialModel tutorialModel) {
        log.info("The RabbitMqListener received the following message {} via the queue {}", tutorialModel, QUEUE);
    }
}
