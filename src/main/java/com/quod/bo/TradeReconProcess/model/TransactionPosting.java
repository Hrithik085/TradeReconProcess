package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author 17636
 * @date 25-10-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPosting {

    @JsonProperty(value = "institutionId")
    private Long institutionId;

    @JsonProperty(value = "tradeDate")
    private String tradeDate;

    @JsonProperty(value = "venueId")
    private String venueId;

    @JsonProperty(value = "eUser")
    private String eUser;

}
