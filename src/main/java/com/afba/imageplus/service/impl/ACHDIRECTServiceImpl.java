package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.ACHDIRECT;
import com.afba.imageplus.repository.sqlserver.ACHDIRECTRepository;
import com.afba.imageplus.service.ACHDIRECTService;
import org.springframework.stereotype.Service;

@Service
public class ACHDIRECTServiceImpl extends BaseServiceImpl<ACHDIRECT,Long>  implements ACHDIRECTService {
    protected ACHDIRECTServiceImpl(ACHDIRECTRepository repository) {
        super(repository);
    }

    @Override
    protected Long getNewId(ACHDIRECT entity) {
        return entity.getRoutingNumber();
    }
}
