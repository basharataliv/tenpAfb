package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.MOVECASEH;
import com.afba.imageplus.repository.BaseRepository;
import com.afba.imageplus.service.MOVECASEHService;
import org.springframework.stereotype.Service;

@Service
public class MOVECASEHServiceImpl extends BaseServiceImpl<MOVECASEH, Long> implements MOVECASEHService {

    protected MOVECASEHServiceImpl(BaseRepository<MOVECASEH, Long> repository) {
        super(repository);
    }

    protected Long getNewId(MOVECASEH entity) {

        return entity.getId();
    }
}
