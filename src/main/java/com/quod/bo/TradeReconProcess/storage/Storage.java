package com.quod.bo.TradeReconProcess.storage;


import com.quod.bo.TradeReconProcess.model.*;

import java.sql.SQLException;

public interface Storage {

    void persistTransaction(TradeReconKey key, TradeRecon transaction) throws SQLException;
    void processorReport(ProcessorReport transaction) throws SQLException;
}
