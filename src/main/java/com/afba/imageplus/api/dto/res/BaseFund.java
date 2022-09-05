package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseFund {
    @JsonProperty("BfNonForfeiture")
    private String bfNonForfeiture;
    @JsonProperty("BfSpecifiedAmt")
    private Double bfSpecifiedAmt;
    @JsonProperty("BfCurrentDb")
    private Double bfCurrentDb;
    @JsonProperty("BfDbOption")
    private String bfDbOption;
    @JsonProperty("BfCurrSurrLoad")
    private Double bfCurrSurrLoad;
    @JsonProperty("BfMinimumPremium")
    private Double bfMinimumPremium;
    @JsonProperty("BfTarget")
    private Double bfTarget;
    @JsonProperty("BfTamraAmount")
    private Double bfTamraAmount;
    @JsonProperty("BfGuidelinLvlPr")
    private Double bfGuidelinLvlPr;
    @JsonProperty("BfGuidelinSngPr")
    private Double bfGuidelinSngPr;
    @JsonProperty("BfLastValDate")
    private Integer bfLastValDate;
    @JsonProperty("BfDateNegative")
    private Integer bfDateNegative;
    @JsonProperty("BfThirdPrtyFlag")
    private String bfThirdPrtyFlag;
    @JsonProperty("BfTotInfFlag")
    private String bfTotInfFlag;
    @JsonProperty("BfVarCovrFlag")
    private String bfVarCovrFlag;
    @JsonProperty("BfWithProcDate")
    private Integer bfWithProcDate;
    @JsonProperty("BfFvCreatnFlag")
    private String bfFvCreatnFlag;
    @JsonProperty("BfPremDepRque")
    private String bfPremDepRque;
    @JsonProperty("BfRothIraCbFlg")
    private String bfRothIraCbFlg;
    @JsonProperty("BfBaseTargetExp")
    private Double bfBaseTargetExp;
    @JsonProperty("BfWaiverFee")
    private String bfWaiverFee;
    @JsonProperty("BfWaiverCalc")
    private String bfWaiverCalc;
    @JsonProperty("BfBenefitLock")
    private Integer bfBenefitLock;
    @JsonProperty("BfIndexedCovrFl")
    private String bfIndexedCovrFl;
    @JsonProperty("BfTreasryCvrFlg")
    private String bfTreasryCvrFlg;
    @JsonProperty("BfHybridCovrFlg")
    private String bfHybridCovrFlg;
}
