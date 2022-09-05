package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherRider {
    @JsonProperty("OrRiderType")
    private String orRiderType;
    @JsonProperty("OrMthlyDed")
    private Double orMthlyDed;
    @JsonProperty("OrPuaFace")
    private Double orPuaFace;
}
