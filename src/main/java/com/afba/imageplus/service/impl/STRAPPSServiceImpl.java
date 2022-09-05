package com.afba.imageplus.service.impl;

import com.afba.imageplus.dto.req.EnqFLrReq;
import com.afba.imageplus.dto.req.Enum.UnPendCallingProgram;
import com.afba.imageplus.dto.req.UnPendCaseReq;
import com.afba.imageplus.dto.res.QCRunTimeCheckRes;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;
import com.afba.imageplus.model.sqlserver.EKD0370PendCase;
import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.afba.imageplus.model.sqlserver.id.EKD0315CaseDocumentKey;
import com.afba.imageplus.service.CaseCommentService;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.PendCaseService;
import com.afba.imageplus.service.QCRUNHISService;
import com.afba.imageplus.service.STRAPPSService;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.mst.pdfview.fonts.tt.Loca;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class STRAPPSServiceImpl implements STRAPPSService {
    Logger logger = LoggerFactory.getLogger(EKDMoveServiceImpl.class);
    private final CaseQueueService caseQueueService;
    private final CaseService caseService;
    private final CaseDocumentService caseDocumentService;
    private final DateHelper dateHelper;
    private final PendCaseService pendCaseService;
    private final CaseCommentService caseCommentService;
    private final QCRUNHISService qCRUNHISService;
    private final AuthorizationHelper authorizationHelper;

    @Autowired
    protected STRAPPSServiceImpl(CaseQueueService caseQueueService, CaseService caseService,
            CaseDocumentService caseDocumentService, CaseCommentService caseCommentService,
            PendCaseService pendCaseService, QCRUNHISService qCRUNHISService, DateHelper dateHelper,
            AuthorizationHelper authorizationHelper) {
        this.caseQueueService = caseQueueService;
        this.caseService = caseService;
        this.caseDocumentService = caseDocumentService;
        this.pendCaseService = pendCaseService;
        this.caseCommentService = caseCommentService;
        this.qCRUNHISService = qCRUNHISService;
        this.dateHelper = dateHelper;
        this.authorizationHelper = authorizationHelper;

    }

    @Override
    public void sTRAPPSProcessing() {
        List<EKD0250CaseQueue> list = caseQueueService
                .getCaseDocumentsFromMultipleQueue(Arrays.asList("APPSINQ", "MEDIINQ"));
        List<EKD0250CaseQueue> listCases = new ArrayList<>(list);
        listCases.forEach(cases -> {
            try {
                String targetCaseId = "";
                String targetQueueId = "";
                EKD0350Case targetCaseDetails = null;
                List<EKD0315CaseDocument> listDocs = new ArrayList<>(cases.getCases().getDocuments());

                List<EKD0350Case> matchedCases = caseService
                        .findByCmAccountNumberAndName(cases.getCases().getCmAccountNumber(), "11");
                matchedCases
                        .addAll(caseService.findByCmAccountNumberAndName(cases.getCases().getCmAccountNumber(), "31"));
                // apply strApps filter on the cases
                List<EKD0350Case> strAppCriteriaMatchedCase = getCasesOnStrAppCriteria(matchedCases, listDocs,
                        cases.getCases().getCaseId());
                if (!strAppCriteriaMatchedCase.isEmpty()) {

                    if (strAppCriteriaMatchedCase.size() == 1) {
                        targetCaseDetails = strAppCriteriaMatchedCase.get(0);
                        if (targetCaseDetails.getStatus().equals(CaseStatus.P)) {
                            targetQueueId = releaseTheCase(targetCaseDetails);
                            targetCaseId = targetCaseDetails.getCaseId();
                        } else {
                            targetCaseId = targetCaseDetails.getCaseId();
                            targetQueueId = targetCaseDetails.getCurrentQueueId();
                        }
                    } else {
                        String pendCase = "";
                        // more the one cases found
                        for (EKD0350Case cas : strAppCriteriaMatchedCase) {
                            if (!(cas.getStatus().equals(CaseStatus.W) || cas.getStatus().equals(CaseStatus.P))
                                    && !cas.getCurrentQueueId().isBlank()) {
                                targetCaseId = cas.getCaseId();
                                targetQueueId = cas.getCurrentQueueId();
                                targetCaseDetails = cas;
                                break;
                            } else if (cas.getStatus().equals(CaseStatus.P)) {
                                targetCaseId = cas.getCaseId();
                                pendCase = cas.getCaseId();
                                targetCaseDetails = cas;
                            }

                        }
                        if (targetCaseId.equals(pendCase)) {
                            targetQueueId = releaseTheCase(targetCaseDetails);
                        }
                    }

                    if (targetQueueId.equals("MOVE")) {
                        QCRunTimeCheckRes flag = callQCRunTimeCheck(targetCaseDetails);
                        if (flag.getQcFlag().equals("Y")) {
                            targetQueueId = "APPSQC";
                        }
                    }

                    // move docs to target case
                    String cas = targetCaseId;
                    cases.getCases().getDocuments().forEach(document -> {
                        if (!caseDocumentService
                                .existsById(new EKD0315CaseDocumentKey(cas, document.getDocumentId()))) {
                            EKD0315CaseDocument caseDoc = new EKD0315CaseDocument();
                            caseDoc.setCaseId(cas);
                            caseDoc.setDocumentId(document.getDocumentId());
                            caseDoc.setScanningUser(document.getScanningUser());
                            caseDoc.setScanningDateTime(document.getScanningDateTime());
                            try {
                                caseDocumentService.insert(caseDoc);
                            } catch (Exception e) {
                                logger.info("unable to load insert document " + document.getDocumentId() + " and case "
                                        + cas);
                            }
                        }
                    });
                    // comments replaces
                    addCommentsToTargetCase(cases, targetCaseId);

                    try {
                        // reassign the queue
                        var scanningDateTimeIsNotNull = targetCaseDetails.getScanningDateTime() != null;
                        String reqDate = dateHelper.reformateDate(scanningDateTimeIsNotNull ? targetCaseDetails.getScanningDateTime().toLocalDate() : targetCaseDetails.getScanningDate(), "yyyyMMdd");
                        String reqTime = dateHelper.reformateDate(scanningDateTimeIsNotNull ? targetCaseDetails.getScanningDateTime().toLocalTime() : targetCaseDetails.getScanningTime(), "hhmmss");
                        caseService.caseEnqueue(targetCaseId, new EnqFLrReq(authorizationHelper.getRequestRepId(),
                                targetQueueId, 0 + reqDate + reqTime));
                        // assign delete queue
                        scanningDateTimeIsNotNull = cases.getCases().getScanningDateTime() != null;
                        String date = dateHelper.reformateDate(scanningDateTimeIsNotNull ? cases.getCases().getScanningDateTime().toLocalDate() : cases.getCases().getScanningDate(), "yyyyMMdd");
                        String time = dateHelper.reformateDate(scanningDateTimeIsNotNull ? cases.getCases().getScanningDateTime().toLocalTime() : cases.getCases().getScanningTime(), "hhmmss");
                        caseService.caseEnqueue(cases.getCases().getCaseId(),
                                new EnqFLrReq(authorizationHelper.getRequestRepId(), "DELETES", 0 + date + time));
                    } catch (Exception e) {
                        logger.error("exception in EnqFlr", e);
                    }

                } else {
                    targetQueueId = getQueueOnDocTypeCriteria(listDocs);
                    targetCaseId = cases.getCases().getCaseId();
                    var scanningDateTimeIsNotNull = cases.getCases().getScanningDateTime() != null;
                    String date = dateHelper.reformateDate(scanningDateTimeIsNotNull ? cases.getCases().getScanningDateTime().toLocalDate() : cases.getCases().getScanningDate(), "yyyyMMdd");
                    String time = dateHelper.reformateDate(scanningDateTimeIsNotNull ? cases.getCases().getScanningDateTime().toLocalTime() : cases.getCases().getScanningTime(), "hhmmss");
                    try {
                        caseService.caseEnqueue(cases.getCases().getCaseId(),
                                new EnqFLrReq(authorizationHelper.getRequestRepId(), targetQueueId, 0 + date + time));
                    } catch (Exception e) {
                        logger.error("exception in EnqFlr", e);
                    }

                }

            } catch (Exception e) {
                logger.error("exception in strApp", e);
            }

        });

    }

    private String releaseTheCase(EKD0350Case existingCase) {
        EKD0370PendCase pendCase = pendCaseService.findByCaseId(existingCase.getCaseId());
        if (pendCase != null) {
            caseService.unPendCaseFromJob(new UnPendCaseReq(authorizationHelper.getRequestRepId(),
                    UnPendCallingProgram.JOB.name(), false, pendCase.getCaseId(), "", false), existingCase, pendCase);

            return pendCase.getReturnQueue();
        }
        return "";
    }

    private void addCommentsToTargetCase(EKD0250CaseQueue cases, String targetCaseId) {
        try {
            List<EKD0352CaseComment> commets = caseCommentService.getCommentsSetByCaseId(cases.getCases().getCaseId());
            commets.forEach(com -> com.setCaseId(targetCaseId));
            caseCommentService.saveAll(commets);
        } catch (Exception e) {
            logger.info("no Case Comments found " + e);
        }
    }

    private QCRunTimeCheckRes callQCRunTimeCheck(EKD0350Case strAppCriteriaMatchedCase) {
        try {
            return  qCRUNHISService.qcRunTimeCheck(authorizationHelper.getRequestRepId(),
                    strAppCriteriaMatchedCase.getCaseId(), strAppCriteriaMatchedCase.getCmAccountNumber(),
                    strAppCriteriaMatchedCase.getDocuments().get(0).getDocument().getDocumentType());

        } catch (Exception e) {
            logger.info("QCRun time check exception" + e);
            return new QCRunTimeCheckRes();
        }

    }

    private String getQueueOnDocTypeCriteria(List<EKD0315CaseDocument> listDocs) {
        Boolean mEDIPMBWDocType = false;
        Boolean mEDIDOCSExist = false;
        String targetQueue = "";
        for (EKD0315CaseDocument doc : listDocs) {
            if (doc.getDocument() != null) {
                if (doc.getDocument().getDocumentType().equals("MEDIPMBW")
                        || doc.getDocument().getDocumentType().equals("MEDIEXAM")
                        || doc.getDocument().getDocumentType().equals("MEDIPHY")) {
                    mEDIPMBWDocType = true;
                } else {
                    mEDIDOCSExist = true;
                }
            }
        }
        if (listDocs.isEmpty()) {
            return null;
        }
        EKD0310Document firstDoc = listDocs.get(0).getDocument();
        if (firstDoc != null) {

            if (firstDoc.getDocumentType().equals("PARAWORK")) {
                targetQueue = "FSROCIVLMQ";
            } else if (firstDoc.getDocumentType().equals("APPSRATE")) {
                targetQueue = "APPSRATEQ";
            } else if (mEDIDOCSExist) {
                targetQueue = "MEDIREVWQ";
            } else if (mEDIPMBWDocType && !mEDIDOCSExist) {
                targetQueue = "MEDIPMBWQ";
            } else if (firstDoc.getDocumentType().equals("APPSBA") || firstDoc.getDocumentType().equals("APPSLT")
                    || firstDoc.getDocumentType().equals("APPSIC")) {
                targetQueue = "APPSMAILQ";
            } else {
                targetQueue = "APPSVERIQ";
            }

            if (firstDoc.getDocumentType().equals("REPLMAIL") || firstDoc.getDocumentType().equals("REPLCORR")) {
                targetQueue = "APPSREPLQ ";
            }
        }
        return targetQueue;
    }

    private List<EKD0350Case> getCasesOnStrAppCriteria(List<EKD0350Case> matchedCases,
            List<EKD0315CaseDocument> listDocs, String caseId) {
        String docType = "";
        String docTypeFullB = "";
        int i = 0;
        List<EKD0350Case> strAppfilteredCase = new ArrayList<>(matchedCases);

        if (listDocs != null && !listDocs.isEmpty()) {
            if (listDocs.get(0).getDocument() != null) {
                docType = listDocs.get(0).getDocument().getDocumentType().substring(0, 4);
                docTypeFullB = listDocs.get(0).getDocument().getDocumentType();
            }
        }
        for (EKD0350Case getcase : matchedCases) {
            if (!caseId.equals(getcase.getCaseId())) {
                if (!getcase.getStatus().equals(CaseStatus.W)) {
                    String queue = "";
                    if (StringUtils.isNotBlank(getcase.getCurrentQueueId())) {
                        queue = getcase.getCurrentQueueId().substring(0, 4);
                    }
                    if ((queue.equals("APPS") || queue.equals("MEDI"))
                            || (getcase.getStatus().equals(CaseStatus.W) || getcase.getStatus().equals(CaseStatus.P))
                            || (queue.equals("FSRO") && (docType.equals("APPS") || docType.equals("MEDI")))) {

                        if ((getcase.getCurrentQueueId().equals("APPSGFQ")
                                || getcase.getCurrentQueueId().equals("FSROGFQ"))
                                || (getcase.getStatus().equals(CaseStatus.P) && docTypeFullB.equals("APPSGF"))) {
                            strAppfilteredCase.remove(i);
                        }
                    }

                } else {
                    strAppfilteredCase.remove(i);
                }
            } else {
                strAppfilteredCase.remove(i);
            }
            i++;
        }
        return strAppfilteredCase;
    }

}
