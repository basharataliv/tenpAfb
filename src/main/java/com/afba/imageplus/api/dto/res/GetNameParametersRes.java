package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetNameParametersRes {
    @JsonProperty("GetNameParameters")
    private List<GetNameRes> getNameRes;
    @JsonProperty("GUID")
    private String gUId;
    @JsonProperty("ReturnCode")
    private int returnCode;
    @JsonProperty("Message")
    private String message;
}
