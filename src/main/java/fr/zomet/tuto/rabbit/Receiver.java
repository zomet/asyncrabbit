package fr.zomet.tuto.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

    @Value("${rabbitmq.queue.name}")
    private String rabbitQueueName;

    private final RabbitTemplate rabbitTemplate;
    private final Sender sender;

    public Receiver(RabbitTemplate rabbitTemplate, Sender sender) {
        this.rabbitTemplate = rabbitTemplate;
        this.sender = sender;
    }

    @Retryable(value = NoSuchElementException.class, maxAttempts = 100, backoff = @Backoff(delay = 100))
    public String receiveMessage(String correlationId) throws NoSuchElementException {
        Message message = rabbitTemplate.receive(rabbitQueueName);
        if (message == null) {
            throw new NoSuchElementException();
        }
        if (!correlationId.equals(message.getMessageProperties().getCorrelationId())) {
            sender.requeueMessage(message);
            throw new NoSuchElementException();
        }
        return new String(message.getBody());
    }
}
