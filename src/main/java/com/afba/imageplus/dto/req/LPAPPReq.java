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
public class LPAPPReq {

    @Size(min = 9, max = 9,message = "policyId length can only be 9")
    private String policyId;

    private String psn1Ssn;

    private String psn1Type;

    private String psn1FirstName;

    private String psn1MiddleName;

    private String psn1LastName;

    private String amountPaid;

    private String cov1Plan;
}
