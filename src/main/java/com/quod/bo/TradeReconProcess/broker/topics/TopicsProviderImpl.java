package com.quod.bo.TradeReconProcess.broker.topics;

import org.apache.kafka.clients.admin.NewTopic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author tra865
 * @Date 17-Feb-2022
 * @purpose Implementation class of TopicsProvider
 */

public class TopicsProviderImpl implements TopicsProvider {
    /**
     * @return Collection<NewTopic>
     */
    @Override
    public Collection<NewTopic> getTopics() {
        return Arrays.stream(Topic.values())
                .filter(topic -> topic.getNumPartitions() > 0)
                .map(topic -> {
                    NewTopic newTopic = new NewTopic(topic.getName(), topic.getNumPartitions(), topic.getReplicationFactor());
                    Optional.ofNullable(topic.getCustomConfigs()).ifPresent(newTopic::configs);
                    return newTopic;
                })
                .collect(Collectors.toList());
    }
}
