package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.BPAYRC;
import com.afba.imageplus.repository.sqlserver.BPAYRCRepository;
import com.afba.imageplus.service.BPAYRCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BPAYRCServiceImpl extends BaseServiceImpl<BPAYRC, String> implements BPAYRCService {
    @Autowired
    protected BPAYRCServiceImpl(BPAYRCRepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(BPAYRC entity) {
        return entity.getRc();
    }
}
