package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.TRNIDPOLR;
import com.afba.imageplus.model.sqlserver.id.TRNIDPOLRKey;
import com.afba.imageplus.repository.sqlserver.TRNIDPOLRRepository;
import com.afba.imageplus.service.TRNIDPOLRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TRNIDPOLRServiceImpl extends BaseServiceImpl<TRNIDPOLR, TRNIDPOLRKey>
        implements TRNIDPOLRService {
    private final TRNIDPOLRRepository trnidpolrRepository;

    @Autowired
    public TRNIDPOLRServiceImpl(TRNIDPOLRRepository repository) {
        super(repository);
        this.trnidpolrRepository = repository;
    }

    @Override
    protected TRNIDPOLRKey getNewId(TRNIDPOLR entity) {
        return new TRNIDPOLRKey(entity.getTpTransactionId(), entity.getTpPolicyId());
    }

    @Override
    public List<TRNIDPOLR> findByTransitionId(String transitionId) {
        return trnidpolrRepository.findByTpTransactionId(transitionId);
    }
}
