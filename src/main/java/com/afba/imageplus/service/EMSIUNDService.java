package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EMSIUND;

import java.util.Optional;

public interface EMSIUNDService extends BaseService<EMSIUND, Long> {
    Optional<EMSIUND> getByPolicyAndProcessFlag(String policyId, String processFlag);
}
