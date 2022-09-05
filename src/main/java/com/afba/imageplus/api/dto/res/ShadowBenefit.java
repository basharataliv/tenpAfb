package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShadowBenefit {
    @JsonProperty("SuTypeCode")
    private String suTypeCode;
    @JsonProperty("SuSubtypeCd")
    private String suSubtypeCd;
    @JsonProperty("SuUnitsCtrl")
    private String suUnitsCtrl;
    @JsonProperty("SuDeduction")
    private Double suDeduction;
    @JsonProperty("SuPremiumsPaid")
    private Double suPremiumsPaid;
    @JsonProperty("SuDivCredited")
    private Double suDivCredited;
    @JsonProperty("SuTaxBasis")
    private Double suTaxBasis;
    @JsonProperty("SuCompSubType")
    private String suCompSubType;
    @JsonProperty("SuTotInfFlag")
    private String suTotInfFlag;
    @JsonProperty("SuLPOverride")
    private String suLPOverride;
    @JsonProperty("SuTamraFlag")
    private String suTamraFlag;
    @JsonProperty("SuRowSource")
    private String suRowSource;
    @JsonProperty("SuBundledBenSeq")
    private Integer suBundledBenSeq;
    @JsonProperty("SuJoIntegerSelection")
    private String suJoIntegerSelection;
    @JsonProperty("SuSpecialFee")
    private String suSpecialFee;
    @JsonProperty("SuWaiverFee")
    private String suWaiverFee;
    @JsonProperty("SuWaiverCalc")
    private String suWaiverCalc;
    @JsonProperty("SuBenefitLock")
    private Integer suBenefitLock;
    @JsonProperty("SuTamraTrmnt")
    private String suTamraTrmnt;
}
