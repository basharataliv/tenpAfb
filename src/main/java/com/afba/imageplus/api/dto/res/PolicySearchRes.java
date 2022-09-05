package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicySearchRes {
    @JsonProperty("CompanyCode")
    private String companyCode;
    @JsonProperty("PolicyNumber")
    private String policyNumber;
    @JsonProperty("LineOfBusiness")
    private String lineOfBusiness;
    @JsonProperty("PolicyOwner")
    private String policyOwner;
    @JsonProperty("ContractCode")
    private String contractCode;
    @JsonProperty("BillingCode")
    private String billingCode;
    @JsonProperty("IssueDate")
    private String issueDate;
    @JsonProperty("ActualBillDate")
    private String actualBillDate;
}
