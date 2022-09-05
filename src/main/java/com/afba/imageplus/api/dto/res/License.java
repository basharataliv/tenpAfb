package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class License {
    @JsonProperty("LicenseNumber")
    private String licenseNumber;
    @JsonProperty("LicenseType")
    private String licenseType;
    @JsonProperty("LOBLicensedToSell")
    private LOBLicensedToSell lOBLicensedToSell;
}
