package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.APPSWTA;
import com.afba.imageplus.repository.sqlserver.APPSWTARepository;
import com.afba.imageplus.service.APPSWTAService;
import org.springframework.stereotype.Service;

@Service
public class APPSWTAServiceImpl extends BaseServiceImpl<APPSWTA, String> implements APPSWTAService {

    protected APPSWTAServiceImpl(APPSWTARepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(APPSWTA entity) {
        return entity.getHeight();
    }
}
