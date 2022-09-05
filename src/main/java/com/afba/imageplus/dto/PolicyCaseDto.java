package com.afba.imageplus.dto;

import com.afba.imageplus.dto.res.CaseResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
public class PolicyCaseDto {

    private String policyId;
    private List<CaseResponse> cases;
}
