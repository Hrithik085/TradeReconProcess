package com.quod.bo.TradeReconProcess.service;

import com.quod.bo.TradeReconProcess.broker.topics.Topic;
import com.quod.bo.TradeReconProcess.dataprovider.TransactionCsvDataProvider;
import com.quod.bo.TradeReconProcess.model.CsvTransaction;
import com.quod.bo.TradeReconProcess.serialization.SerdesFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.Signal;

import java.nio.file.Path;
import java.nio.file.Paths;



/**
 * @author 17603
 * @date 16-06-2022
 */
@Service
public class CsvReaderService {

    @Autowired
    SerdesFactory serdesFactory;

    public void start() throws InterruptedException {

        //var serdesFactory = new SerdesFactory();

        Path folder = Paths.get("TradeReconProcess/input_csv/transactions");
        long checkIntervalMs = 1000;
        TransactionCsvDataProvider transactionDataProvider = new TransactionCsvDataProvider(folder,
                checkIntervalMs,
                CsvTransaction.class,
                Topic.CSV_TRANSACTIONS,
                serdesFactory);

        var thread = new Thread(transactionDataProvider::startOnePass);

        thread.start();

        Signal.handle(new Signal("INT"), signal -> thread.interrupt());

        thread.join();
    }
}
