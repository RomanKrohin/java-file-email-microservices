package com.example.mailservice;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Properties;

@Configuration
public class RabbitMQConfig {

    public static final String UPLOAD_QUEUE = "uploadQueue";
    public static final String DOWNLOAD_QUEUE = "downloadQueue";

    public static final String AUTH_QUEUE = "authQueue";

    @Bean
    public Queue uploadQueue() {
        return new Queue(UPLOAD_QUEUE, false);
    }

    @Bean
    public Queue downloadQueue() {
        return new Queue(DOWNLOAD_QUEUE, false);
    }

    @Bean
    public Queue authQueue() {
        return new Queue(AUTH_QUEUE, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("coreExchange");
    }

    @Bean
    public Binding uploadBinding(Queue uploadQueue, TopicExchange exchange) {
        return BindingBuilder.bind(uploadQueue).to(exchange).with("uploadRoutingKey");
    }

    @Bean
    public Binding downloadBinding(Queue downloadQueue, TopicExchange exchange) {
        return BindingBuilder.bind(downloadQueue).to(exchange).with("downloadRoutingKey");
    }

    @Bean
    public Binding authBinding(Queue downloadQueue, TopicExchange exchange) {
        return BindingBuilder.bind(downloadQueue).to(exchange).with("authRoutingKey");
    }

    @Bean
    public MessageListenerContainer uploadMessageListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter uploadListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("uploadQueue");
        container.setMessageListener(uploadListenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerContainer downloadMessageListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter downloadListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("downloadQueue");
        container.setMessageListener(downloadListenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerContainer authMessageListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter downloadListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("authQueue");
        container.setMessageListener(downloadListenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter uploadListenerAdapter(MailService receiver) {
        return new MessageListenerAdapter(receiver, "receiveUploadMessage");
    }

    @Bean
    public MessageListenerAdapter downloadListenerAdapter(MailService receiver) {
        return new MessageListenerAdapter(receiver, "receiveDownloadMessage");
    }

    @Bean
    public MessageListenerAdapter authListenerAdapter(MailService receiver) {
        return new MessageListenerAdapter(receiver, "receiveAuthMessage");
    }
}


