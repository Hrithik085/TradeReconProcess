package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author - tra865
 * @date - 07-05-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TradeRecon {
    @JsonProperty
    private Integer slNO;
    @JsonProperty(value = "TRANSACTIONNO")
    private Long transactionNo;
    @JsonProperty(value = "VENUETRADEID")
    private Long venueTradeID;
    @JsonProperty(value = "VENUEINSTRID")
    private String venueInstrID;
    @JsonProperty(value = "EXECUTIONDATETIME")
    private String excecutionDateTime;
    @JsonProperty(value = "ISIN")
    private String isin;
    @JsonProperty(value = "TRADESTATUS")
    private String tradeStatus;
    @JsonProperty(value = "TRANDATE")
    private String tranDate;
    @JsonProperty(value = "INVESTORACCOUNT")
    private String investorsAccount;
    @JsonProperty(value = "MARKETTYPE")
    private String marketType;
    @JsonProperty(value = "INVESTORNAME")
    private String investorName;
    @JsonProperty(value = "ORDERID")
    private String orderID;
    @JsonProperty(value = "SIDE")
    private String side;
    @JsonProperty(value = "PRICE")
    private BigDecimal price;
    @JsonProperty(value = "QTY")
    private Long qty;
    @JsonProperty(value = "VALUE")
    private BigDecimal value;
    @JsonProperty(value = "UPDATED")
    private String updated;
    @JsonProperty(value = "STTLNO")
    private Long sttlNo;
    @JsonProperty(value = "LISTINGID")
    private Long listingId;
    @JsonProperty(value = "ACCOUNTGROUPID")
    private String accountGroupId;
    @JsonProperty(value = "LOCATIONID")
    private Long locationId;
    @JsonProperty(value = "SLABID")
    private String slabID;
    @JsonProperty(value = "INSTRID")
    private String instrId;
    @JsonProperty(value = "INSTRTYPE")
    private String instrType;
    @JsonProperty(value = "INSTRUMENTNAME")
    private String instrumentName;
    @JsonProperty(value = "INSTITUTIONID")
    private Long institutionId;
    @JsonProperty(value = "VENUEID")
    private String venueId;
    @JsonProperty(value = "LASTUPDATEDON")
    private LocalDateTime lastUpdatedOn;
    @JsonProperty(value = "REMARKS")
    private String remarks;
    @JsonProperty(value = "ALIVE")
    private String alive;
    @JsonProperty(value = "VENUEACTGRPNAME", required = true)
    private String venueActGrpName;

    @JsonProperty(value = "CURRENCYCODE")
    private String currencyCode;
}








