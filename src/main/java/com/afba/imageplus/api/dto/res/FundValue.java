package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundValue {
    @JsonProperty("FvFundValueCode")
    private String fvFundValueCode;
    @JsonProperty("FvBasis1")
    private Double fvBasis1;
    @JsonProperty("FvBasis2")
    private Double fvBasis2;
    @JsonProperty("FvIncome1")
    private Double fvIncome1;
    @JsonProperty("FvIncome2")
    private Double fvIncome2;
    @JsonProperty("FvBalance1")
    private Double fvBalance1;
    @JsonProperty("FvBalance2")
    private Double fvBalance2;
    @JsonProperty("FvTotLoanBasis")
    private Double fvTotLoanBasis;
    @JsonProperty("FvTotalLoanInc")
    private Double fvTotalLoanInc;
    @JsonProperty("FvTotalLoanBal")
    private Double fvTotalLoanBal;
    @JsonProperty("FvGuarRate")
    private Double fvGuarRate;
    @JsonProperty("FvGuarDeposits")
    private Double fvGuarDeposits;
    @JsonProperty("FvDepLoads")
    private Double fvDepLoads;
    @JsonProperty("FvGrWithdrawals")
    private Double fvGrWithdrawals;
    @JsonProperty("FvWithdrawalLoad")
    private Double fvWithdrawalLoad;
    @JsonProperty("FvCoi")
    private Double fvCoi;
    @JsonProperty("FvBaseDed")
    private Double fvBaseDed;
    @JsonProperty("FvProcessLoads")
    private Double fvProcessLoads;
    @JsonProperty("FvProductType")
    private String fvProductType;
    @JsonProperty("FvType")
    private String fvType;
    @JsonProperty("FvProcessRule")
    private String fvProcessRule;
    @JsonProperty("FvDepAlloc")
    private String fvDepAlloc;
    @JsonProperty("FvWtdAlloc")
    private String fvWtdAlloc;
    @JsonProperty("FvDedAlloc")
    private String fvDedAlloc;
    @JsonProperty("FvCoiAlloc")
    private String fvCoiAlloc;
    @JsonProperty("FvLonAlloc")
    private String fvLonAlloc;
    @JsonProperty("FvPldAlloc")
    private String fvPldAlloc;
    @JsonProperty("FvLastValDate")
    private Integer fvLastValDate;
    @JsonProperty("FvLandmarkDate")
    private Integer fvLandmarkDate;
    @JsonProperty("FvDepAllocFlag")
    private String fvDepAllocFlag;
    @JsonProperty("FvSpecialFund")
    private String fvSpecialFund;
    @JsonProperty("FvOrderNum")
    private String fvOrderNum;
    @JsonProperty("FvCommClass")
    private String fvCommClass;
    @JsonProperty("FvWaiverFee")
    private String fvWaiverFee;
    @JsonProperty("FvWaiverCalc")
    private String fvWaiverCalc;
    @JsonProperty("FvFillerLock")
    private String fvFillerLock;
}
