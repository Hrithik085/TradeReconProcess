package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @JsonProperty(value = "TRANSACTIONNO")
    private String transactionNo;

    @JsonProperty(value = "TRANDATE")
    private String tranDate;

    @JsonProperty(value = "VENUEID")
    private String venueId;

    @JsonProperty(value = "STTLNO")
    private Long sttlNo;

    @JsonProperty(value = "ACCOUNTGROUPID")
    private String accountGroupId;

    @JsonProperty(value = "LOCATIONID")
    private Integer locationId;

    @JsonProperty(value = "VENUESCRIPID")
    private String venueScripId;

    @JsonProperty(value = "INSTRID")
    private String instrId;

    @JsonProperty(value = "BUYSELL")
    private String buySell;

    @JsonProperty(value = "QTY")
    private Long qty;

    @JsonProperty(value = "SIGNEDQTY")
    private Integer signedQty;

    @JsonProperty(value = "RATE")
    private Long rate;

    @JsonProperty(value = "ORDERNO")
    private String orderNo;

    @JsonProperty(value = "TRADENO")
    private String tradeNo;

    @JsonProperty(value = "TRADETIME")
    private LocalDateTime tradeTime;

    @JsonProperty(value = "CONTRACTNOTENO")
    private String contractNoteNo;

    @JsonProperty(value = "MODIFYCOUNT")
    private Integer modifyCount;

    @JsonProperty(value = "TRANSACTIONTYPE")
    private String transactionType;

    @JsonProperty(value = "ORDERTYPE")
    private String orderType;

    @JsonProperty(value = "ORDERTIME")
    private LocalDateTime orderTime;

    @JsonProperty(value = "TRADEDCODE")
    private String tradedCode;

    @JsonProperty(value = "CHANNEL")
    private String channel;

    @JsonProperty(value = "ACCOUNTDATE")
    private String accountDate;

    @JsonProperty(value = "LASTUPDATEDON")
    private LocalDateTime lastUpdatedOn;

    @JsonProperty(value = "EUSER")
    private String eUser;

    @JsonProperty(value = "MANUALENTRY")
    private String manualEntry;

    @JsonProperty(value = "INSTITUTIONID")
    private Integer institutionId;

    @JsonProperty(value = "REMARKS")
    private String remarks;

    @JsonProperty(value = "CDSTRADEID")
    private String cdsTradeId;

    @JsonProperty(value = "TRADESTATUS")
    private String tradeStatus;

    @JsonProperty(value = "POOLTRANSACTIONNO")
    private Integer poolTransactionNo;

    @JsonProperty(value = "TOLEVY")
    private Integer toLevy;

    @JsonProperty(value = "BRKSLABID")
    private Long brkSlabId;

    @JsonProperty(value = "DLVQTY")
    private Long dlvQty;

    @JsonProperty(value = "DLVBRKCHARGED")
    private Long dlvBrkCharged;

    @JsonProperty(value = "DLVBRKMINCHARGED")
    private Long dlvBrkMinCharged;

    @JsonProperty(value = "INTRADAYBRKCHARGED")
    private Integer intraDayBrkCharged;

    @JsonProperty(value = "INTRADAYBRKMINCHARGED")
    private Integer intraDayBrkMinCharged;

    @JsonProperty(value = "DLVBRK")
    private Long dlvBrk;

    @JsonProperty(value = "INTRADAYBRK")
    private Integer intraDayBrk;

    @JsonProperty(value = "BRKSLABTYPE")
    private String brkSlabType;

    @JsonProperty(value = "TOTALBRK")
    private Long totalBrk;

    @JsonProperty(value = "CURRENCYCODE")
    private String currencyCode;

    @JsonProperty(value = "CNTRORDERNO")
    private String cntOrderNo;

    @JsonProperty(value = "EXCHANGECLIENTID")
    private String exchangeClientId;

    @JsonProperty(value = "MANUALENTRYLEVY")
    private String manualEntryLevy;

    @JsonProperty(value = "BROKERNAME")
    private String brokerName;

    @JsonProperty(value = "DEALERCODE")
    private String dealerCode;

    @JsonProperty(value = "ACCUMULATIVEINTEREST")
    private String accumulativeInterest;

    @JsonProperty(value = "HOACCOUNTDATE")
    private String hoAccountDate;

    @JsonProperty(value = "PRODUCTID")
    private Long productId;

    @JsonProperty(value = "INTRADAYLEVY")
    private Integer intraDayLevy;

    @JsonProperty(value = "DLVLEVY")
    private Long dlvLevy;

    @JsonProperty(value = "LEVYMINCHARGED")
    private Integer levyMinCharged;

    @JsonProperty(value = "EXGRATEDATE")
    private LocalDate exgrateDate;

    @JsonProperty(value = "VAT")
    private Long vat;

    @JsonProperty(value = "ALIVE")
    private String alive;

}
