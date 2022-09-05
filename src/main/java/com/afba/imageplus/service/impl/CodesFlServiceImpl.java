package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.CODESFL;
import com.afba.imageplus.repository.sqlserver.CodesFlRepository;
import com.afba.imageplus.service.CodesFlService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CodesFlServiceImpl extends BaseServiceImpl<CODESFL, Long> implements CodesFlService {

    CodesFlRepository codesFlRepository;

    protected CodesFlServiceImpl(CodesFlRepository repository) {
        super(repository);
        this.codesFlRepository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String generateNewSsn() {
        List<CODESFL> codesFlOpt = codesFlRepository.findBySysCodeAndSysCodeType("SSN", "CUSTID");

        if (!codesFlOpt.isEmpty()) {
            CODESFL codesFlObj = codesFlOpt.get(0);
            Long nextSsn = Long.parseLong(codesFlObj.getSysCodeBuff()) + 1;
            codesFlObj.setSysCodeBuff(nextSsn.toString());
            super.update(codesFlObj.getId(), codesFlObj);
            return nextSsn.toString();
        } else {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.CODSFL001);
        }
    }

    @Override
    protected Long getNewId(CODESFL entity) {
        return entity.getId();
    }

    @Override
    public List<CODESFL> findBySysCodeTypeAndSysCode(String sysCodeType, String sysCode) {
        return this.codesFlRepository.findBySysCodeAndSysCodeType(sysCode, sysCodeType);
    }
}
