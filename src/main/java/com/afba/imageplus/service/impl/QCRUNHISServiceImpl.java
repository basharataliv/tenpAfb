package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.dto.res.QCRunTimeCheckRes;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.model.sqlserver.QCRUNHIS;
import com.afba.imageplus.model.sqlserver.WRKQCRUN;
import com.afba.imageplus.repository.sqlserver.QCRUNHISRepository;
import com.afba.imageplus.service.QCRUNHISService;
import com.afba.imageplus.service.UserProfileService;
import com.afba.imageplus.service.WRKQCRUNService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class QCRUNHISServiceImpl extends BaseServiceImpl<QCRUNHIS, String> implements QCRUNHISService {

    private final UserProfileService userProfileService;
    private final WRKQCRUNService wrkQcRunService;

    protected QCRUNHISServiceImpl(QCRUNHISRepository qcRunHisRepository, UserProfileService userProfileService,
            WRKQCRUNService wrkQcRunService) {
        super(qcRunHisRepository);
        this.userProfileService = userProfileService;
        this.wrkQcRunService = wrkQcRunService;
    }

    @Override
    protected String getNewId(QCRUNHIS entity) {
        return entity.getQcCaseId();
    }

    public QCRUNHIS updateQcFlag(String caseId, String userId, String qcPassFlag) {

        QCRUNHIS entity = new QCRUNHIS(caseId, null, null, null, userId, qcPassFlag, LocalDate.now(), LocalTime.now());
        return super.update(caseId, entity);
    }

    @Transactional
    public QCRunTimeCheckRes qcRunTimeCheck(String userId, String caseId, String policyId, String documentType) {

        String qcFlag = "N";
        QCRunTimeCheckRes response = new QCRunTimeCheckRes();
        Optional<EKD0360UserProfile> userOpt = userProfileService.findById(userId);
        if (userOpt.isPresent()) {
            EKD0360UserProfile user = userOpt.get();
            Optional<WRKQCRUN> wrkQcRunOpt = wrkQcRunService.findById(userId);
            if (wrkQcRunOpt.isPresent()) {
                WRKQCRUN wrkQcRun = wrkQcRunOpt.get();
                if (documentType.contains("BA")) {
                    wrkQcRun.setWqBaRunCont(wrkQcRun.getWqBaRunCont() + 1);
                    if (wrkQcRun.getWqBaRunCont() >= user.getWqBaPrfCont()) {
                        qcFlag = "Y";
                        wrkQcRun.setWqBaRunCont(0);
                    }
                } else if (documentType.contains("LT")) {
                    wrkQcRun.setWqLtRunCont(wrkQcRun.getWqLtRunCont() + 1);
                    if (wrkQcRun.getWqLtRunCont() >= user.getWqLtPrfCont()) {
                        qcFlag = "Y";
                        wrkQcRun.setWqLtRunCont(0);
                    }
                } else if (documentType.contains("IP")) {
                    wrkQcRun.setWqIpRunCont(wrkQcRun.getWqIpRunCont() + 1);
                    if (wrkQcRun.getWqIpRunCont() >= user.getWqIpPrfCont()) {
                        qcFlag = "Y";
                        wrkQcRun.setWqIpRunCont(0);
                    }
                } else if (documentType.contains("DR")) {
                    wrkQcRun.setWqDrRunCont(wrkQcRun.getWqDrRunCont() + 1);
                    if (wrkQcRun.getWqDrRunCont() >= user.getWqDrPrfCont()) {
                        qcFlag = "Y";
                        wrkQcRun.setWqDrRunCont(0);
                    }
                } else if (documentType.contains("GF")) {
                    wrkQcRun.setWqGfRunCont(wrkQcRun.getWqGfRunCont() + 1);
                    if (wrkQcRun.getWqGfRunCont() >= user.getWqGfPrfCont()) {
                        qcFlag = "Y";
                        wrkQcRun.setWqGfRunCont(0);
                    }
                } else if (documentType.contains("BE")) {
                    wrkQcRun.setWqBeRunCont(wrkQcRun.getWqBeRunCont() + 1);
                    if (wrkQcRun.getWqBeRunCont() >= user.getWqBePrfCont()) {
                        qcFlag = "Y";
                        wrkQcRun.setWqBeRunCont(0);
                    }
                } else {
                    return this.errorService.throwException(HttpStatus.BAD_REQUEST, EKDError.WKQCRN001);
                }
                wrkQcRunService.update(userId, wrkQcRun);
                if ("Y".equals(qcFlag)) {
                    QCRUNHIS qcrunhis = QCRUNHIS.builder().qcCaseId(caseId).qcPolId(policyId).qcDocType(documentType)
                            .qcUserId(userId).qcReviewer("").qcQcPass(qcFlag).qcDate(LocalDate.now())
                            .qcTime(LocalTime.now()).build();
                    super.insert(qcrunhis);
                }
                response.setQcFlag(qcFlag);
                return response;
            } else {
                return this.errorService.throwException(HttpStatus.NOT_FOUND, EKDError.WKQCRN404, userId);
            }

        } else {
            return this.errorService.throwException(HttpStatus.NOT_FOUND, EKDError.EKD360404, userId);
        }
    }
}
