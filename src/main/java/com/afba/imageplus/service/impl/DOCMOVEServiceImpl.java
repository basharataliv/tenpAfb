package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.DOCMOVE;
import com.afba.imageplus.repository.sqlserver.DOCMOVERepository;
import com.afba.imageplus.service.DOCMOVEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DOCMOVEServiceImpl extends BaseServiceImpl<DOCMOVE, String> implements DOCMOVEService {
    private final DOCMOVERepository dOCMOVERepository;

    @Autowired
    protected DOCMOVEServiceImpl(DOCMOVERepository dOCMOVERepository) {
        super(dOCMOVERepository);
        this.dOCMOVERepository = dOCMOVERepository;
    }

    @Override
    protected String getNewId(DOCMOVE entity) {
        return entity.getDocType();
    }
}
