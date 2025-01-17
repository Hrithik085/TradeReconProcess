package com.quod.bo.TradeReconProcess.broker.topics;

import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collection;
/**
 *  @author tra865
 *  @Date 17-Feb-2022
 *  @purpose Interface for providing topics
 */

public interface TopicsProvider {
    Collection<NewTopic> getTopics();
}
