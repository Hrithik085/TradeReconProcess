package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author 17603
 * @date 23-06-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VVenue {
    @JsonProperty(value = "VENUEID", required = true)
    private String venueId;
    @JsonProperty(value = "VENUENAME", required = true)
    private String venueName;
    @JsonProperty(value = "LASTUPDATEDON", required = true)
    private Timestamp lastUpdatedOn;

}
