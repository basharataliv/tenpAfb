package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Benefit {
    @JsonProperty("BenefitSeq")
    private Integer benefitSeq;
    @JsonProperty("BenefitType")
    private String benefitType;
    @JsonProperty("ParentBenSeq")
    private Integer parentBenSeq;
}
