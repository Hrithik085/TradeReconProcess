package com.quod.bo.TradeReconProcess.model;

import lombok.*;

/**
 * @author - tra865
 * @date - 06-05-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProcessRequestResponse {
    public String status;
    public String count;
    public String processId;
    public String  fileName;
}
