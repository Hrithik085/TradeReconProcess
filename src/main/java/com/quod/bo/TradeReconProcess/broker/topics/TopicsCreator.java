package com.quod.bo.TradeReconProcess.broker.topics;



import com.quod.bo.TradeReconProcess.properties.PropertiesProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author tra865
 * @Date 17-Feb-2022
 * @purpose Kafka Topic Creator
 */
@Slf4j
public class TopicsCreator {

    private final TopicsProvider topicsProvider;
    private Properties property;

    public TopicsCreator(TopicsProvider topicsProvider) {
        this.topicsProvider = topicsProvider;
    }

    public void start() throws ExecutionException {
        try (Admin adminClient = KafkaAdminClient.create(PropertiesProvider.KAFKA_PRODUCER.getProperties())) {
            Set<String> existingTopics = adminClient.listTopics().names().get();
            List<NewTopic> topicsToCreate = topicsProvider.getTopics().stream()
                    .filter(topic -> !existingTopics.contains(topic.name()))
                    .collect(Collectors.toList());
            if (!topicsToCreate.isEmpty()) {
                adminClient.createTopics(topicsToCreate).all().get();
                log.info("Topics successfully created");
            } else {
                log.info("All topics already created");
            }
        } catch (InterruptedException e) {
            log.error("Unexpected interruption", e);
            Thread.currentThread().interrupt();
        }
    }
}
