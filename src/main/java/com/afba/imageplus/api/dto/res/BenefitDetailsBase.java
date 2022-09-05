package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenefitDetailsBase {
    @JsonProperty("BaThirdPrtyFlag")
    private String baThirdPrtyFlag;
    @JsonProperty("BaOrTotInfFlag")
    private String baOrTotInfFlag;
    @JsonProperty("BaTamraPrem")
    private Double baTamraPrem;
    @JsonProperty("BaTamraFlag")
    private String baTamraFlag;
    @JsonProperty("BaOrDivIntegra")
    private String baOrDivIntegra;
    @JsonProperty("BaOrVanishFlag")
    private String baOrVanishFlag;
    @JsonProperty("BaOrPuaSeq")
    private Double baOrPuaSeq;
    @JsonProperty("BaOrSpecialFee")
    private String baOrSpecialFee;
    @JsonProperty("BaOrWaiverFee")
    private String baOrWaiverFee;
    @JsonProperty("BaOrWaiverCalc")
    private String baOrWaiverCalc;
    @JsonProperty("BaOrBenefitLock")
    private Double baOrBenefitLock;
    @JsonProperty("BaOrTamraTrmnt")
    private String baOrTamraTrmnt;
    @JsonProperty("BaOrCovLvlDiv")
    private String baOrCovLvlDiv;
}
