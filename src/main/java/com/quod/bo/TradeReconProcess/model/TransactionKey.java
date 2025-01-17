package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionKey implements IsKey {

    @JsonProperty("TXN_SOURCE_KEY")
    private UUID fileId;

    @JsonProperty("TXN_SOURCE_ORDER")
    private Integer rowNumber;

}
