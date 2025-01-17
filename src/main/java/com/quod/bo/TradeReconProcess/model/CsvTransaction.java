package com.quod.bo.TradeReconProcess.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author - tra865
 * @date - 02-05-2022
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvTransaction {

    @CsvBindByName(column = "Date")
    private String tranDate;
    @CsvBindByName(column = "Venue")
    private String venueId;
    @CsvBindByName(column = "ISIN")
    private String isin;
    @CsvBindByName(column = "Side")
    private String buySell;
    @CsvBindByName(column = "Quantity")
    private Long qty;
    @CsvBindByName(column = "Rate")
    private BigDecimal rate;
    @CsvBindByName(column = "Order No")
    private String orderNo;
    @CsvBindByName(column = "Trade No")
    private String tradeNo;
    @CsvBindByName(column = "Trade Time")
    private String tradeTime;
    @CsvBindByName(column = "Trade Code")
    private String tradeCode;
    @CsvBindByName(column = "Currency")
    private String currencyCode;
    @CsvBindByName(column = "Venue Account Id")
    private String venueAccId;
    @CsvBindByName(column = "Security Id")
    private String securityId;
    private String source;
    private Integer totalRow;
    private Integer slNO;

}








