package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author 17603
 * @date 23-06-2022
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VVenueAccountGroupKey implements IsKey {

    @JsonProperty(value = "VENUEACTGRPNAME", required = true)
    private String venueActGrpName;

    @JsonProperty(value = "VENUEID")
    private String venueId;

}
