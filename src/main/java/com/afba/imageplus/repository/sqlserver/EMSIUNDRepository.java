package com.afba.imageplus.repository.sqlserver;

import com.afba.imageplus.model.sqlserver.EMSIUND;
import com.afba.imageplus.repository.BaseRepository;

import java.util.Optional;

public interface EMSIUNDRepository extends BaseRepository<EMSIUND, Long> {

    Optional<EMSIUND> findFirstByPolicyIdAndProcessFlagOrderByCreatedDateDescCreatedTimeDesc(String policyId,
            String processFlag);
}
