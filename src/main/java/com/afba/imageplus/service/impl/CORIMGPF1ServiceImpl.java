package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.CORIMGPF1;
import com.afba.imageplus.model.sqlserver.id.CORIMGPFKey;
import com.afba.imageplus.repository.sqlserver.CORIMGPF1Repository;
import com.afba.imageplus.service.CORIMGPF1Service;
import org.springframework.stereotype.Service;

@Service
public class CORIMGPF1ServiceImpl extends BaseServiceImpl<CORIMGPF1, CORIMGPFKey> implements CORIMGPF1Service {

    protected CORIMGPF1ServiceImpl(CORIMGPF1Repository repository) {
        super(repository);
    }

    @Override
    protected CORIMGPFKey getNewId(CORIMGPF1 entity) {
        return new CORIMGPFKey(entity.getPolicyNo(), entity.getFileName());
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
