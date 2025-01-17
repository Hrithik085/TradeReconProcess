package com.quod.bo.TradeReconProcess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author - tra865
 * @date - 28-04-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessRequest {
    private String fileName;
    private String venueId;
    private LocalDate uploadDate;
}
