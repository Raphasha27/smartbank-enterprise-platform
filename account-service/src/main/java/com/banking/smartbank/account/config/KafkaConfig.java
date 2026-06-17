package com.banking.smartbank.account.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic debitRequestsTopic() {
        return TopicBuilder.name("debit-requests").partitions(6).replicas(1).build();
    }

    @Bean
    public NewTopic debitResponsesTopic() {
        return TopicBuilder.name("debit-responses").partitions(6).replicas(1).build();
    }
}
