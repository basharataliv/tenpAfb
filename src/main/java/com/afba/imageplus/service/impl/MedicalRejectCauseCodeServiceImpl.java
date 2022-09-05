package com.afba.imageplus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afba.imageplus.model.sqlserver.MedicalRejectCauseCode;
import com.afba.imageplus.repository.sqlserver.MedicalRejectCauseCodeRepository;
import com.afba.imageplus.service.MedicalRejectCauseCodeService;

@Service
public class MedicalRejectCauseCodeServiceImpl extends BaseServiceImpl<MedicalRejectCauseCode, String>
        implements MedicalRejectCauseCodeService {
    @Autowired
    protected MedicalRejectCauseCodeServiceImpl(MedicalRejectCauseCodeRepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(MedicalRejectCauseCode entity) {
        return entity.getCauseCode();
    }
}
