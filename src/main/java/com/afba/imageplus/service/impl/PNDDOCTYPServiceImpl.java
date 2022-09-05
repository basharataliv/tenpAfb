package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.model.sqlserver.PNDDOCTYP;
import com.afba.imageplus.repository.sqlserver.PNDDOCTYPRepository;
import com.afba.imageplus.service.PNDDOCTYPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PNDDOCTYPServiceImpl extends BaseServiceImpl<PNDDOCTYP, String> implements PNDDOCTYPService {

    private final PNDDOCTYPRepository pnddoctypRepository;

    @Autowired
    public PNDDOCTYPServiceImpl(PNDDOCTYPRepository pnddoctypRepository) {
        super(pnddoctypRepository);
        this.pnddoctypRepository = pnddoctypRepository;
    }

    @Override
    protected String getNewId(PNDDOCTYP entity) {
        return entity.getDocType();
    }

    public PNDDOCTYP getPendDocTypeById(String docType) {
        var pndDocType = pnddoctypRepository.findById(docType);
        if (pndDocType.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.PNDDOC404, docType);
        }
        return pndDocType.get();
    }
}
