package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.dto.EKDMOVEEmailDto;
import com.afba.imageplus.dto.req.RMVOBJReq;
import com.afba.imageplus.model.sqlserver.DOCMOVE;
import com.afba.imageplus.model.sqlserver.DOCTEMP;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EmailTemplate;
import com.afba.imageplus.model.sqlserver.MOVETRAIL;
import com.afba.imageplus.model.sqlserver.id.EKD0315CaseDocumentKey;
import com.afba.imageplus.repository.sqlserver.EmailTemplateRepository;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.DOCMOVEService;
import com.afba.imageplus.service.DOCTEMPService;
import com.afba.imageplus.service.EKDMoveService;
import com.afba.imageplus.service.MOVETRAILService;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.EmailUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EKDMoveServiceImpl implements EKDMoveService {
    Logger logger = LoggerFactory.getLogger(EKDMoveServiceImpl.class);
    private final CaseQueueService caseQueueService;
    private final DOCMOVEService dOCMOVEService;
    private final CaseService caseService;
    private final CaseDocumentService caseDocumentService;
    private final MOVETRAILService mOVETRAILService;
    private final DOCTEMPService dOCTEMPService;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailUtility emailUtility;
    private final AuthorizationHelper authorizationHelper;

    @Autowired
    protected EKDMoveServiceImpl(CaseQueueService caseQueueService, DOCMOVEService dOCMOVEService,
            CaseService caseService, CaseDocumentService caseDocumentService, MOVETRAILService mOVETRAILService,
            DOCTEMPService dOCTEMPService, SpringTemplateEngine thymeleafTemplateEngine,
            EmailTemplateRepository emailTemplateRepository, EmailUtility emailUtility,
            AuthorizationHelper authorizationHelper) {
        this.caseQueueService = caseQueueService;
        this.dOCMOVEService = dOCMOVEService;
        this.caseService = caseService;
        this.caseDocumentService = caseDocumentService;
        this.mOVETRAILService = mOVETRAILService;
        this.dOCTEMPService = dOCTEMPService;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailUtility = emailUtility;
        this.authorizationHelper = authorizationHelper;

    }

    @Override
    public void eKDMoveProcessingForMoveQueue() {

        List<EKD0250CaseQueue> listCases = caseQueueService.getCaseDocumentsFromQueue("MOVE");
        try {
            listCases.forEach(cases -> {
                List<EKD0315CaseDocument> listDocs = new ArrayList<>(cases.getCases().getDocuments());
                listDocs.forEach(doc -> {
                    if (doc.getDocument() != null) {
                        Optional<DOCMOVE> docMove = dOCMOVEService
                                .findById(doc.getDocument().getDocumentType().substring(0, 4));
                        EKD0310Document document = doc.getDocument();
                        if (docMove.isPresent()) {
                            List<EKD0350Case> matchedCases = caseService.findByCmAccountNumberAndName(
                                    cases.getCases().getCmAccountNumber(), docMove.get().getCaseNumber());
                            if (!matchedCases.isEmpty()) {
                                matchedCases.forEach(matchCase -> {
                                    if (!caseDocumentService.existsById(new EKD0315CaseDocumentKey(
                                            matchCase.getCaseId(), document.getDocumentId()))) {
                                        EKD0315CaseDocument caseDoc = new EKD0315CaseDocument();
                                        caseDoc.setCaseId(matchCase.getCaseId());
                                        caseDoc.setDocumentId(document.getDocumentId());
                                        caseDoc.setScanningDateTime(document.getScanningDateTime());
                                        caseDoc.setScanningUser(document.getScanningRepId());
                                        caseDocumentService.insert(caseDoc);
                                    }
                                });
                            } else {
                                sendEmail(cases.getCaseId(), cases.getCases().getCmAccountNumber(),
                                        cases.getCases().getCurrentQueueId());
                            }
                            try {
                                caseDocumentService.RMVOBJDocument(new RMVOBJReq(document.getDocumentId(),
                                        doc.getCaseId(), "N", doc.getCases().getCmAccountNumber(),
                                        authorizationHelper.getRequestRepId()), "");
                                mOVETRAILService.save(new MOVETRAIL(doc.getCaseId(), document.getDocumentId(),
                                        doc.getScanningDateTime() != null ? doc.getScanningDateTime().toLocalDate() : doc.getScanningDate()));
                            } catch (Exception e) {
                                logger.error("error in Apis " + e);
                                e.printStackTrace();
                            }
                        } else {
                            Optional<DOCTEMP> docTamp = dOCTEMPService.findById(doc.getDocument().getDocumentType());
                            if (docTamp.isPresent()) {
                                try {
                                    caseDocumentService.RMVOBJDocument(new RMVOBJReq(document.getDocumentId(),
                                            doc.getCaseId(), "N", doc.getCases().getCmAccountNumber(),
                                            authorizationHelper.getRequestRepId()), "");
                                } catch (Exception e) {
                                    logger.error("error RMVOBJ api " + e);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                try {
                    caseService.closeCase(cases.getCaseId());
                    caseService.deleteCase(cases.getCaseId());
                } catch (Exception e) {
                    logger.error("error calling apis " + e);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            logger.error("error in ekd move" + e);
            e.printStackTrace();
        }
    }

    @Override
    public void eKDMoveProcessingForDeleteQueue() {
        try {
            List<EKD0250CaseQueue> listCases = caseQueueService
                    .getCaseDocumentsFromMultipleQueue(Arrays.asList("DELETE", "DELETES"));
            listCases.forEach(cases -> {
                List<EKD0315CaseDocument> listDocs = new ArrayList<>(cases.getCases().getDocuments());
                listDocs.forEach(doc -> {
                    String docId = doc.getDocument().getDocumentId();
                    try {
                        // call remove obj
                        caseDocumentService.RMVOBJDocument(new RMVOBJReq(docId, doc.getCaseId(), "N",
                                doc.getCases().getCmAccountNumber(), authorizationHelper.getRequestRepId()), "");
                    } catch (Exception e) {
                        logger.error("error RMVOBJ api" + e);
                        e.printStackTrace();
                    }
                });
                try {
                    caseService.closeCase(cases.getCaseId());
                    caseService.deleteCase(cases.getCaseId());
                } catch (Exception e) {
                    logger.error("error calling apis" + e);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            logger.error("error in delete" + e);
        }
    }

    private void sendEmail(String caseId, String Policy, String queue) {
        String ccRecepients = "";
        String bccRecepients = "";
        Optional<EmailTemplate> emailTemplateOpt = emailTemplateRepository
                .findByTemplateNameAndIsActive(ApplicationConstants.EKDMOVE_EMAIL_TEMPLATE_NAME, true);
        if (emailTemplateOpt.isPresent()) {

            EmailTemplate emailTemplate = emailTemplateOpt.get();
            ccRecepients = emailTemplate.getEmailCC() == null ? "" : emailTemplate.getEmailCC();
            bccRecepients = emailTemplate.getEmailBcc() == null ? "" : emailTemplate.getEmailBcc();
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("ekdMoveDatails", new EKDMOVEEmailDto(caseId, Policy, queue));
            Context thymeleafContext = new Context();
            thymeleafContext.setVariables(templateModel);
            String htmlBody = thymeleafTemplateEngine.process(ApplicationConstants.EKDMOVE_EMAIL_TEMPLATE_NAME,
                    thymeleafContext);

            // Send Email With Highest Priority
            emailUtility.sendHtmlMessage(emailTemplate.getEmailSubject(), emailTemplate.getEmailTo(), ccRecepients,
                    bccRecepients, htmlBody, 1);
        } else {
            logger.info("Email template not found");
        }
    }
}