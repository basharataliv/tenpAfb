package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetNameRes {
    @JsonProperty("ParmDefinitionID")
    private int parmDefinitionId;
    @JsonProperty("AllowableValueID")
    private int allowableValueId;
    @JsonProperty("ParameterValue")
    private String parameterValue;
    @JsonProperty("ValueDescription")
    private String valueDescription;
}
