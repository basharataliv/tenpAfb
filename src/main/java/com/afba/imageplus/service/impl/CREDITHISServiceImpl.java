package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.CREDITHIS;
import com.afba.imageplus.model.sqlserver.id.CREDITHISKey;
import com.afba.imageplus.repository.sqlserver.CREDITHISRepository;
import com.afba.imageplus.service.CREDITHISService;
import org.springframework.stereotype.Service;

@Service
public class CREDITHISServiceImpl extends BaseServiceImpl<CREDITHIS, CREDITHISKey> implements CREDITHISService {

    protected CREDITHISServiceImpl(CREDITHISRepository repository) {
        super(repository);
    }

    @Override
    protected CREDITHISKey getNewId(CREDITHIS entity) {
        return new CREDITHISKey(entity.getSsn(),entity.getPolicyId());
    }
}
