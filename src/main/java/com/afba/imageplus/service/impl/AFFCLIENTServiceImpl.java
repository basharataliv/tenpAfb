package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.AFFCLIENT;
import com.afba.imageplus.repository.BaseRepository;
import com.afba.imageplus.service.AFFCLIENTService;
import org.springframework.stereotype.Service;

@Service
public class AFFCLIENTServiceImpl extends BaseServiceImpl<AFFCLIENT,String>
        implements AFFCLIENTService {
    protected AFFCLIENTServiceImpl(BaseRepository<AFFCLIENT, String> repository) {
        super(repository);
    }

    @Override
    protected String getNewId(AFFCLIENT entity) {
        return null;
    }
}
