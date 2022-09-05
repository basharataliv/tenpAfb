package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.DDCREDIT;
import com.afba.imageplus.repository.sqlserver.DDCREDITRepository;
import com.afba.imageplus.service.DDCREDITService;
import org.springframework.stereotype.Service;

@Service
public class DDCREDITServiceImpl extends BaseServiceImpl<DDCREDIT, String> implements DDCREDITService {

    protected DDCREDITServiceImpl(DDCREDITRepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(DDCREDIT entity) {
        return entity.getTransactionId();
    }
}
