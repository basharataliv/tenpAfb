package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LPAPPRes {
    private String policyId;

    private String psn1Ssn;

    private String psn1Type;

    private String psn1FirstName;

    private String psn1MiddleName;

    private String psn1LastName;

    private String amountPaid;

    private String cov1Plan;
}
