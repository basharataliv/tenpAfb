package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LOBLicensedToSell {
    @JsonProperty("LifeInd")
    private String lifeInd;
    @JsonProperty("HealthInd")
    private String healthInd;
    @JsonProperty("AnnuityInd")
    private String annuityInd;
    @JsonProperty("LTC")
    private String lTC;
}
