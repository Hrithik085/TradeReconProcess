package com.quod.bo.TradeReconProcess.broker.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quod.bo.TradeReconProcess.model.ProcessRequestResponse;
import com.quod.bo.TradeReconProcess.broker.topics.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


public class ProcessResponseProducer {
    private final static Logger LOG = LoggerFactory.getLogger(ProcessResponseProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;



    public void sendMessage(String response) throws JsonProcessingException {

        kafkaTemplate.send(Topic.PROCESS_RESPONSE.getName(), response);

    }
}

