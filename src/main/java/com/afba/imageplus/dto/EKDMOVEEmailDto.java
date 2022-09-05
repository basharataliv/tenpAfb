package com.afba.imageplus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EKDMOVEEmailDto {
    private String caseId;
    private String policyId;
    private String queue;

}
