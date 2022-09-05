package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPartyRelationshipsResultRes {
    @JsonProperty("PartyRelationshipsResp")
    private List<PartyRelationshipsRes> partyRelationshipsResp;
    @JsonProperty("GUID")
    private String gUID;
    @JsonProperty("ReturnCode")
    private int returnCode;
    @JsonProperty("Message")
    private Object message;
}
