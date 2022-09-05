package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.TSACORRHIS;
import com.afba.imageplus.repository.sqlserver.TSACORRHISRepository;
import com.afba.imageplus.service.TSACORRHISSevice;
import org.springframework.stereotype.Service;

@Service
public class TSACORRHISSeviceImpl extends BaseServiceImpl<TSACORRHIS, String> implements TSACORRHISSevice {

    public TSACORRHISSeviceImpl(TSACORRHISRepository tSACORRHISRepository) {
        super(tSACORRHISRepository);
    }

    @Override
    protected String getNewId(TSACORRHIS entity) {
        return entity.getDocumentId();
    }

}
