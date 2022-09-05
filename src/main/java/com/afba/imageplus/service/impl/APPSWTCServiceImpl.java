package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.APPSWTC;
import com.afba.imageplus.model.sqlserver.id.APPSWTCKey;
import com.afba.imageplus.repository.sqlserver.APPSWTCRepository;
import com.afba.imageplus.service.APPSWTCService;
import org.springframework.stereotype.Service;

@Service
public class APPSWTCServiceImpl extends BaseServiceImpl<APPSWTC, APPSWTCKey> implements APPSWTCService {

    protected APPSWTCServiceImpl(APPSWTCRepository repository) {
        super(repository);
    }

    @Override
    protected APPSWTCKey getNewId(APPSWTC entity) {
        return new APPSWTCKey(entity.getAge(), entity.getMonth(), entity.getSex());
    }
}
