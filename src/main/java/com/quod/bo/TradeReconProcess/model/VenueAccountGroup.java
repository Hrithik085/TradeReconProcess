package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author - tra865
 * @date - 27-04-2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueAccountGroup {
    @JsonProperty(value = "venueId")
    private String venueId;

    @JsonProperty(value = "accountGroupId")
    private String accountGroupId;

    @JsonProperty(value = "venueActGrpName")
    private String venueActGrpName;

    @JsonProperty(value = "venueClientActGrpName")
    private String venueClientActGrpName;

    @JsonProperty(value = "maxCommissionValue")
    private Long maxCommissionValue;

    @JsonProperty(value = "maxCommType")
    private String maxCommType;

    @JsonProperty(value = "defaultRouteId")
    private Long defaultRouteId;

    @JsonProperty(value = "pxPrecision")
    private Long pxPrecision;

    @JsonProperty(value = "alive")
    private Boolean alive;

    @JsonProperty(value = "routingParamGroupId")
    private Long routingParamGroupId;

    @JsonProperty(value = "stampFeeExemption")
    private String stampFeeExemption;

    @JsonProperty(value = "levyFeeExemption")
    private String levyFeeExemption;

    @JsonProperty(value = "perTransacFeeExemption")
    private String perTransacFeeExemption;

    @JsonProperty(value = "inLastEnabledEntity")
    private String inLastEnabledEntity;



}
