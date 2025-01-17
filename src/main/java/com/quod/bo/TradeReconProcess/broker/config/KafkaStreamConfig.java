package com.quod.bo.TradeReconProcess.broker.config;

import com.quod.bo.TradeReconProcess.broker.topics.TopicsCreator;
import com.quod.bo.TradeReconProcess.broker.topics.TopicsProviderImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

/**
 * @author 17603
 * @date 17-06-2022
 */
@Slf4j
@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {
    @Value(value = "${bootstrap.servers}")
    private String bootstrapAddress;

    @Value(value = "${application.id}")
    private String applicationId;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        var props = new HashMap<String, Object>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId + UUID.randomUUID());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "3000");
        //exactly once semantic
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);
        //idempotent producer
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        return new KafkaStreamsConfiguration(props);
    }
    @Bean
    public void topiCreateNew(){
        var topicsCreator = new TopicsCreator( new TopicsProviderImpl());
        try {
            topicsCreator.start();
            log.info("Topic creation successful");
        } catch (ExecutionException e) {
            log.error("Topics creation error", e);
            exit(-1);
        }
    }
}
