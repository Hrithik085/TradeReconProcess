package com.quod.bo.TradeReconProcess.broker.topics;

import lombok.experimental.UtilityClass;
import org.apache.kafka.common.config.TopicConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tra865
 * @Date 16-Feb-2022
 * @purpose Topic configurator class
 */

@UtilityClass
public class CustomTopicConfigs {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Map<String, String> configs = new HashMap<>();

        public Builder cleanupPolicyCompact() {
            configs.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);
            return this;
        }

        public Builder retentionMs(long ms) {
            configs.put(TopicConfig.RETENTION_MS_CONFIG, Long.toString(ms));
            return this;
        }

        public Builder minInsyncReplicas(int num) {
            configs.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, Integer.toString(num));
            return this;
        }

        public Map<String, String> build() {
            return Collections.unmodifiableMap(configs);
        }
    }
}
