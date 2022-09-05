package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.DOCTEMP;
import com.afba.imageplus.repository.sqlserver.DOCTEMPRepository;
import com.afba.imageplus.service.DOCTEMPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DOCTEMPServiceImpl extends BaseServiceImpl<DOCTEMP, String> implements DOCTEMPService {
    private final DOCTEMPRepository dOCTEMPRepository;

    @Autowired
    protected DOCTEMPServiceImpl(DOCTEMPRepository dOCTEMPRepository) {
        super(dOCTEMPRepository);
        this.dOCTEMPRepository = dOCTEMPRepository;
    }

    @Override
    protected String getNewId(DOCTEMP entity) {
        return entity.getDocType();
    }

}
