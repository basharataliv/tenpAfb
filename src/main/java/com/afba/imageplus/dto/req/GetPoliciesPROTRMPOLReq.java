package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPoliciesPROTRMPOLReq {

    private String policyId;

    @Size(min = 9, max = 9, message = "Length of ssn must be 9")
    private String ssn;

}
