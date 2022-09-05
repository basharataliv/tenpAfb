package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAgentLicensingInfoRes {
    @JsonProperty("CompanyCode")
    private String companyCode;
    @JsonProperty("AgentNumber")
    private String agentNumber;
    @JsonProperty("AgntId")
    private Integer agntId;
    @JsonProperty("LicensedState")
    private String licensedState;
    @JsonProperty("LicenseStatus")
    private String licenseStatus;
    @JsonProperty("LicenseReason")
    private String licenseReason;
    @JsonProperty("GrantedDate")
    private String grantedDate;
    @JsonProperty("ExpiresDate")
    private String expiresDate;
    @JsonProperty("ResidentInd")
    private String residentInd;
    @JsonProperty("NasdInd")
    private String nasdInd;
    @JsonProperty("License")
    private License license;
}
