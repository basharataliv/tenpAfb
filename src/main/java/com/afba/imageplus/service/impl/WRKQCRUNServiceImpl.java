package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.WRKQCRUN;
import com.afba.imageplus.repository.sqlserver.WRKQCRUNRepository;
import com.afba.imageplus.service.WRKQCRUNService;
import org.springframework.stereotype.Service;

@Service
public class WRKQCRUNServiceImpl extends BaseServiceImpl<WRKQCRUN, String> implements WRKQCRUNService {

    protected WRKQCRUNServiceImpl(WRKQCRUNRepository wrkQcRunRepository) {
        super(wrkQcRunRepository);
    }

    @Override
    protected String getNewId(WRKQCRUN entity) {
        return null;
    }
}
