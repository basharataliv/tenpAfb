package com.afba.imageplus.api.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBenefitSummaryReq {
    @JsonProperty("CompanyCode")
    private String company_Code;
    @JsonProperty("PolicyNumber")
    private String policy_Number;
    @JsonProperty("IsSortingRequired")
    private String isSortingRequired;
    @JsonProperty("GUID")
    private String gUID;
    @JsonProperty("UserType")
    private String userType;
    @JsonProperty("CoderID")
    private String coderID;
}
