package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaidUp {
    @JsonProperty("PuTypeCode")
    private String puTypeCode;
    @JsonProperty("PuIntegerPdToDate")
    private Integer puIntegerPdToDate;
    @JsonProperty("PuAccruInteger")
    private Double puAccruInteger;
    @JsonProperty("PuAccruFaceAmt")
    private Double puAccruFaceAmt;
    @JsonProperty("PuPremiumsPaid")
    private Double puPremiumsPaid;
    @JsonProperty("PuDivCredited")
    private Double puDivCredited;
    @JsonProperty("PuTaxBasis")
    private Double puTaxBasis;
    @JsonProperty("PuSpecialFee")
    private String puSpecialFee;
    @JsonProperty("PuWaiverFee")
    private String puWaiverFee;
    @JsonProperty("PuWaiverCalc")
    private String puWaiverCalc;
    @JsonProperty("PuBenefitLock")
    private Integer puBenefitLock;
}
