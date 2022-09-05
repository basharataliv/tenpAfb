package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnuityRider {
    @JsonProperty("ArBillingMode")
    private String arBillingMode;
    @JsonProperty("ArBillingForm")
    private String arBillingForm;
    @JsonProperty("ArBilledToDate")
    private Integer arBilledToDate;
    @JsonProperty("ArPaidToDate")
    private Integer arPaidToDate;
    @JsonProperty("ArActBillDate")
    private Integer arActBillDate;
    @JsonProperty("ArBillableAmount")
    private Double arBillableAmount;
    @JsonProperty("ArSurrenderLoad")
    private Double arSurrenderLoad;
    @JsonProperty("ArCurrAccumCash")
    private Double arCurrAccumCash;
    @JsonProperty("ArLastValDate")
    private Integer arLastValDate;
    @JsonProperty("ArOther")
    private String arOther;
    @JsonProperty("ArVarCovrFlag")
    private String arVarCovrFlag;
    @JsonProperty("ArWithProcDate")
    private Integer arWithProcDate;
    @JsonProperty("ArFvCreatnFlag")
    private String arFvCreatnFlag;
    @JsonProperty("ArPremDepRque")
    private String arPremDepRque;
    @JsonProperty("ArIndexedCovrFl")
    private String arIndexedCovrFl;
    @JsonProperty("ArWaiverFee")
    private String arWaiverFee;
    @JsonProperty("ArWaiverCalc")
    private String arWaiverCalc;
    @JsonProperty("ArBenefitLock")
    private Integer arBenefitLock;
}
