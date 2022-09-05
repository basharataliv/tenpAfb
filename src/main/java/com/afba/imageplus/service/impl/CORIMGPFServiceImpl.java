package com.afba.imageplus.service.impl;

import com.afba.imageplus.model.sqlserver.CORIMGPF;
import com.afba.imageplus.model.sqlserver.id.CORIMGPFKey;
import com.afba.imageplus.repository.sqlserver.CORIMGPFRepository;
import com.afba.imageplus.service.CORIMGPFService;
import org.springframework.stereotype.Service;

@Service
public class CORIMGPFServiceImpl extends BaseServiceImpl<CORIMGPF, CORIMGPFKey> implements CORIMGPFService {

    protected CORIMGPFServiceImpl(CORIMGPFRepository repository) {
        super(repository);
    }

    @Override
    protected CORIMGPFKey getNewId(CORIMGPF entity) {
        return new CORIMGPFKey(entity.getPolicyNo(), entity.getFileName());
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
