package com.quod.bo.TradeReconProcess.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author 17603
 * @date 11-11-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessorReport {
    @JsonProperty(value = "ALIVE")
    private String alive;
    @JsonProperty(value = "LASTUPDATEDON")
    private String lastUpdatedOn;
    @JsonProperty(value = "PROCESSDATE")
    private String processDate;
    @JsonProperty(value = "PROCESSID")
    private String processID;
    @JsonProperty(value = "PROCESSNAME")
    private String processName;
    @JsonProperty(value = "SOURCEFILE")
    private String sourceFile;
    @JsonProperty(value = "STATUS")
    private String status;
    @JsonProperty(value = "TOTALROWS")
    private Long totalRows;
    @JsonProperty(value = "REMARKS")
    private String remarks;
}

