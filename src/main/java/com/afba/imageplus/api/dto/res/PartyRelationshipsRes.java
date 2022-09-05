package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyRelationshipsRes {
    @JsonProperty("NameID")
    private Integer nameID;
    @JsonProperty("RelateCode")
    private String relateCode;
    @JsonProperty("IdentifyingAlpha")
    private String identifyingAlpha;
    @JsonProperty("BenefitSeqNumber")
    private Integer benefitSeqNumber;
    @JsonProperty("ContactCaseNum")
    private String contactCaseNum;
    @JsonProperty("ContactPayorId")
    private Integer contactPayorId;
    @JsonProperty("SubTypeCode")
    private String subTypeCode;
    @JsonProperty("PctOfInterest")
    private Double pctOfInterest;
    @JsonProperty("AMTOfInterest")
    private Double aMTOfInterest;
    @JsonProperty("CompanyCode")
    private String companyCode;
    @JsonProperty("PolicyNumber")
    private String policyNumber;
    @JsonProperty("ContractCode")
    private String contractCode;
    @JsonProperty("ContractDate")
    private Integer contractDate;
    @JsonProperty("LineOfBusiness")
    private String lineOfBusiness;
    @JsonProperty("ProductCode")
    private String productCode;
    @JsonProperty("TaxQualifyCode")
    private String taxQualifyCode;
    @JsonProperty("IssueState")
    private String issueState;
    @JsonProperty("IssueDate")
    private Integer issueDate;
    @JsonProperty("BillingCode")
    private String billingCode;
    @JsonProperty("BillingForm")
    private String billingForm;
    @JsonProperty("BillingMode")
    private String billingMode;
    @JsonProperty("ModePremium")
    private Double modePremium;
    @JsonProperty("PaidToDate")
    private Integer paidToDate;
    @JsonProperty("FaceAmount")
    private Double faceAmount;
    @JsonProperty("PolicySpecialMode")
    private String policySpecialMode;
    @JsonProperty("AgentName")
    private String agentName;
}