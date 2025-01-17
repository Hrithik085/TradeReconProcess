package com.quod.bo.TradeReconProcess.util;

import com.quod.bo.TradeReconProcess.model.CsvTransaction;
import com.quod.bo.TradeReconProcess.model.TransactionKey;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Optional;

/**
 * @author 17603
 * @date 08-07-2022
 */
public class TransactionFilter implements Transformer<TransactionKey, CsvTransaction, KeyValue<TransactionKey, CsvTransaction>> {

    private static final String OFFSET_KEY = "transaction-tmp-enrichment";
    private String OFFSETS_STATE_STORE;

    private ProcessorContext context;
    private KeyValueStore<String, Long> offsets;

    public TransactionFilter(String offsets_state_store) {
        OFFSETS_STATE_STORE = offsets_state_store;
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.offsets = context.getStateStore(OFFSETS_STATE_STORE);
    }

    @Override
    public KeyValue<TransactionKey, CsvTransaction> transform(TransactionKey key, CsvTransaction value) {
        long offset = Optional.ofNullable(offsets.get(OFFSET_KEY)).orElse(-1L);
        if (offset < context.offset()) {
            offsets.put(OFFSET_KEY, context.offset());
            return new KeyValue<>(key, value);
        }
        return null;
    }

    @Override
    public void close() {
        // no-op
    }
}
