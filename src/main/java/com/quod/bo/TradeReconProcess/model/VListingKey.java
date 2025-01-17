package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author 17603
 * @date 01-07-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class VListingKey implements IsKey {
    @JsonProperty(value = "VENUEID")
    private String venueId;
    @JsonProperty(value = "SECURITYID")
    private String securityId;
}
