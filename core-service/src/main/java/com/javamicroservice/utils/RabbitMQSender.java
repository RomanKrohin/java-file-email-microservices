package com.javamicroservice.utils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendUploadMessage(String email, String content) {
        String message = email + ":" + content;
        rabbitTemplate.convertAndSend("coreExchange", "uploadRoutingKey", message);
    }

    public void sendDownloadMessage(String email, String content) {
        String message = email + ":" + content;
        rabbitTemplate.convertAndSend("coreExchange", "downloadRoutingKey", message);
    }

    public void sendAuthMessage(String email, String content) {
        String message = email + ":" + content;
        rabbitTemplate.convertAndSend("coreExchange", "authRoutingKey", message);
    }
}

