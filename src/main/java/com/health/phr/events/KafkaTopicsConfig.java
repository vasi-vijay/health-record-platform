package com.health.phr.events;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * The single topic used in the 8-week plan. The outbox publisher (week 4) writes record-access and
 * consent events here; the notification module consumes them.
 */
@Configuration
class KafkaTopicsConfig {

    @Bean
    NewTopic recordEventsTopic(@Value("${phr.kafka.record-events-topic}") String name) {
        return TopicBuilder.name(name).partitions(1).replicas(1).build();
    }
}
