package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.MOVETRAIL;
import com.afba.imageplus.model.sqlserver.id.EKD0315CaseDocumentKey;
import com.afba.imageplus.repository.sqlserver.MOVETRAILRepository;
import com.afba.imageplus.service.MOVETRAILService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MOVETRAILServiceImpl extends BaseServiceImpl<MOVETRAIL, EKD0315CaseDocumentKey>
        implements MOVETRAILService {

    private final MOVETRAILRepository mOVETRAILRepository;

    @Autowired
    protected MOVETRAILServiceImpl(MOVETRAILRepository mOVETRAILRepository) {
        super(mOVETRAILRepository);
        this.mOVETRAILRepository = mOVETRAILRepository;
    }

    @Override
    protected EKD0315CaseDocumentKey getNewId(MOVETRAIL entity) {
        return new EKD0315CaseDocumentKey(entity.getCaseId(), entity.getDocumentId());
    }

}
