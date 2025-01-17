package com.quod.bo.TradeReconProcess.stream;

import com.quod.bo.TradeReconProcess.broker.topics.Topic;
import com.quod.bo.TradeReconProcess.model.*;
import com.quod.bo.TradeReconProcess.serialization.SerdesFactory;
import com.quod.bo.TradeReconProcess.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 17603
 * @date 17-06-2022
 */
@Configuration
@Slf4j
public class CsvTransactionStream {
    @Autowired
    Storage storage;

    @Autowired
    SerdesFactory serdesFactory;

    static String OFFSETS_STATE_STORE = "offsets-state-store";

    @Bean
    public KStream<TransactionKey, CsvTransaction> kStreamCommodityTrading(StreamsBuilder builder) {
        AtomicInteger slno = new AtomicInteger();
        var keySerde = serdesFactory.createAvroSerde(TransactionKey.class);
        var tradeSerde = serdesFactory.createAvroSerde(CsvTransaction.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //csv transaction to kStream
        KStream<TransactionKey, CsvTransaction> csvStream = builder
                .stream(Topic.CSV_TRANSACTIONS.getName(), Consumed.with(keySerde, tradeSerde));
        csvStream.print(Printed.toSysOut());

        //Offset-state-store
        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(OFFSETS_STATE_STORE),
                        Serdes.String(),
                        Serdes.Long()
                )
        );
//
//        var exchanges = builder.globalTable(Topic.EXCHANGES.getName(),
//                serdesFactory.consumeAvro(VVenue.class));

        //VAccountGroup globalKTable
        var clients = builder.globalTable(Topic.CLIENTS.getName(),
                serdesFactory.consumeAvro(VVenueAccountGroupKey.class, VVenueAccountGroup.class));


        //VListing globalKTable
        var listingGkTable = builder.globalTable(Topic.LISTING.getName(),
                serdesFactory.consumeAvro(VListingKey.class, VListing.class));

        // Settlement globalKTable
        var settlementGKTable = builder.globalTable(Topic.SETTLEMENT.getName(),
                serdesFactory.consumeAvro(SettlementKey.class, Settlement.class));

        KeyValueMapper<TransactionKey, TradeRecon, SettlementKey> settlementMapper =
                (key, value) -> {
                    SettlementKey settlementKey = new SettlementKey();
                    settlementKey.setVenueId(value.getVenueId());
                    settlementKey.setFromDate(value.getTranDate());
                    log.info("settlementMapper" + settlementKey);
                    return settlementKey;
                };

        ValueJoiner<TradeRecon, Settlement, TradeRecon> settlementJoiner =
                (transaction, settlement) -> {
                    log.info("SETTLEMENT::::::::::" + settlement);
                    if (settlement != null) {
                        log.info("settlementJoiner" + settlement);
                        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate localDate = LocalDate.parse(settlement.getFromDate(), formatters);
                        //   LocalDate fromDate = LocalDate.parse(settlement.getFromDate());
                        transaction.toBuilder().tranDate(String.valueOf(localDate))
                                .venueId(settlement.getVenueId()).build();
                        transaction.setSttlNo(settlement.getSttlNo());
                        log.info(":::::::::::::::::::::SETTLEMENT" + transaction);
                        return transaction;
                    }
                    return transaction;
                };
        KeyValueMapper<TransactionKey, TradeRecon, VListingKey> listingIdMapper =
                (key, value) -> {
                    VListingKey vListingKey = new VListingKey();
                    vListingKey.setSecurityId(value.getVenueInstrID());
                    vListingKey.setVenueId(value.getVenueId());
                    log.info("Listing1" + vListingKey);
                    return vListingKey;
                };

        ValueJoiner<TradeRecon, VListing, TradeRecon> listingIdJoiner =
                (transaction, listing) -> {
                    if (listing != null) {
                        // log.info("Listing1" + listing);
                        transaction.toBuilder().venueInstrID(listing.getSecurityID())
                                .venueId(listing.getVenueID()).build();
                        transaction.setListingId(listing.getListingID());
                        transaction.setInstrType(listing.getInstrType());
                        transaction.setInstrId(listing.getInstrID());

                        return transaction;
                    }
                    return transaction;
                };

        KeyValueMapper<TransactionKey, TradeRecon, VVenueAccountGroupKey> clientMapper =
                (key, value) -> {
                    VVenueAccountGroupKey groupKey = new VVenueAccountGroupKey();
                    groupKey.setVenueActGrpName(value.getVenueActGrpName());
                    groupKey.setVenueId(value.getVenueId());
                    log.info("CLIENT GROUP KEY" + groupKey);
                    return groupKey;
                };

        ValueJoiner<TradeRecon, VVenueAccountGroup, TradeRecon> clientJoiner =
                (transaction, client) -> {
                    if (client != null) {
                        log.info("inside client joiner :::::::::" + client);
                        transaction.toBuilder()
                                .venueActGrpName(client.getVenueActGrpName())
                                .venueId(client.getVenueId()).build();
                        transaction.setRemarks("Client and Exchange valid");
                        transaction.setAccountGroupId(client.getAccountGroupId());
                        transaction.setInstitutionId(client.getInstitutionId());
                        transaction.setLocationId(client.getLocationId());
                        transaction.setTradeStatus("Y");
                        log.info("INSIDE CLIENT VALIDATION :::::" + transaction);
                        return transaction;
                    }
                    transaction.setRemarks("Client or Exchange invalid");
                    transaction.setTradeStatus("N");
                    return transaction;
                };

        KStream<TradeReconKey, TransactionError> resultStream = csvStream
                .transform(TransactionFilter::new, OFFSETS_STATE_STORE)
                .mapValues(value -> TradeRecon.builder()
                        .slNO(slno.getAndIncrement())
                        .tranDate(value.getTranDate())
                        .venueActGrpName(value.getVenueAccId())
                        .transactionNo(Long.valueOf(value.getTradeNo()))
                        .isin(value.getIsin())
                        .venueId(value.getVenueId())
                        .lastUpdatedOn(LocalDateTime.now(ZoneOffset.UTC))
                        //   .accountGroupId(value.getClientId())
                        .side(value.getBuySell())
                        .currencyCode(value.getCurrencyCode())
                        .qty(value.getQty())
                        .price(value.getRate())
                        .venueInstrID(value.getSecurityId())
                        .orderID(value.getOrderNo())
                        // .tradedCode(value.getTradeCode())
                        // .accountGroupId(value.getClientId())
                        .alive("Y")
                        // .tradeStatus("P")
                        .build())
                // .leftJoin(exchanges, exchangeMapper, exchangeJoiner)
                .leftJoin(clients, clientMapper, clientJoiner)
                .leftJoin(listingGkTable, listingIdMapper, listingIdJoiner)
                .leftJoin(settlementGKTable, settlementMapper, settlementJoiner)
                .transform(TransactionKeyTransform::new)
                .transform(() -> new TransactionSaver(storage));

        resultStream.print(Printed.toSysOut());

//        resultStream.to(Topic.TRANSACTIONS_TMP.getName(),
//                serdesFactory.produceAvro
//                        (TradeReconKey.class, TransactionError.class, (topic, key, value, numPartitions)
//                                -> PartitionUtils.getPartition(value, numPartitions)
//                        )
//        );

        //    resultStream.print(Printed.toSysOut());
        return csvStream;
    }

    private static class TransactionKeyTransform implements
            Transformer<TransactionKey, TradeRecon, KeyValue<TradeReconKey, TradeRecon>> {

        private ProcessorContext context;

        @Override
        public void init(ProcessorContext context) {
            this.context = context;
        }

        @Override
        public KeyValue<TradeReconKey, TradeRecon> transform(TransactionKey key, TradeRecon value) {
//            log.info("TRANSFORM key" + key);
//            log.info("TRANSFORM value" + value);
//            if (Objects.equals(value.getBuySell(), "S")) {
//                value.setSignedQty(-value.getQty());
//            } else {
//                value.setSignedQty(value.getQty());
//            }

            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            try {
                d = f.parse(value.getTranDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert d != null;
            long milliseconds = d.getTime();
            var transactionNo = milliseconds + value.getOrderID();
            value.setTransactionNo(Long.valueOf(transactionNo));
            log.info("KEY :::::::::::::::::" + transactionNo);
            return new KeyValue<>(TradeReconKey.builder()
                    .transactionNo(transactionNo).build(), value);
        }

        @Override
        public void close() {
            // no-op
        }
    }

    private static class TransactionFilter implements Transformer<TransactionKey, CsvTransaction, KeyValue<TransactionKey, CsvTransaction>> {

        private static final String OFFSET_KEY = "transaction-tmp-enrichment";

        private ProcessorContext context;
        private KeyValueStore<String, Long> offsets;

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

    private static final class TransactionSaver implements Transformer<TradeReconKey, TradeRecon, KeyValue<TradeReconKey, TransactionError>> {

        private static final Logger logger = LoggerFactory.getLogger(TransactionSaver.class);

        private final Storage storage;

        public TransactionSaver(Storage storage) {
            this.storage = storage;
        }

        @Override
        public void init(ProcessorContext context) {
        }

        @Override
        public KeyValue<TradeReconKey, TransactionError> transform(TradeReconKey key, TradeRecon value) {
            try {
                storage.persistTransaction(key, value);
            } catch (SQLException e) {
                logger.error("Unable to persist transaction. Key: {}, Value: {}", key, value, e);
                return new KeyValue<>(
                        key,
                        TransactionError.builder()
                                .transaction(value)
                                .error(e.getMessage())
                                .build()
                );
            }
            return null;
        }

        @Override
        public void close() {
            // no-op
        }
    }


}
