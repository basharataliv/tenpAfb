package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentHierarchyRes {
    @JsonProperty("CompanyCode")
    public String companyCode;
    @JsonProperty("AgentNumber")
    public String agentNumber;
    @JsonProperty("MarketCode")
    public String marketCode;
    @JsonProperty("NameID")
    public String nameID;
    @JsonProperty("AgentLevel")
    public String agentLevel;
    @JsonProperty("StopDate")
    public String stopDate;
    @JsonProperty("StartDate")
    public String startDate;
    @JsonProperty("PayCode")
    public String payCode;
    @JsonProperty("DealCode")
    public String dealCode;
    @JsonProperty("NameFormatCode")
    public String nameFormatCode;
    @JsonProperty("IndividualFirst")
    public String individualFirst;
    @JsonProperty("IndividualLast")
    public String individualLast;
    @JsonProperty("IndividualMiddle")
    public String individualMiddle;
    @JsonProperty("IndividualPrefix")
    public String individualPrefix;
    @JsonProperty("IndividualSuffix")
    public String individualSuffix;
    @JsonProperty("BusinessPrefix")
    public Object businessPrefix;
    @JsonProperty("NameBusiness")
    public Object nameBusiness;
    @JsonProperty("BusinessSuffix")
    public Object businessSuffix;
    @JsonProperty("HierarchyAgent")
    public String hierarchyAgent;
    @JsonProperty("FinancialAgent")
    public String financialAgent;
    @JsonProperty("AllocatedAgent")
    public String allocatedAgent;
    @JsonProperty("HierarchyAgentLevel")
    public String hierarchyAgentLevel;
}
