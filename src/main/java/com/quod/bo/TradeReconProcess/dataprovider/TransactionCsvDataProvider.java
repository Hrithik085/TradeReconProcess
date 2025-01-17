package com.quod.bo.TradeReconProcess.dataprovider;


import com.quod.bo.TradeReconProcess.broker.topics.Topic;
import com.quod.bo.TradeReconProcess.model.CsvTransaction;
import com.quod.bo.TradeReconProcess.model.TransactionKey;
import com.quod.bo.TradeReconProcess.partitioning.PartitionUtils;
import com.quod.bo.TradeReconProcess.serialization.SerdesFactory;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Future;

@Component
@Scope("prototype")
public class TransactionCsvDataProvider extends CsvDataProvider<TransactionKey, CsvTransaction> {

    private final Topic topic;

    private Path file;
    private UUID fileId;
    private int rowNumber;

    public TransactionCsvDataProvider(Path folder, long checkIntervalMs,
                                      Class<CsvTransaction> valueClass, Topic topic,
                                      SerdesFactory serdesFactory) {
        super(folder, checkIntervalMs, valueClass, serdesFactory);
        this.topic = Objects.requireNonNull(topic, "topic");
    }

    @Override
    protected Future<Void> processFile(Path file) {
        this.file = file;
        this.fileId = UUID.randomUUID();
        this.rowNumber = 1;
        return super.processFile(file);
    }

    @Override
    protected Class<TransactionKey> getKeyClass() {
        return TransactionKey.class;
    }

    @Override
    protected Class<CsvTransaction> getValueClass() {
        return CsvTransaction.class;
    }

    @Override
    protected ProducerRecord<TransactionKey, CsvTransaction> createProducerRecord(CsvTransaction value) {
        value.setSource(file.getFileName().toString());
        var key = TransactionKey.builder()
                .fileId(fileId)
                .rowNumber(rowNumber++)
                .build();
        var partition = PartitionUtils.getPartition(value, topic.getNumPartitions());
        value.setSlNO(rowNumber++);
        value.setTotalRow(value.getSlNO());
        return new ProducerRecord<>(topic.getName(), partition, key, value);
    }
}
