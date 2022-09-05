package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.DDCHECK;
import com.afba.imageplus.repository.sqlserver.DDCHECKRepository;
import com.afba.imageplus.service.DDCHECKService;
import org.springframework.stereotype.Service;

@Service
public class DDCHECKServiceImpl extends BaseServiceImpl<DDCHECK, String> implements DDCHECKService {

    protected DDCHECKServiceImpl(DDCHECKRepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(DDCHECK entity) {
        return entity.getTransactionId();
    }

}
