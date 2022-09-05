package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.EMSIUND;
import com.afba.imageplus.repository.sqlserver.EMSIUNDRepository;
import com.afba.imageplus.service.EMSIUNDService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EMSIUNDServiceImpl extends BaseServiceImpl<EMSIUND, Long> implements EMSIUNDService {

    private final EMSIUNDRepository emsiUndRepository;

    protected EMSIUNDServiceImpl(EMSIUNDRepository emsiUndRepository) {
        super(emsiUndRepository);
        this.emsiUndRepository = emsiUndRepository;
    }

    @Override
    protected Long getNewId(EMSIUND entity) {
        return entity.getRecordKey();
    }

    public Optional<EMSIUND> getByPolicyAndProcessFlag(String policyId, String processFlag) {

        return emsiUndRepository.findFirstByPolicyIdAndProcessFlagOrderByCreatedDateDescCreatedTimeDesc(policyId,
                processFlag);
    }
}
