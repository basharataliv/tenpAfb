package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentHierarchyBaseRes {
    @JsonProperty("AgentHierarchy")
    public List<AgentHierarchyRes> agentHierarchy;
    public int count;
    @JsonProperty("GUID")
    public String gUID;
    @JsonProperty("ReturnCode")
    public int returnCode;
    @JsonProperty("Message")
    public String message;
}
