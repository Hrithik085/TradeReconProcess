package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author 17603
 * @date 03-08-2022
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Settlement {
    @JsonProperty(value = "VENUEID")
    private String venueId;
    @JsonProperty(value = "STTLNO")
    private Long sttlNo;
    @JsonProperty(value = "FROMDATE")
    private String fromDate;
    @JsonProperty(value = "TODATE")
    private String toDate;
    @JsonProperty(value = "PAYINDATE")
    private String payInDate;
    @JsonProperty(value = "PAYOUTDATE")
    private String payOutDate;
    @JsonProperty(value = "AUCTIONDATE")
    private String auctionDate;
    @JsonProperty(value = "TRANSACTIONARCHIVED")
    private String transactionArchived;
    @JsonProperty(value = "EUSER")
    private String eUser;
    @JsonProperty(value = "LASTUPDATEDON")
    private String lastUpdatedOn;
    @JsonProperty(value = "ALIVE")
    private String alive;
}










