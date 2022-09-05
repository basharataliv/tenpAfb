package com.afba.imageplus.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EKD0105Dto {

    private String caseDescription;
    private String queueId;
    private String policyId;

}
