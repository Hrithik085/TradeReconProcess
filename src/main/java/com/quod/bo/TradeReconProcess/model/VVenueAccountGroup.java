package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 17603
 * @date 22-06-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VVenueAccountGroup {
    @JsonProperty(value = "INSTITUTIONID", required = true)
    private Long institutionId;
    @JsonProperty(value = "ACCOUNTGROUPID", required = true)
    private String accountGroupId;
    @JsonProperty(value = "VENUEID", required = true)
    private String venueId;
    @JsonProperty(value = "VENUEACTGRPNAME", required = true)
    private String venueActGrpName;
    @JsonProperty(value = "STATUS", required = true)
    private String status;
    @JsonProperty(value = "ALIVE", required = true)
    private String alive;
    @JsonProperty(value = "LASTUPDATEDON", required = true)
    private LocalDateTime lastUpdatedDate;
    @JsonProperty(value = "LOCATIONID")
    private Long locationId;
}

