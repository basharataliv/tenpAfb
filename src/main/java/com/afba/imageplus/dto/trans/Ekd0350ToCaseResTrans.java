package com.afba.imageplus.dto.trans;

import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import org.springframework.stereotype.Service;

@Service
public class Ekd0350ToCaseResTrans {

    public CaseResponse caseEntityToCaseRes(EKD0350Case case350) {
        var caseResponse = new CaseResponse(
                case350.getCaseId(), null, null,
                case350.getInitialQueueId(), case350.getInitialRepId(), case350.getLastRepId(), case350.getStatus(),
                null, null, case350.getCmAccountNumber(),
                case350.getCmFormattedName(), null, null,
                case350.getChargeBackFlag(), case350.getCurrentQueueId(), case350.getFiller(),
                null, null, null
        );
        caseResponse.setScanningDateTime(case350.getScanningDateTime());
        caseResponse.setCaseCloseDateTime(case350.getCaseCloseDateTime());
        caseResponse.setLastUpdateDateTime(case350.getLastUpdateDateTime());
        return caseResponse;
    }
}
