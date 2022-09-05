package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetServiceAgentDetailsResultRes {
    @JsonProperty("AgentNumber")
    private String agentNumber;
    @JsonProperty("Agency")
    private String agency;
    @JsonProperty("CommissionablePercent")
    private Double commissionablePercent;
    @JsonProperty("DivisionCode")
    private String divisionCode;
    @JsonProperty("Level")
    private String level;
    @JsonProperty("Market")
    private String market;
    @JsonProperty("Region")
    private String region;
    @JsonProperty("StatementAgent")
    private String statementAgent;
    @JsonProperty("StatusCode")
    private String statusCode;
    @JsonProperty("AgentName")
    private String agentName;
    @JsonProperty("GUID")
    private String gUID;
    @JsonProperty("ReturnCode")
    private int returnCode;
    @JsonProperty("Message")
    private Object message;
}
