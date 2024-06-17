package com.javamicroservice;

import  org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JavaMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaMicroserviceApplication.class, args);
    }

}
