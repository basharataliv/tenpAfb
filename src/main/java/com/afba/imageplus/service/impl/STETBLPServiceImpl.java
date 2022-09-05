package com.afba.imageplus.service.impl;


import com.afba.imageplus.model.sqlserver.STETBLP;
import com.afba.imageplus.model.sqlserver.id.STETBLPKey;
import com.afba.imageplus.repository.sqlserver.STETBLPRepository;
import com.afba.imageplus.service.STETBLPService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class STETBLPServiceImpl extends BaseServiceImpl<STETBLP, STETBLPKey> implements STETBLPService {

    private final STETBLPRepository stetblpRepository;

    protected STETBLPServiceImpl(STETBLPRepository repository) {
        super(repository);
        this.stetblpRepository = repository;
    }

    @Override
    protected STETBLPKey getNewId(STETBLP entity) {
        return new STETBLPKey(entity.getCrcpCd(), entity.getCrcoCd());
    }

    public List<STETBLP> findByCrcpCd(String crcpCd) {
        return this.stetblpRepository.findByCrcpCd(crcpCd);
    }
}
