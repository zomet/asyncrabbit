package fr.zomet.tuto.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Sender<T> {

    @Value("${rabbitmq.topic.name}")
    private String rabbitTopicName;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public Sender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(T t, CorrelationId correlationId) {
        try {
            rabbitTemplate.convertAndSend(rabbitTopicName, routingKey, buildMessage(t, correlationId.toString()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    void requeueMessage(Message message) {
        rabbitTemplate.send(rabbitTopicName, routingKey, message);
    }

    private Message buildMessage(T t, String correlationId) throws JsonProcessingException {
        return new Message(objectMapper.writeValueAsBytes(t), buildMessageProperties(correlationId));
    }

    private MessageProperties buildMessageProperties(String correlationId) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(correlationId);
        messageProperties.setContentType("application/json");
        return messageProperties;
    }
}
