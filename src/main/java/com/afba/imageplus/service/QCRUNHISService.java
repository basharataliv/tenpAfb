package com.afba.imageplus.service;

import com.afba.imageplus.dto.res.QCRunTimeCheckRes;
import com.afba.imageplus.model.sqlserver.QCRUNHIS;

public interface QCRUNHISService extends BaseService<QCRUNHIS, String> {
    QCRUNHIS updateQcFlag(String caseId, String userId, String qcPassFlag);

    QCRunTimeCheckRes qcRunTimeCheck(String userId, String caseId, String policyId, String documentType);
}
