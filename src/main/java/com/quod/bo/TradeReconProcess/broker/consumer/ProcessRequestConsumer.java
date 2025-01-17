package com.quod.bo.TradeReconProcess.broker.consumer;


import com.quod.bo.TradeReconProcess.broker.topics.Topic;
import com.quod.bo.TradeReconProcess.model.ProcessRequest;
import com.quod.bo.TradeReconProcess.model.ProcessRequestResponse;
import com.quod.bo.TradeReconProcess.properties.PropertiesProvider;
import com.quod.bo.TradeReconProcess.serialization.SerdesFactory;
import com.quod.bo.TradeReconProcess.service.CsvReaderService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author 17603
 * @date 16-06-2022
 */
@Component
@Slf4j
public class ProcessRequestConsumer {
    @Autowired
    CsvReaderService csvReaderService;
    @Autowired
    SerdesFactory serdesFactory;

    //@SendTo("recon-response11")
    @KafkaListener(topics = "trade-recon-request",
            groupId = "${recon.topic.group.id}",
            containerFactory = "reconRequestKafkaListenerContainerFactory")
    public ProcessRequestResponse consume(ProcessRequest request) {
        log.info(String.format("request received -> %s", request));

        ProcessRequestResponse response = new ProcessRequestResponse();

        String message = "Trade Reconciliation Process Started: " + request.getFileName();
        response.setStatus(message);
        response.setFileName(request.getFileName());
        response.setProcessId("1");

        //String value= objectMapper.writeValueAsString(response);
        //JsonNode jsonValue=objectMapper.readTree(value);
        var producer = createProducer(serdesFactory);
        var record = new ProducerRecord<>(Topic.PROCESS_RESPONSE.getName(), "key_" + UUID.randomUUID(), response);
       // producer.send(record);
        try {
            csvReaderService.start();
            String message1 = "Trade Reconciliation Process Started: " + request.getFileName();
            response.setStatus(message);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
            String errorMessage = request.getFileName() + "  error";
            response.setStatus(errorMessage);
            return response;
        }
    }

    public Producer<String, ProcessRequestResponse> createProducer(@NonNull SerdesFactory serdesFactory) {
        return new KafkaProducer<>(
                PropertiesProvider.KAFKA_PRODUCER.getProperties(), new StringSerializer(),
                serdesFactory.createJsonSerde(ProcessRequestResponse.class).serializer());
    }

}