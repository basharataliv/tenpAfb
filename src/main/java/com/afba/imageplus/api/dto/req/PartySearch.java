package com.afba.imageplus.api.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartySearch {
    @JsonProperty("Search_Indicator")
    private String search_Indicator;
    @JsonProperty("Function")
    private String function;
    @JsonProperty("SSN")
    private String sSN;
    @JsonProperty("GUID")
    private String gUID;
    @JsonProperty("UserType")
    private String userType;
    @JsonProperty("CoderID")
    private String coderID;
}