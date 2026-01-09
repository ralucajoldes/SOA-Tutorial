package com.toie.tutorialRabbitMq;

import com.toie.tutorialRabbitMq.domain.TutorialModel;
import com.toie.tutorialRabbitMq.service.RabbitMqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor
public class TutorialRabbitMqApplication implements CommandLineRunner {

    private final RabbitMqProducer rabbitMqProducer;

    public static void main(String[] args) {
        SpringApplication.run(TutorialRabbitMqApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter something to be sent in a rabbitMq queue: ");
            String input = scanner.nextLine();

            var tutorialModel = TutorialModel.builder()
                    .id(UUID.randomUUID())
                    .data(input)
                    .build();

            rabbitMqProducer.sendMessage(tutorialModel);
        }
    }
}
