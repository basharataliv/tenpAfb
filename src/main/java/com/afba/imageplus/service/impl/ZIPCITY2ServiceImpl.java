package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.ZIPCITY2;
import com.afba.imageplus.repository.BaseRepository;
import com.afba.imageplus.service.ZIPCITY2Service;
import org.springframework.stereotype.Service;

@Service
public class ZIPCITY2ServiceImpl extends BaseServiceImpl<ZIPCITY2, Long> implements ZIPCITY2Service {
    protected ZIPCITY2ServiceImpl(BaseRepository<ZIPCITY2, Long> repository) {
        super(repository);
    }

    @Override
    protected Long getNewId(ZIPCITY2 entity) {
        return entity.getZip2();
    }
}
