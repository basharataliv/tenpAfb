package com.afba.imageplus.service.impl;

import org.springframework.stereotype.Service;

import com.afba.imageplus.model.sqlserver.ANNIMGPF1;
import com.afba.imageplus.repository.sqlserver.ANNIMGPF1Repository;
import com.afba.imageplus.service.ANNIMGPF1Service;

@Service
public class ANNIMGPF1ServiceImpl extends BaseServiceImpl<ANNIMGPF1, String> implements ANNIMGPF1Service {

    protected ANNIMGPF1ServiceImpl(ANNIMGPF1Repository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(ANNIMGPF1 entity) {
        return entity.getPolicyNo();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
