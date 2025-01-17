package com.quod.bo.TradeReconProcess;


import com.quod.bo.TradeReconProcess.broker.topics.TopicsCreator;
import com.quod.bo.TradeReconProcess.broker.topics.TopicsProviderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

@SpringBootApplication
@Slf4j
public class TradeReconProcessApplication {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(com.quod.bo.TradeReconProcess.TradeReconProcessApplication.class, args);
    }

}

