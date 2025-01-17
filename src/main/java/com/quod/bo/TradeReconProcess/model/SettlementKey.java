package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @author 17603
 * @date 03-08-2022
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementKey implements IsKey{
    @JsonProperty(value = "VENUEID")
    private String venueId;;
    @JsonProperty(value = "FROMDATE")
    private String fromDate;
}
