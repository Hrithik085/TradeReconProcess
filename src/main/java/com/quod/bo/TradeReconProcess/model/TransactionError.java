package com.quod.bo.TradeReconProcess.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionError {

    private TradeRecon transaction;

    private String error;

}
