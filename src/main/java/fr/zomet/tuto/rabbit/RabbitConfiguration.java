package fr.zomet.tuto.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class RabbitConfiguration {

    @Value("${rabbitmq.topic.name}")
    private String rabbitTopicName;

    @Value("${rabbitmq.queue.name}")
    private String rabbitQueueName;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(rabbitQueueName, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(rabbitTopicName);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(routingKey);
    }
}
