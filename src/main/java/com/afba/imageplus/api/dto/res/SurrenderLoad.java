package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurrenderLoad {
    @JsonProperty("SlTableCode")
    private String slTableCode;
    @JsonProperty("SlRateUpAge")
    private Integer slRateUpAge;
    @JsonProperty("SlPercent")
    private Double slPercent;
    @JsonProperty("SlFlatAmount")
    private Double slFlatAmount;
    @JsonProperty("SlPctCeaseDate")
    private Integer slPctCeaseDate;
    @JsonProperty("SlFlatCeaseDate")
    private Integer slFlatCeaseDate;
    @JsonProperty("Sl2NdTableCode")
    private String sl2NdTableCode;
    @JsonProperty("Sl2NdRateAgeUp")
    private Integer sl2NdRateAgeUp;
    @JsonProperty("Sl2NdPercent")
    private Double sl2NdPercent;
    @JsonProperty("Sl2NdFlatAmount")
    private Double sl2NdFlatAmount;
    @JsonProperty("Sl2NdPctCsDate")
    private Integer sl2NdPctCsDate;
    @JsonProperty("Sl2NdFltCsDate")
    private Integer sl2NdFltCsDate;
    @JsonProperty("SlPctMthlyDed")
    private Double slPctMthlyDed;
    @JsonProperty("SlFlatMthlyDed")
    private Double slFlatMthlyDed;
    @JsonProperty("SlPremiumsPaid")
    private Double slPremiumsPaid;
    @JsonProperty("SlDivCredited")
    private Double slDivCredited;
    @JsonProperty("SlTaxBasis")
    private Double slTaxBasis;
    @JsonProperty("SlLPOverride")
    private String slLPOverride;
    @JsonProperty("SlWaiverCalc")
    private String slWaiverCalc;
    @JsonProperty("SlBenefitLock")
    private Integer slBenefitLock;
}
