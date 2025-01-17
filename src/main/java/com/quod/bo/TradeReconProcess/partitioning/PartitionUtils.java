package com.quod.bo.TradeReconProcess.partitioning;


import com.quod.bo.TradeReconProcess.model.CsvTransaction;
import com.quod.bo.TradeReconProcess.model.TradeRecon;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class PartitionUtils {

    public static int getPartition(CsvTransaction transaction, int numPartitions) {
        int hash = Objects.hash(transaction.getIsin(), transaction.getTradeCode());
        return getPartition(hash, numPartitions);
    }

    public static int getPartition(TradeRecon transaction, int numPartitions) {
        int hash = Objects.hash(transaction.getIsin(), transaction.getAccountGroupId());
        return getPartition(hash, numPartitions);
    }

    private static int getPartition(int hash, int numPartitions) {
        return Math.abs(hash) % numPartitions;
    }
}
