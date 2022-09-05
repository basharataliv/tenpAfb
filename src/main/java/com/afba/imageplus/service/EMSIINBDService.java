package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EMSIINBND;

import java.util.List;

public interface EMSIINBDService extends BaseService<EMSIINBND, Integer> {
    List<String> getDistinctPoliciesByProcessFlagFalse();

    List<EMSIINBND> getByPolicyId(String policyId);

    Boolean performValidationForEmsiInbndRecords(EMSIINBND emsiInbnd);
}
