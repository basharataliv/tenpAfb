package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.DDAPPS;
import com.afba.imageplus.repository.sqlserver.DDAPPSRepository;
import com.afba.imageplus.service.DDAPPSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DDAPPSServiceImpl extends BaseServiceImpl<DDAPPS, String> implements DDAPPSService {

    protected DDAPPSServiceImpl(
            DDAPPSRepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(DDAPPS entity) {
        return entity.getTransactionId();
    }


}
