package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeReconKey implements IsKey {

    @JsonProperty("TRANSACTIONNO")
    private String transactionNo;

}
