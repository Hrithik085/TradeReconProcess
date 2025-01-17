package com.quod.bo.TradeReconProcess.broker.config;

import com.quod.bo.TradeReconProcess.model.ProcessRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 16624
 * @date 16-06-2022
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig
{
    @Value(value = "${bootstrap.servers}")
    private String bootstrapAddress;

    @Value(value = "${recon.topic.group.id}")
    private String reconRequestGroupId;

    //Consume TradeReconRequest objects from Kafka

    public ConsumerFactory<String, ProcessRequest> reconRequestConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, reconRequestGroupId + UUID.randomUUID());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(ProcessRequest.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProcessRequest>
    reconRequestKafkaListenerContainerFactory(KafkaTemplate<String, Object> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, ProcessRequest> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(reconRequestConsumerFactory());
        factory.setReplyTemplate(kafkaTemplate);
        return factory;
    }
}
