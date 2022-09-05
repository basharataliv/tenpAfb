package com.afba.imageplus.api.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetNameParametersReq {
    @JsonProperty("NameID")
    private int nameId;
    @JsonProperty("ParmDefinitionID")
    private int parmDefinitionId;
    @JsonProperty("GUID")
    private String gUId;
    @JsonProperty("UserType")
    private String userType;
    @JsonProperty("CoderID")
    private String coderId;

}
