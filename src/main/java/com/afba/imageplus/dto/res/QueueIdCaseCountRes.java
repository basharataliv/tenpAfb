package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueIdCaseCountRes {
    private String queueId;
    private String queueDescription;
    private  String count;
    private String queueType;
}
