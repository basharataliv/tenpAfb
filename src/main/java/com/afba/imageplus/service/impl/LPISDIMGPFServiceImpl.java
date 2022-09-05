package com.afba.imageplus.service.impl;

import org.springframework.stereotype.Service;

import com.afba.imageplus.model.sqlserver.LPISDIMGPF;
import com.afba.imageplus.repository.sqlserver.LPISDIMGPFRepository;
import com.afba.imageplus.service.LPISDIMGPFService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LPISDIMGPFServiceImpl extends BaseServiceImpl<LPISDIMGPF, String> implements LPISDIMGPFService {

    protected LPISDIMGPFServiceImpl(LPISDIMGPFRepository repository) {
        super(repository);
    }

    @Override
    protected String getNewId(LPISDIMGPF entity) {
        return entity.getPolicyNo();
    }

    @Override
    public void deleteAll() {
        log.info("Flushing LPISDIMGPF");
        repository.deleteAll();
    }
}
