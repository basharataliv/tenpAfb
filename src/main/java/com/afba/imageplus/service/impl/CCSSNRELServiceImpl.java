package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.CCSSNREL;
import com.afba.imageplus.repository.sqlserver.CCSSNRELRepository;
import com.afba.imageplus.service.CCSSNRELService;
import org.springframework.stereotype.Service;

@Service
public class CCSSNRELServiceImpl extends BaseServiceImpl<CCSSNREL,String> implements CCSSNRELService {
    protected CCSSNRELServiceImpl(CCSSNRELRepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(CCSSNREL entity) {
        return entity.getCsCaseId();
    }
}
