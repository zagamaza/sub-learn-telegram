package ru.maza.telegram.infra.mq;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class CustomerService {

    private final RabbitTemplate rabbitTemplate;

    private final Exchange exchange;

    public CustomerService(RabbitTemplate rabbitTemplate, Exchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void createCustomer() {
        // ... do some database stuff
        String routingKey = "customer.created";
        String message = "customer created";
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, message);
    }
}
