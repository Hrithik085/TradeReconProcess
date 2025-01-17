package com.quod.bo.TradeReconProcess.broker.topics;

import java.util.Map;

/**
 * @author tra865
 * @Date 16-Feb-2022
 * @purpose Loading Kafka Topics
 */

public enum Topic {

    CSV_TRANSACTIONS("csv-transactions", 4, 3,
            CustomTopicConfigs.builder().minInsyncReplicas(2).build()),
    TRANSACTIONS_TMP("transactions-tmp", 4, 3,
            CustomTopicConfigs.builder().minInsyncReplicas(2).build()),
    UNSAVED_TRANSACTIONS("unsaved-transactions"),
    REQUEST_LISTENER("trade-recon-request", 1, 1),
    CLIENTS("db.vvenueaccountgroup"),
    SETTLEMENT("db.settlement"),
    LISTING("db.vlistingBO"),
    PROCESS_RESPONSE("db.processorReport", 1, 1);

    private final String name;
    private final int numPartitions;
    private final short replicationFactor;
    private final Map<String, String> customConfigs;

    Topic(String name, int numPartitions, int replicationFactor, Map<String, String> customConfigs) {
        this.name = name;
        this.numPartitions = numPartitions;
        this.replicationFactor = (short) replicationFactor;
        this.customConfigs = customConfigs;
    }

    Topic(String name) {
        this(name, -1, -1);
    }

    Topic(String name, int numPartitions, int replicationFactor) {
        this(name, numPartitions, replicationFactor, null);
    }

    public String getName() {
        return name;
    }

    public int getNumPartitions() {
        return numPartitions;
    }

    public short getReplicationFactor() {
        return replicationFactor;
    }

    public Map<String, String> getCustomConfigs() {
        return customConfigs;
    }
}


