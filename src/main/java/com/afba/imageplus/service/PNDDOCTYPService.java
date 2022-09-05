package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.PNDDOCTYP;

public interface PNDDOCTYPService extends BaseService<PNDDOCTYP, String>{
    PNDDOCTYP getPendDocTypeById(String docType);
}
