package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.LIFEAGT;
import com.afba.imageplus.repository.BaseRepository;
import com.afba.imageplus.service.LIFEAGTService;
import org.springframework.stereotype.Service;

@Service
public class LIFEAGTServiceImpl extends BaseServiceImpl<LIFEAGT,String> implements LIFEAGTService {
    protected LIFEAGTServiceImpl(BaseRepository<LIFEAGT, String> repository) {
        super(repository);
    }

    @Override
    protected String getNewId(LIFEAGT entity) {
        return null;
    }
}
