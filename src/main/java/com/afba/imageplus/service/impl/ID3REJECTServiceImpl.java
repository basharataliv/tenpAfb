package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.ID3REJECT;
import com.afba.imageplus.repository.sqlserver.ID3REJECTRepository;
import com.afba.imageplus.service.ID3REJECTService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ID3REJECTServiceImpl extends BaseServiceImpl<ID3REJECT, Long> implements ID3REJECTService {
    private final ID3REJECTRepository repository;

    protected ID3REJECTServiceImpl(ID3REJECTRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected Long getNewId(ID3REJECT entity) {
        return entity.getId();
    }

    @Override
    public List<ID3REJECT> getBySsn(long ssn) {
        List<ID3REJECT> ssnList = repository.findBySsnNo(ssn);
        if (ssnList.isEmpty()) {
            errorService.throwException(HttpStatus.NOT_FOUND, EKDError.ID3RJT404, ssn);
        }
        return ssnList;
    }
}
