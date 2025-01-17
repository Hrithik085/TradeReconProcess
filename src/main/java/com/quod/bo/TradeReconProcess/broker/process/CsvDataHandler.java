package com.quod.bo.TradeReconProcess.broker.process;

import com.opencsv.bean.CsvToBeanBuilder;
import com.quod.bo.TradeReconProcess.model.TradeRecon;
import com.quod.bo.TradeReconProcess.model.CsvTransaction;
import com.quod.bo.TradeReconProcess.properties.PropertiesProvider;
import com.quod.bo.TradeReconProcess.serialization.SerdesFactory;
import com.quod.bo.TradeReconProcess.broker.topics.Topic;
import com.quod.bo.TradeReconProcess.util.FileReadUtility;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author 09607
 * @Date 20-Feb-2022
 * @purpose CsvDataHandler process will start when CSV upload event received.
 * Process reads the file and convert to corresponding object class and
 * publishes to KAFKA.
 */
@AllArgsConstructor
@RequiredArgsConstructor
public class CsvDataHandler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvDataHandler.class);
    @NonNull
    SerdesFactory serdesFactory;
    @NonNull
    String fileName;
    @NonNull
    Class classNme;

    Topic topic;
    private Producer<String, TradeRecon> producer;

    @Override
    public void run() {
        LOGGER.info("CSV DATA HANDLER START!!!!!");
        long start = System.currentTimeMillis();
        String fileLocation = PropertiesProvider.GLOBAL_PROPERTY.getProperty("uploadFileLocation");
        Objects.requireNonNull(fileLocation, "Input transaction file location should be provided in properties");

        fileName = fileLocation + "" + fileName;
        Iterator<Object> iterator;
        try {
            topic = Topic.CSV_TRANSACTIONS;
            this.producer = createProducer(serdesFactory);

            LOGGER.info("Starting to read file :" + fileName);
            /*
             Reading the CSV file.
             */
            try (BufferedReader reader = FileReadUtility.sftpService(fileName)) {

                iterator = new CsvToBeanBuilder<>(reader)
                        .withType(CsvTransaction.class)
                        .withSeparator(',')
                        .withIgnoreLeadingWhiteSpace(true)
                        .withIgnoreEmptyLine(true)
                        .build().iterator();

                for (Iterator<Object> it = iterator; it.hasNext(); ) {
                    CsvTransaction csv = (CsvTransaction) it.next();
                    // LOGGER.info("DATA PRODUCE FROM CSV :" + csv);

                    produce(csv);

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            long stop = System.currentTimeMillis();
            LOGGER.info("Start process:" + start);
            LOGGER.info("stop complete process:" + stop);
            LOGGER.info("Total Time taken to complete process:" + (stop - start));

        } catch (IOException | IllegalStateException e) {
            LOGGER.error("Exception in CsvPublisher: {0}", e);
        } finally {
            producer.close();
        }
    }

    /**
     * Creates a KAFKA producer instance
     *
     * @param serdesFactory - SerdesFactory
     * @return Producer<String, Class>
     */
    public Producer<String, TradeRecon> createProducer(@NonNull SerdesFactory serdesFactory) {
        return new KafkaProducer<>(
                PropertiesProvider.KAFKA_PRODUCER.getProperties(), new StringSerializer(),
                serdesFactory.createAvroSerde(TradeRecon.class).serializer());
    }

    /**
     * Publishes each CSV record into KAFKA with Transaction number as key
     *
     * @param value - CsvTransaction
     */
    protected void produce(@NonNull CsvTransaction value) throws ParseException {
       var tradeReconMessage = new TradeRecon();
//        tradeReconMessage.setTransactionNo(value.getTransactionNo());
//
//        tradeReconMessage.setAccountGroupId(value.getAccountGroupId());
//        tradeReconMessage.setVenueId(value.getVenueId());
//        tradeReconMessage.setSttlNo(value.getSttlNo());
//        tradeReconMessage.setLocationId(value.getLocationId());
//        tradeReconMessage.setVenueScripId(value.getVenueScripId());
//        tradeReconMessage.setInstrId(value.getInstrId());
//        tradeReconMessage.setBuySell(value.getBuySell());
//        tradeReconMessage.setQty(value.getQty());
//        tradeReconMessage.setRate(value.getRate());
//        tradeReconMessage.setOrderNo(value.getOrderNo());
//        tradeReconMessage.setContractNoteNo(value.getContractNoteNo());
//        tradeReconMessage.setModifyCount(value.getModifyCount());
//        tradeReconMessage.setTransactionType(value.getTransactionType());
//        tradeReconMessage.setOrderType(value.getOrderType());
//        tradeReconMessage.setTradedCode(value.getTradeCode());
//        tradeReconMessage.setChannel(value.getChannel());
//        tradeReconMessage.setEUser(value.getEUser());
//        tradeReconMessage.setManualEntry(value.getManualEntry());
//        tradeReconMessage.setInstitutionId(value.getInstitutionId());
//        tradeReconMessage.setRemarks(value.getRemarks());
//        tradeReconMessage.setTradeStatus(value.getTradeStatus());
//        tradeReconMessage.setCurrencyCode(value.getCurrencyCode());
//        tradeReconMessage.setAlive(value.getAlive());
//
//
//        tradeReconMessage.setTranDate(value.getTranDate());
//
//        tradeReconMessage.setTradeTime(value.getTradeTime());
//
//        tradeReconMessage.setOrderTime(value.getOrderTime());
//
//        tradeReconMessage.setLastUpdatedOn(value.getLastUpdateOn());

        var key = tradeReconMessage.getTransactionNo();
        tradeReconMessage.setTransactionNo(key);
        var partition = Math.abs(tradeReconMessage.getTransactionNo().hashCode()) % topic.getNumPartitions();
        var record = new ProducerRecord<>(topic.getName(), partition, key + "", tradeReconMessage);
        producer.send(record, (recordMetadata, exception) -> {
            if (exception != null) {
                LOGGER.error("Unable to send record to topic ", exception);
            }
        });

    }
}