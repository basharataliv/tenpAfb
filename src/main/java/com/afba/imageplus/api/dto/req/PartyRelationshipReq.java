package com.afba.imageplus.api.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyRelationshipReq {
    @JsonProperty("Name_ID")
    private int name_ID;
    @JsonProperty("GUID")
    private String gUID;
    @JsonProperty("UserType")
    private String userType;
    @JsonProperty("CoderID")
    private String coderID;
}
