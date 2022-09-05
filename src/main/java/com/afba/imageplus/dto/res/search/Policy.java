package com.afba.imageplus.dto.res.search;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Policy {
    String policyId;
    Integer caseCount;
    List<Case> cases;

    public Policy(String policyId, List<Case> cases) {
        this.policyId = policyId;
        this.cases = cases;
        this.caseCount = cases == null ? 0 : cases.size();
    }
}
