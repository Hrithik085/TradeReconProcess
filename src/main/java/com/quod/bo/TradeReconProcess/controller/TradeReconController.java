//package com.quod.bo.TradeReconProcess.controller;
//
//import com.quod.bo.TradeReconProcess.serialization.SerdesFactory;
//import com.quod.bo.TradeReconProcess.broker.topics.TopicsCreator;
//import com.quod.bo.TradeReconProcess.broker.topics.TopicsProviderImpl;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static java.lang.System.exit;
//
///**
// * @author 17603
// * @date 03-06-2022
// */
////@Component
//@Slf4j
//public class TradeReconController {
//
//    public   void startStreams() {
//        var serdesFactory = new SerdesFactory();
//
//        /*
//         Kafka Topic creation
//         */
//        var topicsCreator = new TopicsCreator(new TopicsProviderImpl());
//        try {
//            topicsCreator.start();
//        } catch (ExecutionException e) {
//            log.error("Topics creation error", e);
//            exit(-1);
//        }
//
//        /*
//        Deploying Kafka Consuemr
//         */
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        executor.submit(new KafkaConsumerClass(serdesFactory));
//        log.info("Stream process fully started ..............");
//    }
//}
