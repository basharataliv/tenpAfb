package com.afba.imageplus.api.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBenefitDetailsReq {
    @JsonProperty("CompanyCode")
    private String companyCode;
    @JsonProperty("PolicyNumber")
    private String policyNumber;
    @JsonProperty("BenefitSequence")
    private Integer benefitSequence;
    @JsonProperty("BenefitType")
    private String benefitType;
    @JsonProperty("GUID")
    private String gUId;
    @JsonProperty("UserType")
    private String userType;
    @JsonProperty("CoderID")
    private String coderId;

}
