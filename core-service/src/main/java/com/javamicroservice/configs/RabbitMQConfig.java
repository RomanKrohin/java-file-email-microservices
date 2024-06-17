package com.javamicroservice.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
