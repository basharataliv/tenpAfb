package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coverage {
    @JsonProperty("CoverageCode")
    private String coverageCode;
    @JsonProperty("CoverageDesc")
    private String coverageDesc;
}
