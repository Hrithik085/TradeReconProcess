package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 17603
 * @date 11-11-2022
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessorReportKey implements IsKey {
    @JsonProperty("PROCESSID")
    private String processID;
}
