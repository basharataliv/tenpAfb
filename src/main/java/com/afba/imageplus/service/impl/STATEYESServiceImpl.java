package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.STATEYES;
import com.afba.imageplus.model.sqlserver.id.STATEEYESSysTemSysStateKey;
import com.afba.imageplus.repository.sqlserver.STATEYESRepository;
import com.afba.imageplus.service.STATEYESService;
import org.springframework.stereotype.Service;

@Service
public class STATEYESServiceImpl extends BaseServiceImpl<STATEYES, STATEEYESSysTemSysStateKey> implements STATEYESService {

    private final STATEYESRepository stateyesRepository;

    protected STATEYESServiceImpl(STATEYESRepository repository) {
        super(repository);
        this.stateyesRepository = repository;
    }

    @Override
    protected STATEEYESSysTemSysStateKey getNewId(STATEYES entity) {
        return new STATEEYESSysTemSysStateKey(entity.getSystemTemplate(), entity.getSystemState());
    }

}
