# RabbitMQ with Spring Boot

This tutorial demonstrates how to use RabbitMQ together with a Spring Boot application.

## Prerequisites

- Java 11+
- Spring Boot
- Docker (for running RabbitMQ locally)

## 1. Create a Spring Boot Application

## 2. Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

## 3. Configure RabbitMQ

### 3.1 Create a configuration class where you should define some beans

### 3.2 Define an Exchange

Create a `@Bean` of type `Exchange`:

```java
@Bean
public Exchange tutorialExchange() {
    final var exchange = new TopicExchange("tutorialQueue");
    exchange.setShouldDeclare(true);
    return exchange;
}
```

### 3.3 Define a Queue

Create a `@Bean` of type `Queue`:

```java
@Bean
public Queue tutorialQueue() {
    return QueueBuilder.durable("tutorialQueue")
        .quorum()
        .deadLetterExchange("tutorialQueue.dlq")
        .deadLetterRoutingKey("deadLetter")
        .build();
}
```

### 3.4 Bind Queue to Exchange

Create a `@Bean` of type `Binding` to link the queue and exchange:

```java
@Bean
public Binding tutorialQueueBinding(final Exchange tutorialExchange, final Queue tutorialQueue) {
    return BindingBuilder.bind(tutorialQueue).to(tutorialExchange)
        .with("tutorial").noargs();
}
```

### 3.5 Define a Dead Letter Exchange

Create a `@Bean` for a dead letter exchange:

```java
@Bean
public Exchange tutorialDeadLetterExchange() {
    return new DirectExchange("tutorialQueue.dlx");
}
```

### 3.6 Define a Dead Letter Queue

Create a `@Bean` of type `Queue` for dead letter messages:

```java
@Bean
public Queue tutorialDeadLetterQueue() {
    return QueueBuilder.durable("tutorialQueue.dlq")
        .quorum()
        .build();
}
```

### 3.7 Bind Dead Letter Queue to Dead Letter Exchange

Create a `@Bean` of type `Binding` to connect the dead letter queue:

```java
@Bean
public Binding tutorialDeadLetterQueueBinding(final Exchange tutorialDeadLetterExchange, final Queue tutorialDeadLetterQueue) {
    return BindingBuilder.bind(tutorialDeadLetterQueue).to(tutorialDeadLetterExchange).with("deadLetter").noargs();
}
```

### 3.8 Configure JSON Message Converter

Create a `@Bean` of type `MessageConverter` for messages conversion. This is used to have the DTO automatically converted to a json object in the queue when sending data. When receiving data it will automatically transform it to the corresponding model:

```java
@Bean
public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter(new ObjectMapper());
}
```

### 3.9 Try other configurations
Feel free to try and experiment all the available configuration options available. Here is just a simple ecemple of what you can achieve. You can find more at *https://www.rabbitmq.com/docs*

## 4. Create a DTO Class

Define a DTO to encapsulate the message data:

```java
public class MyMessage {
    private String content;
    
    // Constructors, Getters, and Setters
}
```

## 5. Implement a RabbitMQ Producer

The producer is responsible for sending messages to RabbitMQ. It uses the `RabbitTemplate` to publish messages to a specific queue.

```java
@Service
public class RabbitMqProducer {
    private final RabbitTemplate rabbitTemplate;
    
    public RabbitMqProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void sendMessage(MyMessage message) {
        System.out.println("Sending message: " + message.getContent());
        rabbitTemplate.convertAndSend("tutorialQueue", message);
    }
}
```

- `RabbitTemplate` is a helper class provided by Spring to interact with RabbitMQ.
- `convertAndSend()` is used to send and convert using the MessageConverter @Bean a message to the specified queue.


## 6. Implement a RabbitMQ Listener

The consumer listens for messages on the specified queue and processes them asynchronously.

```java
@Service
public class RabbitMqListener {
    
    @RabbitListener(queues = "tutorialQueue")
    public void receiveMessage(MyMessage message) {
        System.out.println("Received message: " + message.getContent());
        // Process the message as needed
    }
}
```

- The `@RabbitListener` annotation tells Spring to listen on the `tutorialQueue` for incoming messages.
- The method receives the `MyMessage` object, which contains the message data.
- You can add additional processing logic inside the `receiveMessage` method.


## 7. Running RabbitMQ Locally with Docker

Use Docker to set up a RabbitMQ instance:

```yaml
version: '3.8'
services:
  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq-for-tutorial
    ports:
      - "5672:5672"
      - "15672:15672"
    networks:
      - my_network
networks:
  my_network:
```

Run RabbitMQ with:

```sh
docker-compose up -d
```

## 8. Configure RabbitMQ in Spring Boot

Add the following to `application.yml`:

```yaml
spring:
  rabbitmq:
    host: localhost
```

## 9. Monitoring RabbitMQ

Access the RabbitMQ management dashboard at [http://localhost:15672](http://localhost:15672).

Use default credentials:
- **Username**: `guest`
- **Password**: `guest`

You can manually publish messages and monitor queues via the management UI.

## Conclusion

You have successfully set up RabbitMQ with Spring Boot! 🚀

