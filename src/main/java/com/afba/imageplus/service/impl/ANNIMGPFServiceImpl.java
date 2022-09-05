package com.afba.imageplus.service.impl;

import org.springframework.stereotype.Service;

import com.afba.imageplus.model.sqlserver.ANNIMGPF;
import com.afba.imageplus.repository.sqlserver.ANNIMGPFRepository;
import com.afba.imageplus.service.ANNIMGPFService;

@Service
public class ANNIMGPFServiceImpl extends BaseServiceImpl<ANNIMGPF, String> implements ANNIMGPFService {

    protected ANNIMGPFServiceImpl(ANNIMGPFRepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(ANNIMGPF entity) {
        return entity.getPolicyNo();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
