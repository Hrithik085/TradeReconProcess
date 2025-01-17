package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VListing {
    @JsonProperty("VENUEID")
    private String venueID;
    @JsonProperty("SECURITYID")
    private String securityID;
    @JsonProperty("DEFAULTMDSYMBOL")
    private String defaultMdSymbol;
    @JsonProperty("INSTRSYMBOL")
    private String instrSymbol;
    @JsonProperty("LISTINGID")
    private Long listingID;
    @JsonProperty("SYMBOL")
    private String symbol;
    @JsonProperty("LOOKUPSYMBOL")
    private String lookUpSymbol;
    @JsonProperty("LASTUPDATEDTIME")
    private String lastUpdateTime;
    @JsonProperty("INSTRID")
    private String instrID;
    @JsonProperty("INSTRTYPE")
    private String instrType;

}
