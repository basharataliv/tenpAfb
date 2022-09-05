package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.BAEDHST;
import com.afba.imageplus.repository.sqlserver.BAEDHSTRepository;
import com.afba.imageplus.service.BAEDHSTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BAEDHSTServiceImpl  extends BaseServiceImpl<BAEDHST, String> implements BAEDHSTService {
    @Autowired
    public BAEDHSTServiceImpl(BAEDHSTRepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(BAEDHST entity) {
        return null;
    }
}
