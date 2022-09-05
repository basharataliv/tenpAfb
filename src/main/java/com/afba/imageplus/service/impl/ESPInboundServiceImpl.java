package com.afba.imageplus.service.impl;

import com.afba.imageplus.api.dto.req.PolicyDetailsBaseReq;
import com.afba.imageplus.api.dto.req.PolicyDetailsReq;
import com.afba.imageplus.api.dto.res.GetPolicyRes;
import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.req.Enum.UnPendCallingProgram;
import com.afba.imageplus.dto.req.UnPendCaseReq;
import com.afba.imageplus.dto.res.QCRunTimeCheckRes;
import com.afba.imageplus.model.sqlserver.*;
import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.afba.imageplus.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ESPInboundServiceImpl implements ESPInboundService {

    private final Logger logger = LoggerFactory.getLogger(ESPInboundServiceImpl.class);
    private final EMSIINBDService emsiInbdService;
    private final EMSIUNDService emsiUndService;
    private final ID3REJECTService id3rejectService;
    private final EKDUserService ekdUserService;
    private final LifeProApiService lifeProApiService;
    private final CaseService caseService;
    private final DocumentService documentService;
    private final PROTRMPOLService protrmpolService;
    private final PendCaseService pendCaseService;
    private final QCRUNHISService qcrunhisService;
    private final AFB0660Service afb0660Service;
    private final CaseDocumentService caseDocumentService;

    @Value("${afba.esp-inbound.base-dir}")
    private String espInboundBaseDir;

    @Value("${life.pro.coder.id:}")
    private String lifeProCoderId;

    private Map<String, String> documentTypes;

    protected ESPInboundServiceImpl(EMSIINBDService emsiInbdService, EMSIUNDService emsiUndService,
            ID3REJECTService id3rejectService, EKDUserService ekdUserService, LifeProApiService lifeProApiService,
            CaseService caseService, DocumentService documentService, PROTRMPOLService protrmpolService,
            PendCaseService pendCaseService, QCRUNHISService qcrunhisService, AFB0660Service afb0660Service,
            CaseDocumentService caseDocumentService) {
        this.emsiInbdService = emsiInbdService;
        this.emsiUndService = emsiUndService;
        this.id3rejectService = id3rejectService;
        this.ekdUserService = ekdUserService;
        this.lifeProApiService = lifeProApiService;
        this.caseService = caseService;
        this.documentService = documentService;
        this.protrmpolService = protrmpolService;
        this.pendCaseService = pendCaseService;
        this.qcrunhisService = qcrunhisService;
        this.afb0660Service = afb0660Service;
        this.caseDocumentService = caseDocumentService;
    }

    @PostConstruct
    public void loadDocumentTypes() {
        documentTypes = new HashMap<>();
        documentTypes.put("APS", "MEDIAI");
        documentTypes.put("ATH", "APPSPEND");
        documentTypes.put("CHG", "APPSCHNG");
        documentTypes.put("MVR", "MEDIPMBW");
        documentTypes.put("CSN", "MEDIPMBW");
        documentTypes.put("TIK", "MEDIPMBW");
        documentTypes.put("EKG", "MEDIEKG");
        documentTypes.put("LAB", "MEDILAB");
        documentTypes.put("PT2", "MEDIPRT2");
        documentTypes.put("MSC", "MEDIAI");
        documentTypes.put("LTR", "APPSLTRS");
        documentTypes.put("UWD", "MEDIDETR");
        documentTypes.put("UW2", "MEDIDETR");
        documentTypes.put("CLS", "APPSCLSE");
        documentTypes.put("MB1", "MEDIMIB1");
        documentTypes.put("MB2", "MEDIMIB2");

    }

    public List<String> performEspInbound() {

        // Get distinct policies from EMSIINBND that are not processed yet
        List<String> emsiInbdPolicies = emsiInbdService.getDistinctPoliciesByProcessFlagFalse();
        for (String policyId : emsiInbdPolicies) {
            EMSIUND emsiUnd = null;

            // Fetch all EMSIINBND records against that policy that are unprocessed
            List<EMSIINBND> emsiInbndPolicyRecords = emsiInbdService.getByPolicyId(policyId);

            for (EMSIINBND emsiInbndRecord : emsiInbndPolicyRecords) {

                final String afbaJob = "AFBAJOB";
                String policyStatus = "";
                String productCode = "";
                String policyStatusReason = "";
                String clientId = "";
                String specialQueue = "";
                String finalQueue = "APPSEMSIDQ";
                String returnQueue = "";
                String paymentMode = "";
                String holdCaseId = "";
                boolean emsiWait = false;
                QCRunTimeCheckRes qcRunTimeResp;
                EKD0350Case ekd0350CaseTBA = null;
                boolean isUwdOrClsFile = false;
                if ("UWD".equals(emsiInbndRecord.getEiFileExt()) || "CLS".equals(emsiInbndRecord.getEiFileExt()))
                    isUwdOrClsFile = true;

                // Update EMSIINBND record process flag to Y regardless if any error detected
                emsiInbndRecord.setEiProcessFlg(true);
                emsiInbdService.update(emsiInbndRecord.getId(), emsiInbndRecord);

                // Validate EMSIINBND record EXT and PageNo fields
                boolean emsiInbndValidationResp = emsiInbdService.performValidationForEmsiInbndRecords(emsiInbndRecord);
                if (emsiInbndValidationResp) {

                    List<GetPolicyRes> policyResponse = null;

                    // Fetch relevant record from EMSIUND against the policy from EMSIINBND record
                    Optional<EMSIUND> emsiUndOpt = emsiUndService.getByPolicyAndProcessFlag(policyId, "Y");
                    if (emsiUndOpt.isPresent()) {
                        emsiUnd = emsiUndOpt.get();

                        try {
                            // Insert record in ID3Reject in case DU or DE
                            if ("DU".equals(emsiUnd.getEmsiReason()) || "DE".equals(emsiUnd.getEmsiReason())) {
                                id3rejectService.insert(ID3REJECT.builder()
                                        .ssnNo(Long.parseLong(ekdUserService.getByAccountNo(policyId).getSsn()))
                                        .policyCode(emsiUnd.getProductId()).rejectCauseCode("M000")
                                        .rejectDate(LocalDate.now()).referenceUserId(afbaJob)
                                        .refComment("AUTO REJECT BY ESP").referenceComment2("").referenceContNo("")
                                        .build());
                            }
                        } catch (Exception e) {
                            logger.error(
                                    "ESP Inbound Error - ID3REJECT insertion failed for EMSIINBD record with policy {} due to {}",
                                    policyId, e.getMessage());
                            continue;
                        }

                        try {
                            // Retrieve policy data from LIFEPRO API
                            policyResponse = lifeProApiService.getPolicyDetails(new PolicyDetailsBaseReq(
                                    new PolicyDetailsReq(emsiUnd.getCompanyCode(), emsiUnd.getPolicyId(),
                                            UUID.randomUUID().toString(), "String", lifeProCoderId))).getPolicyResult
                                                    .getGetPolicyResp();
                        } catch (Exception e) {
                            logger.error(
                                    "ESP Inbound Error - Exception from LIFEPRO API for EMSIINBD record with policy {} due to {}",
                                    policyId, e.getMessage());
                            continue;
                        }

                        // Sanity check, size should be 1
                        if (policyResponse != null && policyResponse.size() == 1) {

                            policyStatus = policyResponse.get(0).getContract_Code();
                            policyStatusReason = policyResponse.get(0).getContract_Reason();
                            clientId = policyResponse.get(0).getClient_ID();
                            paymentMode = policyResponse.get(0).getPayment_Code();
                            productCode = policyResponse.get(0).getProduct_Code();

                            if ("A".equals(policyStatus)) {
                                specialQueue = "MOVEAPPR";
                            } else if ("T".equals(policyStatus)) {
                                specialQueue = "MOVEDECL";
                            } else if ("P".equals(policyStatus) && "RI".equals(policyStatusReason)) {

                                if (clientId.matches(
                                        "^(TSA|MAS.*|NGAMS|NGASC|NGATN|IMC-ESP PAYROLL|IMC-GOV PAYROLL|ESP PAYROLL)$")
                                        || (("BENEARCH").equals(clientId)
                                                || ("CITBANK").equals(clientId) && "D".equals(paymentMode))
                                        || "E".equals(paymentMode)) {

                                    specialQueue = "MOVEAPPR";

                                } else {
                                    specialQueue = "MOVEQUED";
                                }
                            } else if ("P".equals(policyStatus)) {
                                specialQueue = "MOVEQUED";
                            }

                            // Step 3
                            // Find action case with description starting with 11
                            // If found, hold that case id
                            List<EKD0350Case> cases = caseService.findByCmAccountNumber(policyId).stream().filter(
                                    c -> c.getCmFormattedName().startsWith("11") && !c.getStatus().equals(CaseStatus.C))
                                    .collect(Collectors.toList());
                            if (cases.size() == 1) {
                                ekd0350CaseTBA = cases.get(0);
                                if (isUwdOrClsFile) {

                                    if (ekd0350CaseTBA.getStatus().equals(CaseStatus.W)) {
                                        emsiWait = true;
                                        holdCaseId = ekd0350CaseTBA.getCaseId();
                                    } else if (ekd0350CaseTBA.getStatus().equals(CaseStatus.P)) {
                                        holdCaseId = ekd0350CaseTBA.getCaseId();
                                    } else if ((ekd0350CaseTBA.getStatus().equals(CaseStatus.A)
                                            || ekd0350CaseTBA.getStatus().equals(CaseStatus.N))
                                            && !ekd0350CaseTBA.getCurrentQueueId().matches("^(DELETE|DELETES|MOVE)$")) {
                                        holdCaseId = ekd0350CaseTBA.getCaseId();
                                    }
                                } else {
                                    holdCaseId = ekd0350CaseTBA.getCaseId();
                                }
                            } else if (cases.size() > 1) {
                                logger.error("ESP Inbound Error - Multiple cases found against policy id {}", policyId);
                                continue;
                            }

                            // Step 4
                            // Create case if not found in previous step
                            if (StringUtils.isBlank(holdCaseId)) {

                                var ekd0350CaseTBAOpt = caseService.findById(caseService
                                        .createCase(new CaseCreateReq(null, null, "", "", null,
                                                null, policyId, "11 APPLICATION ACTION", "", "",
                                                null, null, null))
                                        .getCaseId());
                                if (ekd0350CaseTBAOpt.isPresent()) {
                                    ekd0350CaseTBA = ekd0350CaseTBAOpt.get();
                                    ekd0350CaseTBA.setLastUpdateDateTime(LocalDateTime.now());
                                    ekd0350CaseTBA.setScanningDateTime(LocalDateTime.now());
                                }
                                holdCaseId = ekd0350CaseTBA.getCaseId();
                            }

                            // Step 5
                            // Determine final queue for BA or LT
                            if (productCode.startsWith(ApplicationConstants.POLICY_TYPE_BA)) {

                                if ("TSA".equals(clientId) || "NGATN".equals(clientId)) {
                                    finalQueue = "BANGEMSIDQ";
                                } else if ("NGAMS".equals(clientId)) {
                                    finalQueue = "APPSBAMSDQ";
                                } else if (clientId.startsWith("MAS") || "NGASC".equals(clientId)) {
                                    finalQueue = "APPSMASDQ";
                                }
                            } else if (productCode.startsWith(ApplicationConstants.POLICY_TYPE_LT)) {
                                finalQueue = "LTEMSIDQ";
                            } else {
                                logger.error(
                                        "ESP Inbound Error - Product code from LifePro is not LT or BA against policy id {} and document id {}",
                                        policyId, emsiInbndRecord.getEiDocId());
                                continue;
                            }

                            // Step 6
                            // Import the document, link it to case. And change the document type based on
                            // file ext
                            var pathToInboundDocument = Path.of(espInboundBaseDir,
                                    emsiInbndRecord.getEiDocId() + "." + emsiInbndRecord.getEiFileExt());
                            if (Files.exists(pathToInboundDocument)) {
                                // Import document in image plus
                                importAndAssignDocument(pathToInboundDocument, emsiInbndRecord.getEiPageNo(),
                                        emsiInbndRecord.getEiFileExt(), holdCaseId);
                            } else {
                                logger.error("ESP Inbound Error - File not found against doc id {}",
                                        emsiInbndRecord.getEiDocId());
                                continue;
                            }

                            // Step 7
                            // Decide the return queue for UWD or CLS and unpend it if found
                            if (isUwdOrClsFile) {

                                EKD0370PendCase pendCase = null;
                                Optional<EKD0370PendCase> pendCaseOpt = pendCaseService.findById(holdCaseId);
                                if (pendCaseOpt.isPresent()) {
                                    pendCase = pendCaseOpt.get();
                                }

                                // If EMSI wait is found and queue is MOVEAPPR
                                // You check PROTRMPOL table and if record found set the special queue
                                if (emsiWait && "MOVEAPPR".equals(specialQueue)) {
                                    if (!protrmpolService.getbyNewPolicyId(policyId).isEmpty()) {
                                        specialQueue = "APPSTERMEQ";
                                    }
                                }
                                // If EMSI wait is not found and status is P
                                else if (!emsiWait && ekd0350CaseTBA.getStatus().equals(CaseStatus.P)) {
                                    returnQueue = finalQueue;
                                }

                                if ("UWD".equals(emsiInbndRecord.getEiFileExt())) {
                                    if (StringUtils.isBlank(specialQueue)) {
                                        returnQueue = finalQueue;
                                    } else if ("MOVEAPPR".equals(specialQueue) || "MOVEDECL".equals(specialQueue)) {
                                        returnQueue = "MOVE";
                                    } else if ("MOVEQUED".equals(specialQueue)) {
                                        returnQueue = finalQueue;
                                    } else if ("APPSTERMEQ".equals(specialQueue)) {
                                        returnQueue = "APPSTERMEQ";
                                    }
                                } else if ("CLS".equals(emsiInbndRecord.getEiFileExt())) {
                                    if ("MOVEAPPR".equals(specialQueue) || "MOVEDECL".equals(specialQueue)) {
                                        returnQueue = "MOVE";
                                    } else if ("MOVEQUED".equals(specialQueue)) {
                                        returnQueue = finalQueue;
                                    } else {
                                        returnQueue = "MEDIEMSICQ";
                                    }
                                }

                                // Delete PROTRMPOL records regardless its UWD or CLS and log it
                                logger.info("{} records removed from PROTRMPOL against policy id {}",
                                        protrmpolService.removebyNewPolicyId(policyId).size(), policyId);

                                if ("MOVE".equals(returnQueue)) {
                                    try {
                                        qcRunTimeResp = qcrunhisService.qcRunTimeCheck("AFBAJOB", holdCaseId, policyId,
                                                getDocumentTypeForQcCheck(ekd0350CaseTBA));
                                        if ("Y".equals(qcRunTimeResp.getQcFlag())) {
                                            returnQueue = "APPSQC";
                                        }
                                    } catch (Exception e) {
                                        logger.error(
                                                "ESP Inbound Error - QCRUNTIME check failed for EMSIINBND record with policy {} due to reason {}",
                                                policyId, e.getMessage());
                                    }

                                }

                                // Update the return queue of EKD0370 and unpend
                                if (pendCaseOpt.isPresent() && !StringUtils.isBlank(returnQueue)) {
                                    pendCase.setReturnQueue(returnQueue);
                                    pendCaseService.update(holdCaseId, pendCase);

                                    // Unpend the case
                                    caseService.unPendCaseFromIMPEMSITIF(new UnPendCaseReq(afbaJob,
                                            UnPendCallingProgram.IMPEMSITIF.name(), false, holdCaseId, "", false));
                                } else {
                                    logger.error(
                                            "ESP Inbound Error - No record to unpend in EKD0370, Return Queue {} , Caseid {}",
                                            returnQueue, holdCaseId);
                                }

                                // Step 8
                                if (pendCaseOpt.isPresent() && !"APPSQC".equals(returnQueue)) {
                                    // Write record to AFB0660
                                    afb0660Service.addHisInAFBA0660(ekd0350CaseTBA, policyId, afbaJob);
                                }
                            }

                        } else {
                            logger.error(
                                    "ESP Inbound Error - Multiple/No policies returned from LifePro against policy id {}",
                                    policyId);
                        }
                    } else {
                        logger.error("ESP Inbound Error - EMSIUND record not found against policy id {}", policyId);
                    }
                } else {
                    logger.error(
                            "ESP Inbound Error - EMSIINBND record validation failed against policy id {} and document id {}",
                            policyId, emsiInbndRecord.getEiDocId());
                }
            }

            // Update the EMSIUND record flag to D once all records from EMSIINBND against
            // same policy are processed
            if (emsiUnd != null) {
                emsiUnd.setProcessFlag("D");
                emsiUndService.update(emsiUnd.getRecordKey(), emsiUnd);
            }
        }
        return emsiInbdPolicies;
    }

    private EKD0315CaseDocument importAndAssignDocument(Path path, Integer noOfPages, String fileExt, String caseId) {
        try {
            // Insert document to EKD0310
            EKD0310Document document = EKD0310Document.builder()
                    .doc(new MockMultipartFile(path.getFileName().toString(), new FileInputStream(path.toString())))
                    .documentType(documentTypes.get(fileExt)).docPage(noOfPages).build();

            documentService.insert(document);

            // Link document to case
            EKD0315CaseDocument caseDocument = EKD0315CaseDocument.builder().caseId(caseId)
                    .documentId(document.getDocumentId()).build();
            return caseDocumentService.insert(caseDocument);
        } catch (IOException e) {
            throw new DomainException(HttpStatus.BAD_REQUEST, "EKDFIL404", "Unable to read file at " + path);
        }
    }

    private String getDocumentTypeForQcCheck(EKD0350Case ekd350Case) {

        return ekd350Case.getDocuments().stream()
                .filter(d -> d.getDocument().getDocumentType()
                        .matches(String.format("^(%s|%s)$", ApplicationConstants.DOCTYPE_APPSBA,
                                ApplicationConstants.DOCTYPE_APPSLT)))
                .map(c -> c.getDocument().getDocumentType()).findFirst().orElse("");

    }
}
