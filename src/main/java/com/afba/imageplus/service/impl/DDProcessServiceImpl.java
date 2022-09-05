package com.afba.imageplus.service.impl;

import com.afba.imageplus.api.dto.req.GetBenefitSummaryBaseReq;
import com.afba.imageplus.api.dto.req.GetBenefitSummaryReq;
import com.afba.imageplus.api.dto.req.PartyRelationshipBaseReq;
import com.afba.imageplus.api.dto.req.PartyRelationshipReq;
import com.afba.imageplus.api.dto.req.PartySearch;
import com.afba.imageplus.api.dto.req.PartySearchBaseReq;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryRes;
import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.constants.Template;
import com.afba.imageplus.constants.TransactionSource;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.EditingResponse;
import com.afba.imageplus.dto.req.CaseCommentLineReq;
import com.afba.imageplus.dto.req.CaseCommentReq;
import com.afba.imageplus.dto.req.CaseCreateReqExtended;
import com.afba.imageplus.dto.res.DDProcessResponse;
import com.afba.imageplus.model.sqlserver.AFB0660;
import com.afba.imageplus.model.sqlserver.DDAPPS;
import com.afba.imageplus.model.sqlserver.DDATTACH;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;
import com.afba.imageplus.model.sqlserver.EKD0353CaseCommentLine;
import com.afba.imageplus.model.sqlserver.EMSITIFF;
import com.afba.imageplus.model.sqlserver.EmailTemplate;
import com.afba.imageplus.model.sqlserver.ICRBuffer;
import com.afba.imageplus.model.sqlserver.ICRFile;
import com.afba.imageplus.model.sqlserver.LPAUTOISS;
import com.afba.imageplus.model.sqlserver.TRNIDPOLR;
import com.afba.imageplus.repository.sqlserver.EmailTemplateRepository;
import com.afba.imageplus.service.AFB0660Service;
import com.afba.imageplus.service.AUTOCMTPFService;
import com.afba.imageplus.service.CaseCommentService;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.DDAPPSService;
import com.afba.imageplus.service.DDATTACHService;
import com.afba.imageplus.service.DDCHECKService;
import com.afba.imageplus.service.DDCREDITService;
import com.afba.imageplus.service.DDProcessService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.EMSITIFFService;
import com.afba.imageplus.service.EditingService;
import com.afba.imageplus.service.EditingService.EditingSubject;
import com.afba.imageplus.service.ICRFileService;
import com.afba.imageplus.service.LPAUTOISSService;
import com.afba.imageplus.service.LifeProApiService;
import com.afba.imageplus.service.LifeProApplicationService;
import com.afba.imageplus.service.PolicyService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.TEMPCOMPMAPService;
import com.afba.imageplus.service.TRNIDPOLRService;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.EmailUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.afba.imageplus.constants.Template.APPSNG0702;
import static com.afba.imageplus.constants.TransactionSource.DATA_DIMENSION;
import static com.afba.imageplus.constants.TransactionSource.STAUNTON;


@Service
@Slf4j
public class DDProcessServiceImpl implements DDProcessService {

    private static final String DOCUMENT_TYPE = "ICRDOC";

    @Value("${afba.dd-process.base-dir:}")
    private String ddBaseDir;

    @Value("${life.pro.coder.id:}")
    private String lifeProCoderId;

    private final AuthorizationHelper authorizationHelper;
    private final DDAPPSService ddappsService;
    private final DDATTACHService ddattachService;
    private final DDCREDITService ddcreditService;
    private final DDCHECKService ddcheckService;
    private final DocumentService documentService;
    private final CaseService caseService;
    private final CaseDocumentService caseDocumentService;
    private final TRNIDPOLRService trnidpolrService;
    private final ICRFileService icrFileService;
    private final CaseCommentService caseCommentService;
    private final LPAUTOISSService lpautoissService;
    private final EditingService editingService;
    private final QueueService queueService;
    private final EKDUserService ekdUserService;
    private final AFB0660Service afb0660Service;
    private final PolicyService policyService;
    private final EMSITIFFService emsitiffService;
    private final AUTOCMTPFService autocmtpfService;
    private final LifeProApiService lifeProApiService;
    private final LifeProApplicationService lifeProApplicationService;
    private final TEMPCOMPMAPService tempcompmapService;

    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailUtility emailUtility;

    private NavigableMap<String, TransactionSource> transactionSources;

    public DDProcessServiceImpl(
            AuthorizationHelper authorizationHelper,
            DDAPPSService ddappsService,
            DDATTACHService ddattachService,
            DDCREDITService ddcreditService,
            DDCHECKService ddcheckService,
            TEMPCOMPMAPService tempcompmapService,
            DocumentService documentService,
            CaseService caseService,
            CaseDocumentService caseDocumentService,
            TRNIDPOLRService trnidpolrService,
            ICRFileService icrFileService,
            CaseCommentService caseCommentService,
            LPAUTOISSService lpautoissService,
            EditingService editingService,
            QueueService queueService,
            EKDUserService ekdUserService,
            AFB0660Service afb0660Service,
            PolicyService policyService,
            EMSITIFFService emsitiffService,
            AUTOCMTPFService autocmtpfService,
            LifeProApiService lifeProApiService,
            LifeProApplicationService lifeProApplicationService,
            SpringTemplateEngine thymeleafTemplateEngine,
            EmailTemplateRepository emailTemplateRepository,
            EmailUtility emailUtility
    ) {
        this.authorizationHelper = authorizationHelper;
        this.ddappsService = ddappsService;
        this.ddattachService = ddattachService;
        this.ddcreditService = ddcreditService;
        this.ddcheckService = ddcheckService;
        this.tempcompmapService = tempcompmapService;
        this.documentService = documentService;
        this.caseService = caseService;
        this.caseDocumentService = caseDocumentService;
        this.trnidpolrService = trnidpolrService;
        this.icrFileService = icrFileService;
        this.caseCommentService = caseCommentService;
        this.lpautoissService = lpautoissService;
        this.editingService = editingService;
        this.queueService = queueService;
        this.ekdUserService = ekdUserService;
        this.afb0660Service = afb0660Service;
        this.policyService = policyService;
        this.emsitiffService = emsitiffService;
        this.autocmtpfService = autocmtpfService;
        this.lifeProApiService = lifeProApiService;
        this.lifeProApplicationService = lifeProApplicationService;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailUtility = emailUtility;
    }

    @PostConstruct
    public void loadTransactionSources() {
        transactionSources = new TreeMap<>();
        transactionSources.put("A", DATA_DIMENSION);
        transactionSources.put("C", DATA_DIMENSION);
        transactionSources.put("P", STAUNTON);
    }


    @Override
    public DDProcessResponse ddProcess() {
        // Read all records with PROCESSFLG = N
        List<DDAPPS> ddApps = ddappsService.findAll(Pageable.unpaged(), Map.of(DDAPPS.Fields.processFlag, false)).getContent();

        var response = new DDProcessResponse();

        // Process each app one by one along with its attachments, credits and checks.
        for (var ddApp : ddApps) {
            try {
                // Identify the source from transaction id's first byte.
                var firstByteOfTranId = ddApp.getTransactionId().substring(0, 1);
                var transactionSource = getTransactionSource(firstByteOfTranId);
                // Check if the tiff file exists.
                var pathToAppDocument = Path.of(ddBaseDir, "APPS", ddApp.getTransactionId() + ".TIF");
                if (!Files.exists(pathToAppDocument)) {
                    throw new DomainException(HttpStatus.BAD_REQUEST, "EKDDDP020", "App tiff file not found.");
                }
                // Validate template name.
                if (Arrays.stream(Template.values()).noneMatch(template -> template.toString().equals(ddApp.getTemplateName()))) {
                    throw new DomainException(HttpStatus.BAD_REQUEST, "EKDDDP030", "Invalid template name.");
                }

                var template = Template.valueOf(ddApp.getTemplateName());

                // Retrieve company code from TEMPCOMPMAP table against template name.
                var tempCompMap = this.tempcompmapService.findById(template.toString());
                if (tempCompMap.isEmpty()) {
                    throw new DomainException(HttpStatus.NOT_FOUND, "EKDDDP035", "Template to company code mapping not found.");
                }
                var companyCode = tempCompMap.get().getCompanyCode();
                var productCode = tempCompMap.get().getProductCode();

                // Validate template name to match data source.
                if (!template.getSources().contains(transactionSource)) {
                    throw new DomainException(HttpStatus.BAD_REQUEST, "EKDDDP040", "Template does not match its source.");
                }

                // If template name is NOT APPSNG0702
                // And data is from Data Dimension and/or eApps,
                // And check payment info and the pay mode is either credit card or PAC
                // And check if the credit tiff file exists.
                // If not credit then check if the check tiff file exists.
                if (!APPSNG0702.equals(template)
                        && DATA_DIMENSION.equals(transactionSource)
                        && List.of("0", "4").contains(ddApp.getPayMode())
                        && !Files.exists(Path.of(ddBaseDir, "CC", ddApp.getTransactionId() + ".TIF"))
                        && !Files.exists(Path.of(ddBaseDir, "CK", ddApp.getTransactionId() + ".TIF"))
                ) {
                    throw new DomainException(HttpStatus.BAD_REQUEST, "EKDDDP050", "Credit card or check tiff file not found.");
                }
                // If ATTACHFLG is equal to Y
                // And data source is from Data Dimensions and/or eApp
                List<DDATTACH> attachments = null;
                if (ddApp.getAttachmentFlag() != null && ddApp.getAttachmentFlag()
                        && DATA_DIMENSION.equals(transactionSource)
                ) {
                    // Read DDATTACH file to see if record exist
                    attachments = ddattachService.findAll(
                            Pageable.unpaged(),
                            Map.of(DDATTACH.Fields.transactionId, ddApp.getTransactionId(),
                                    DDATTACH.Fields.processFlag, false
                            )
                    ).getContent();
                    if (attachments.isEmpty()) {
                        throw new DomainException(HttpStatus.BAD_REQUEST, "EKDDDP060", "Attachment record not found.");
                    }
                    for (var ddAttach : attachments) {
                        // check if file exist in DATADIM/ATTACH.
                        var ext = ddattachService.getExtensionOfTemplate(ddAttach.getTemplateName());
                        if (!Files.exists(Path.of(ddBaseDir, "ATTACH", ddApp.getTransactionId() + ext))) {
                            throw new DomainException(HttpStatus.BAD_REQUEST, "EKDDDP070", "Attachment " + ext + " file not found.");
                        }
                    }
                }

                // Call IMPORT API to import the APPS document
                var document = importDocument(pathToAppDocument, ddApp.getNoOfPages());

                // Change document type
                changeDocumentType(document.getDocumentId(), "APPS" + template.getProductType());

                // Populate ICRFILE record
                var accountId = String.format(
                        "%s%s",
                        template.getProductType(),
                        new SimpleDateFormat("yyMMddHHmmssSS").format(new Date()).substring(0, 14)
                );

                var icrFile = icrFileService.insert(ICRFile.builder()
                        .documentId(document.getDocumentId())
                        .templateName(template.toString())
                        .accountId(accountId)
                        .icrBuffer(ICRBuffer.builder()
                                .ddApps(ddApp)
                                .ddAttachments(attachments)
                                .transactionSource(transactionSource.getValue())
                                .companyCode(companyCode)
                                .productCode(productCode)
                                .build())
                        .build());

                // Change the number of page in EKD0310 table
                // This can be skipped as we already have put the right page number while creating the record.

                // Call INDEXOBJ API to update the document type.  This is Image API restriction, Java can skip this step.
                // Skipping as mentioned.

                var emsiTiff = populateEmsiTiff(ddApp.getTransactionId(), document.getDocumentId());

                var actionCaseReq = this.id3Loadata(icrFile, emsiTiff);

                if (!actionCaseReq.isCreated()) {
                    // Call DEFFOLD API to create the Image case.
                    var caseRes = caseService.createCase(actionCaseReq);
                    actionCaseReq.setCaseId(caseRes.getCaseId());

                    // Populate case comments
                    if (actionCaseReq.getCaseComments() != null) {
                        actionCaseReq.getCaseComments()
                                .forEach(caseCommentReq ->
                                        caseCommentService.insert(
                                                EKD0352CaseComment.builder()
                                                        .caseId(actionCaseReq.getCaseId())
                                                        .commentLines(caseCommentReq.getCommentLines()
                                                                .stream()
                                                                .map(line -> EKD0353CaseCommentLine.builder()
                                                                        .commentSequence(0)
                                                                        .commentLine(line.getCommentLine())
                                                                        .build())
                                                                .collect(Collectors.toSet()))
                                                        .build())
                                );
                    }

                    // Call DEFSTOBJ to add the document to the case
                    var ekd0315 = EKD0315CaseDocument.builder()
                            .caseId(caseRes.getCaseId())
                            .documentId(document.getDocumentId())
                            .scanningUser(authorizationHelper.getRequestRepId())
                            .build();
                    ekd0315.setScanningDateTime(LocalDateTime.now());
                    caseDocumentService.insert(ekd0315);

                    // Populate TRNIDPOLR table record
                    trnidpolrService.insert(TRNIDPOLR.builder()
                            .tpTransactionId(ddApp.getTransactionId())
                            .tpPolicyId(caseRes.getCmAccountNumber())
                            .tpCaseId(caseRes.getCaseId())
                            .tpSource(transactionSource.getValue())
                            .tpDateIn(LocalDate.now())
                            .build());
                }
                // If attachment is found, Import attachment and add attachment document to the case
                if (attachments != null) {
                    processAttachments(attachments, actionCaseReq.getCaseId(), "1".equals(ddApp.getReplacementIndicator()));
                }

                // Process credit card payment data.
                // Loop through DDCREDITL1 (PROCESSFLG = N) table until end of file.
                processCreditCardPaymentData(ddApp.getTransactionId(), document.getDocumentId(), actionCaseReq.getCaseId(), accountId, icrFile);

                // Process PAC (Checkmatic) payment data. This is the exact logic as item #17 except referencing DDCHECKL1 table instead of DDCREDITL1 table.
                processCheckPaymentData(ddApp.getTransactionId(), document.getDocumentId(), actionCaseReq.getCaseId(), accountId, icrFile);

                // Update process flag and last change date time of the ddApp
                updateProcessFlag(ddApp);

                // Add success response
                response.addSuccess(ddApp.getTransactionId());

            } catch (Exception ex) {
                // Update the process flag to Y
                updateProcessFlag(ddApp);

                // Add failure response.
                response.addFailure(ddApp.getTransactionId(), ex.getMessage());

                // Send email to users with error message.
                sendFailureEmail(ddApp.getTransactionId(), ex.getMessage());
                log.error(String.format("DDPROCESS Error - Transaction id: %s, Message: %s", ddApp.getTransactionId(),
                        ex.getMessage()), ex);
            }
        }
        return response;
    }

    private TransactionSource getTransactionSource(String firstByte) {
        var entry = transactionSources.floorEntry(firstByte);
        if (entry == null) {
            throw new DomainException(HttpStatus.BAD_REQUEST, "EKDDDP010", "Invalid transaction id.");
        }
        return entry.getValue();
    }

    private EKD0310Document importDocument(Path path, Integer noOfPages) {
        try {
            var fileName = path.getFileName().toString()
                    .replaceAll(".RPL|.PND|.END", ".TIF");
            EKD0310Document document = EKD0310Document.builder()
                    .doc(new MockMultipartFile(fileName, fileName, MediaType.APPLICATION_OCTET_STREAM_VALUE, new FileInputStream(path.toString())))
                    .documentType(DOCUMENT_TYPE)
                    .docPage(noOfPages)
                    .documentExt(FilenameUtils.getExtension(path.getFileName().toString()))
                    .build();
            return documentService.importDocument(document);
        } catch (IOException e) {
            throw new DomainException(HttpStatus.BAD_REQUEST, "EKDFIL404", "Unable to read file at " + path);
        }
    }

    private void changeDocumentType(String documentId, String documentType) {
        documentService.changeDocumentType(documentId, documentType, null);
    }

    private EMSITIFF populateEmsiTiff(String transactionId, String documentId) {
        return emsitiffService.save(EMSITIFF.builder()
                .tifDocId(transactionId)
                .wafDocId(documentId)
                .processType("I")
                .processFlag(false)
                .processFlag1(false)
                .acknowledged(false)
                .acknowledged1(false)
                .policyId("")
                .policyId1("")
                .jtIssFlag1("")
                .jtIssFlag2("")
                .xRefFlag("0")
                .sendToEmsi("0")
                .build());
    }

    private void processAttachments(List<DDATTACH> attachments, String caseId, boolean isRpLind) {
        for (var ddAttach : attachments) {
            try {
                // Import attachment document
                var ext = ddattachService.getExtensionOfTemplate(ddAttach.getTemplateName());
                var pathToAttachment = Path.of(ddBaseDir, "ATTACH", ddAttach.getTransactionId() + ext);
                var attachmentDocument = importDocument(pathToAttachment, ddAttach.getNoOfPages());
                // Change document type
                String documentType;
                switch (ext) {
                    case ".RPL":
                        documentType = isRpLind ? "REPLMAIL" : "REPLNONE";
                        break;
                    case ".PND":
                        documentType = "APPSPEND";
                        break;
                    case ".END":
                        documentType = "APPSENDR";
                        break;
                    default:
                        documentType = null;
                }
                if (documentType != null) {
                    changeDocumentType(attachmentDocument.getDocumentId(), documentType);
                }

                // Map it to the case.
                var ekd315 = EKD0315CaseDocument.builder()
                        .caseId(caseId)
                        .documentId(attachmentDocument.getDocumentId())
                        .scanningUser(authorizationHelper.getRequestRepId())
                        .build();
                ekd315.setScanningDateTime(attachmentDocument.getScanningDateTime());
                caseDocumentService.insert(ekd315);

            } finally {
                // Set process flag to Y
                ddAttach.setProcessFlag(true);
                ddAttach.setLastChangeDate(LocalDate.now());
                ddAttach.setLastChangeTime(LocalTime.now());
                ddattachService.save(ddAttach);
            }
        }
    }

    private void processCreditCardPaymentData(String transactionId, String documentId, String caseId, String accountId, ICRFile appIcrFile) {
        var ddCreditNullable = ddcreditService.findById(transactionId);

        if (ddCreditNullable.isPresent() && ddCreditNullable.get().getProcessFlag() != null && !Boolean.TRUE.equals(ddCreditNullable.get().getProcessFlag())) {
            var ddCredit = ddCreditNullable.get();
            try {
                var pathToCreditCardDocument = Path.of(ddBaseDir, "CC", transactionId + ".TIF");

                // Use transaction ID of DDCREDITL1 against DDAPPS table.  (DDAPPS, DDCREDIT, DDCHECK tables have the identical transaction ID)
                // If record is not found in DDAPPS table, update the PROCESSFLG in DDCREDITL1 table and send error email to multiple users.
                // Above two steps are not necessary as we are processing transaction wise.

                // Get the APPS document ID by using transaction ID of DDCREDITL1 record against TRNIDPOLR table to get Image CASEID, and then use the CASEID against EKD0315X table to get DOCID.  Finally, use DOCID against EKD0310 table to make sure the DOCTYPE is “APPSxx” to identify the APPS DOCID (All apps document have APPSBA, APPSLT, APPSGF)
                // Skipping this step as discussed with Ram.

                // Use APPS DOCID against ICRFILE and check the return code in ICRBUFFER1271st byte for 4 digits return code.
                // Skipping this step as we are processing payment record along with its app record.

                EKD0310Document creditCardDocument = null;

                // Call IMPORT API to import credit card document.
                creditCardDocument = importDocument(pathToCreditCardDocument, ddCredit.getNoOfPages());

                // Update the number of page in EKD0310 table
                // No need of the above step.

                // Call INDEXOBJ to change document type.
                changeDocumentType(creditCardDocument.getDocumentId(), "CREDAUTH");

                // Populate ICRFILE record by loading credit card info from DDCREDITL1 table
                var icrFile = icrFileService.insert(ICRFile.builder()
                        .documentId(creditCardDocument.getDocumentId())
                        .templateName(ddCredit.getTemplateName())
                        .accountId(accountId)
                        .icrBuffer(ICRBuffer.builder().ddCredit(ddCredit).caseId(caseId).build())
                        .build());

                // If payment form is mismatch the pay mode in App record (Return code = 9002)
                if (!"0".equals(appIcrFile.getIcrBuffer().getDdApps().getPayMode())) {
                    // Call DEFSTOBJ to add the credit card document to the image case.
                    var ekd315 = EKD0315CaseDocument.builder()
                            .caseId(caseId)
                            .documentId(creditCardDocument.getDocumentId())
                            .scanningUser(authorizationHelper.getRequestRepId())
                            .build();
                    ekd315.setScanningDateTime(creditCardDocument.getScanningDateTime());
                    caseDocumentService.insert(ekd315);
                    return;
                }

                // Populate EKD0215 table.   Note: EKD0215 table is the ImagePlus batch indexing request file.  Currently, we use ImagePlus batch index process to perform further process. This task can be replaced by calling EKD0105 program.
                icrFileService.processCreditCardForm(creditCardDocument.getDocumentId(), appIcrFile);


            } finally {
                // Set process flag to Y
                ddCredit.setProcessFlag(true);
                ddCredit.setLastChangeDate(LocalDate.now());
                ddCredit.setLastChangeTime(LocalTime.now());
                ddcreditService.save(ddCredit);
            }
        }

    }

    private void processCheckPaymentData(String transactionId, String documentId, String caseId, String accountId, ICRFile appIcrFile) {

        var ddCheckNullable = ddcheckService.findById(transactionId);

        if (ddCheckNullable.isPresent() && ddCheckNullable.get().getProcessFlag() != null && !Boolean.TRUE.equals(ddCheckNullable.get().getProcessFlag())) {

            var ddCheck = ddCheckNullable.get();
            try {
                var pathToCheckDocument = Path.of(ddBaseDir, "CK", transactionId + ".TIF");

                // Use transaction ID of DDCREDITL1 against DDAPPS table.  (DDAPPS, DDCREDIT, DDCHECK tables have the identical transaction ID)
                // If record is not found in DDAPPS table, update the PROCESSFLG in DDCREDITL1 table and send error email to multiple users.
                // Above two steps are not necessary as we are processing transaction wise.

                // Get the APPS document ID by using transaction ID of DDCREDITL1 record against TRNIDPOLR table to get Image CASEID, and then use the CASEID against EKD0315X table to get DOCID.  Finally, use DOCID against EKD0310 table to make sure the DOCTYPE is “APPSxx” to identify the APPS DOCID (All apps document have APPSBA, APPSLT, APPSGF)
                // Skipping this step as discussed with Ram.

                // Use APPS DOCID against ICRFILE and check the return code in ICRBUFFER1271st byte for 4 digits return code.
                // Skipping this step as we are processing payment record along with its app record.

                EKD0310Document checkDocument = null;

                // Call IMPORT API to import credit card document.
                checkDocument = importDocument(pathToCheckDocument, ddCheck.getNoOfPages());

                // Update the number of page in EKD0310 table
                // No need of the above step.

                // Call INDEXOBJ to change document type.
                changeDocumentType(checkDocument.getDocumentId(), "AUTHCHEK");

                // Populate ICRFILE record by loading credit card info from DDCREDITL1 table
                var icrFile = icrFileService.insert(ICRFile.builder()
                        .documentId(checkDocument.getDocumentId())
                        .templateName(ddCheck.getTemplateName())
                        .accountId(accountId)
                        .icrBuffer(ICRBuffer.builder().ddCheck(ddCheck).caseId(caseId).build())
                        .build());

                // If payment form is mismatch the pay mode in App record (Return code = 9002)
                if (!"4".equals(appIcrFile.getIcrBuffer().getDdApps().getPayMode())) {
                    // Call DEFSTOBJ to add the credit card document to the image case.
                    var ekd315 = EKD0315CaseDocument.builder()
                            .caseId(caseId)
                            .documentId(checkDocument.getDocumentId())
                            .scanningUser(authorizationHelper.getRequestRepId())
                            .build();
                    ekd315.setScanningDateTime(checkDocument.getScanningDateTime());
                    caseDocumentService.insert(ekd315);
                    return;
                }

                // Populate EKD0215 table. Note: EKD0215 table is the ImagePlus batch indexing request file.  Currently, we use ImagePlus batch index process to perform further process. This task can be replaced by calling EKD0105 program.
                icrFileService.processCheckMaticForm(checkDocument.getDocumentId(), appIcrFile);

            } finally {
                // Set process flag to Y
                ddCheck.setProcessFlag(true);
                ddCheck.setLastChangeDate(LocalDate.now());
                ddCheck.setLastChangeTime(LocalTime.now());
                ddcheckService.save(ddCheck);
            }
        }

    }

    private void updateProcessFlag(DDAPPS ddApp) {
        ddApp.setProcessFlag(true);
        ddApp.setLastChangeDate(LocalDate.now());
        ddApp.setLastChangeTime(LocalTime.now());
        ddappsService.save(ddApp);
    }

    @Override
    public CaseCreateReqExtended id3Loadata(ICRFile icrRecord, EMSITIFF emsiTiff) {

        var icrBuffer = icrRecord.getIcrBuffer();

        if (icrBuffer == null || icrBuffer.getDdApps() == null) {
            throw new DomainException(HttpStatus.BAD_REQUEST, "EKDICR400", "ICR Buffer is not set or could not be parsed from JSON string");
        }

        var applicationEditingResponses = editApplicationData(icrRecord);

        // Load Life Pro data for member's existing file.
        loadExistingFile(icrBuffer, icrBuffer.getDdApps().getMemberSSN(), EditingSubject.MEMBER);

        // Load Life Pro data for spouse's existing file.
        loadExistingFile(icrBuffer, icrBuffer.getDdApps().getSpouseSSN(), EditingSubject.SPOUSE);

        // member’s coverage amount > zero
        CaseCreateReqExtended actionCaseReq = null;
        if (icrBuffer.getDdApps().getMemberCoverUnit() > 0) {
            actionCaseReq = processMember(icrRecord, applicationEditingResponses, emsiTiff);
        }
        if (icrBuffer.getDdApps().getSpouseCoverUnit() > 0) {
            var spouseActionCaseReq = processSpouse(icrRecord, applicationEditingResponses, emsiTiff);
            actionCaseReq = actionCaseReq == null ? spouseActionCaseReq : actionCaseReq;
        }
        return actionCaseReq;
    }

    private CaseCreateReqExtended processMember(ICRFile icrFile, List<EditingResponse> applicationEditingResponses, EMSITIFF emsiTiff) {

        var icrBuffer = icrFile.getIcrBuffer();

        // Initialize a new case request
        var memberActionCase = new CaseCreateReqExtended();
        memberActionCase.setCaseId(caseService.generateCaseId());
        memberActionCase.setScanningDate(LocalDate.now());
        memberActionCase.setScanningTime(LocalTime.now());
        memberActionCase.setInitialRepId(authorizationHelper.getRequestRepId());

        var memberEditResponses = editMemberData(icrBuffer);
        memberEditResponses.addAll(applicationEditingResponses);

        var isDataClean = memberEditResponses.stream().allMatch(editingResponse -> StringUtils.isEmpty(editingResponse.getQueue()));

        var autoDecline = memberEditResponses.stream().anyMatch(EditingResponse::isAutoDecline);

        // Move Value to AR-AUTOISSUE
        icrBuffer.setMemberAutoIssue(getAutoIssueFlag(isDataClean, autoDecline, icrBuffer.getPayMode(), Template.valueOf(icrBuffer.getDdApps().getTemplateName())));

        // If an existing policy to be used
        var isExistingPolicy = false;
        if (icrBuffer.getMemberPolicyId() != null) {

            // Map document to existing policy by creating a new action case against the policy.
            isExistingPolicy = true;
            memberActionCase.setCmAccountNumber(icrBuffer.getMemberPolicyId());

            // Map document to existing policy in a permanent case
            if (icrBuffer.isMemberMoveDocumentToPermanentCase()) {
                memberActionCase.setCreated(true);
                return mapDocumentToPermanentCase(memberActionCase, icrFile.getDocumentId());
            }

        } else {

            // Generating policy id instead of calling ID342
            memberActionCase.setCmAccountNumber(policyService.getUniquePolicyId());
            icrBuffer.setMemberPolicyId(memberActionCase.getCmAccountNumber());

            // call LPIMAGE
            lifeProApplicationService.insertMember(icrFile);

            if (isDataClean) {
                // Populate LPAUTOISS table record first.
                lpautoissService.insert(LPAUTOISS.builder()
                        .lpPolicyId(memberActionCase.getCmAccountNumber())
                        .lpPolicyType(icrBuffer.getPolicyType())
                        .lpDocumentId(icrFile.getDocumentId())
                        .processFlag(false)
                        .entryDate(LocalDate.now())
                        .build());
            }

        }

        // Update ICRBuffer for future reference
        icrFileService.update(icrFile.getDocumentId(), icrFile);

        // Determine final queue first
        memberActionCase.setInitialQueueId(editingService.determineFinalQueue(memberEditResponses, icrBuffer.getDdApps().getTemplateName(), icrBuffer));

        // If data is not clean, populate TMPCMTPF record and submit a job to call ADDCMTC program to populate F11 comments
        if (!isDataClean) {
            // Populate case comments (returning comments to EKD0105)
            memberActionCase.setCaseComments(
                    memberEditResponses
                            .stream()
                            .filter(er -> StringUtils.isNotEmpty(er.getQueue()))
                            .map(er -> {
                                String comment = null;
                                if (StringUtils.isNotEmpty(er.getCommentCode())) {
                                    var autoComment = autocmtpfService.findById(er.getCommentCode());
                                    if (autoComment.isPresent()) {
                                        comment = autoComment.get().getCommentText();
                                    }
                                }
                                if (StringUtils.isEmpty(comment)) {
                                    comment = StringUtils.isNotEmpty(er.getComment()) ? er.getComment() : er.getCommentCode();
                                }
                                return new CaseCommentReq(new LinkedHashSet<>(Set.of(new CaseCommentLineReq(comment))));
                            })
                            .collect(Collectors.toList())
            );
        }

        // Call IMG2001 to get image case description and pass case description back to PL-CASE-DESCRIPTION
        // Instead call queue profile by id to get case description.
        var queueProfile = queueService.findById(memberActionCase.getInitialQueueId());
        queueProfile.ifPresent(ekd0150Queue -> memberActionCase.setCmFormattedName(ekd0150Queue.getCaseDescription()));

        // Populate AFB0660 table record by calling ADDHISC program, if member is auto issued
        if (isDataClean) {
            // populate AFB0660
            AFB0660 afb0660 = AFB0660.builder()
                    .policyId(memberActionCase.getCmAccountNumber())
                    .caseId(memberActionCase.getCaseId())
                    .caseType(memberActionCase.getCmFormattedName() == null || memberActionCase.getCmFormattedName().length() < 2
                            ? "11" : memberActionCase.getCmFormattedName().substring(0, 2))
                    .fromQueue("AUTOISSUE")
                    .toQueue(memberActionCase.getInitialQueueId())
                    .userId(authorizationHelper.getRequestRepId())
                    .arrivalDate(LocalDate.now())
                    .currentDate(LocalDate.now())
                    .currentTime(LocalTime.now())
                    .build();
            afb0660Service.save(afb0660);
        }


        if (!isExistingPolicy) {
            // Call EMPTYCASES program to create seven empty permanent cases
            caseService.generatePermanentCases(memberActionCase.getCmAccountNumber());

            // Populate EKDUSER record
            ekdUserService.insert(
                    memberActionCase.getCmAccountNumber(),
                    icrBuffer.getDdApps().getMemberSSN(),
                    icrBuffer.getDdApps().getMemberLastName(),
                    icrBuffer.getDdApps().getMemberFirstName(),
                    icrBuffer.getDdApps().getMemberMiddleInitial(),
                    true
            );
        }

        // Update EMSITIFF record
        if (icrBuffer.isMemberGoesToESP()) {
            emsiTiff.setPolicyId(memberActionCase.getCmAccountNumber());
            // As up to this step, only member is evaluated and needs to go to ESP so always 1.
            emsiTiff.setSendToEmsi("1");
            // if member doesn't need EKG or Paramedi then 1 (Jet Issue) else 0 (Standard Issue)
            emsiTiff.setJtIssFlag1(!icrBuffer.isMemberNeedEKG() && !icrBuffer.isMemberNeedParamedi() ? "1" : "0");
            emsitiffService.save(emsiTiff);
        }

        return memberActionCase;
    }

    private CaseCreateReqExtended processSpouse(ICRFile icrFile, List<EditingResponse> applicationEditingResponses, EMSITIFF emsiTiff) {

        var icrBuffer = icrFile.getIcrBuffer();

        var spouseActionCase = new CaseCreateReqExtended();
        spouseActionCase.setCaseId(caseService.generateCaseId());
        spouseActionCase.setScanningDate(LocalDate.now());
        spouseActionCase.setScanningTime(LocalTime.now());
        spouseActionCase.setInitialRepId(authorizationHelper.getRequestRepId());

        var spouseEditResponses = editSpouseData(icrBuffer);
        spouseEditResponses.addAll(applicationEditingResponses);

        var isDataClean = spouseEditResponses.stream().allMatch(editingResponse -> StringUtils.isEmpty(editingResponse.getQueue()));

        var autoDecline = spouseEditResponses.stream().anyMatch(EditingResponse::isAutoDecline);

        // Move Value to AR-AUTOISSUE
        icrBuffer.setSpouseAutoIssue(getAutoIssueFlag(isDataClean, autoDecline, icrBuffer.getPayMode(), Template.valueOf(icrBuffer.getDdApps().getTemplateName())));

        // If an existing policy to be used
        var isExistingPolicy = false;
        if (icrBuffer.getSpousePolicyId() != null) {

            // Map document to existing policy by creating a new action case against the policy.
            isExistingPolicy = true;
            spouseActionCase.setCmAccountNumber(icrBuffer.getSpousePolicyId());

            // Map document to existing policy in a permanent case
            if (icrBuffer.isMemberMoveDocumentToPermanentCase()) {
                spouseActionCase.setCreated(true);
                return mapDocumentToPermanentCase(spouseActionCase, icrFile.getDocumentId());
            }
        } else {

            // Generating policy id instead of calling ID342
            spouseActionCase.setCmAccountNumber(policyService.getUniquePolicyId());
            icrBuffer.setSpousePolicyId(spouseActionCase.getCmAccountNumber());

            // call LPIMAGE
            lifeProApplicationService.insertSpouse(icrFile);

            if (isDataClean) {
                // Populate LPAUTOISS table record first.
                lpautoissService.insert(LPAUTOISS.builder()
                        .lpPolicyId(spouseActionCase.getCmAccountNumber())
                        .lpPolicyType(icrBuffer.getPolicyType())
                        .lpDocumentId(icrFile.getDocumentId())
                        .processFlag(false)
                        .entryDate(LocalDate.now())
                        .build());
            }

        }

        // Update ICRBuffer for future reference
        icrFileService.update(icrFile.getDocumentId(), icrFile);

        // Determine final queue first
        spouseActionCase.setInitialQueueId(editingService.determineFinalQueue(spouseEditResponses, icrBuffer.getDdApps().getTemplateName(), icrBuffer));

        // Call IMG2001 to get image case description and pass case description back to PL-CASE-DESCRIPTION
        // Instead call queue profile by id to get case description.
        var queueProfile = queueService.findById(spouseActionCase.getInitialQueueId());
        queueProfile.ifPresent(ekd0150Queue -> spouseActionCase.setCmFormattedName(ekd0150Queue.getCaseDescription()));

        // Populate AFB0660 table record by calling ADDHISC program, if member is auto issued
        if (isDataClean) {
            // populate AFB0660
            AFB0660 afb0660 = AFB0660.builder()
                    .policyId(spouseActionCase.getCmAccountNumber())
                    .caseId(spouseActionCase.getCaseId())
                    .caseType(spouseActionCase.getCmFormattedName() == null || spouseActionCase.getCmFormattedName().length() < 2
                            ? "11" : spouseActionCase.getCmFormattedName().substring(0, 2))
                    .fromQueue("AUTOISSUE")
                    .toQueue(spouseActionCase.getInitialQueueId())
                    .userId(authorizationHelper.getRequestRepId())
                    .arrivalDate(LocalDate.now())
                    .currentDate(LocalDate.now())
                    .currentTime(LocalTime.now())
                    .build();
            afb0660Service.save(afb0660);
        }

        if (!isExistingPolicy) {
            // Call EMPTYCASES program to create seven empty permanent cases
            caseService.generatePermanentCases(spouseActionCase.getCmAccountNumber());
        }
        // create action case
        var actionCase = caseService.createCase(spouseActionCase);
        spouseActionCase.setCreated(true);

        // map document to case
        var ekd310 = documentService.findByIdOrElseThrow(icrFile.getDocumentId());
        var ekd315 = EKD0315CaseDocument.builder()
                .caseId(actionCase.getCaseId())
                .documentId(icrFile.getDocumentId())
                .scanningUser(authorizationHelper.getRequestRepId())
                .build();
        ekd315.setScanningDateTime(ekd310.getScanningDateTime());
        caseDocumentService.insert(ekd315);

        // Populate TRNIDPOLR table record
        trnidpolrService.insert(TRNIDPOLR.builder()
                .tpTransactionId(icrBuffer.getDdApps().getTransactionId())
                .tpPolicyId(actionCase.getCmAccountNumber())
                .tpCaseId(actionCase.getCaseId())
                .tpSource(icrBuffer.getTransactionSource())
                .tpDateIn(LocalDate.now())
                .build());

        // If data is not clean, populate TMPCMTPF record and submit a job to call ADDCMTC program to populate F11 comments
        if (!isDataClean) {
            spouseEditResponses
                    .stream()
                    .filter(er -> StringUtils.isNotEmpty(er.getQueue()))
                    .forEach(er ->
                            {
                                String comment = null;
                                if (StringUtils.isNotEmpty(er.getCommentCode())) {
                                    var autoComment = autocmtpfService.findById(er.getCommentCode());
                                    if (autoComment.isPresent()) {
                                        comment = autoComment.get().getCommentText();
                                    }
                                }
                                if (StringUtils.isEmpty(comment)) {
                                    comment = StringUtils.isNotEmpty(er.getComment()) ? er.getComment() : er.getCommentCode();
                                }
                                caseCommentService.insert(
                                        EKD0352CaseComment.builder()
                                                .caseId(actionCase.getCaseId())
                                                .commentLines(Set.of(EKD0353CaseCommentLine.builder()
                                                        .commentSequence(0)
                                                        .commentLine(comment)
                                                        .build()))
                                                .build());
                            }
                    );
        }

        if (!isExistingPolicy) {
            // Populate EKDUSER record
            ekdUserService.insert(
                    spouseActionCase.getCmAccountNumber(),
                    icrBuffer.getDdApps().getSpouseSSN(),
                    icrBuffer.getDdApps().getSpouseLastName(),
                    icrBuffer.getDdApps().getSpouseFirstName(),
                    icrBuffer.getDdApps().getSpouseMiddleInitial(),
                    true
            );
        }
        // Update EMSITIFF record
        if (icrBuffer.isSpouseGoesToESP()) {
            emsiTiff.setPolicyId(spouseActionCase.getCmAccountNumber());
            // if member goes to ESP too then 3 else if only spouse goes to ESP then 2
            emsiTiff.setSendToEmsi("1".equals(emsiTiff.getSendToEmsi()) ? "3" : "2");
            // if spouse doesn't need EKG or Paramedi then 1 (Jet Issue) else 0 (Standard Issue)
            emsiTiff.setJtIssFlag1(!icrBuffer.isSpouseNeedEKG() && !icrBuffer.isSpouseNeedParamedi() ? "1" : "0");
            emsitiffService.save(emsiTiff);
        }
        return spouseActionCase;
    }

    private List<EditingResponse> editApplicationData(ICRFile icrFile) {
        var icrBuffer = icrFile.getIcrBuffer();
        var editingResponses = new ArrayList<EditingResponse>();
        // The first check to see if the agent is CAS or not
        editingResponses.add(editingService.editAgentCode(icrBuffer));
        // Edit member SSN so the dummy SSN is generated if needed.
        if (icrBuffer.getDdApps().getMemberCoverUnit() > 0) {
            editingService.editSSN(icrBuffer, icrBuffer.getDdApps().getMemberSSN(), EditingSubject.MEMBER);
        }
        // Edit spouse SSN so the dummy SSN is generated if needed.
        if (icrBuffer.getDdApps().getSpouseCoverUnit() > 0) {
            editingService.editSSN(icrBuffer, icrBuffer.getDdApps().getSpouseSSN(), EditingSubject.SPOUSE);
        }
        editingResponses.add(editingService.editContractState(icrBuffer));
        editingResponses.add(editingService.editGeneralNote(icrBuffer));
        editingResponses.add(editingService.editParamediOrderFlag(icrBuffer));
        editingResponses.add(editingService.editSOHInitialFlag(icrBuffer));
        editingResponses.add(editingService.editPolicyType(icrBuffer));
        editingResponses.add(editingService.editPayMode(icrBuffer));
        editingResponses.add(editingService.editAgentCode2(icrBuffer));
        editingResponses.add(editingService.editPremiumPaid(icrBuffer));
        editingResponses.add(editingService.editAffiliationCode(icrBuffer));
        editingResponses.add(editingService.editProcType(icrBuffer));
        editingResponses.add(editingService.editAgentSignFlag(icrBuffer));
        editingResponses.add(editingService.editAgentSignDate(icrBuffer));
        editingResponses.add(editingService.editAgentLevel(icrBuffer));
        editingResponses.add(editingService.editApplicationSpecifiedState(icrBuffer));
        editingResponses.add(editingService.editApplicantReplacementIndicator(icrBuffer));
        editingResponses.add(editingService.editNumberOfPages(icrBuffer, icrFile.getDocumentId()));
        editingResponses.add(editingService.editPremiumPaid(icrBuffer));
        editingResponses.add(editingService.editChildUnits(icrBuffer));
        editingResponses.add(editingService.editChildPremium(icrBuffer));
        editingResponses.add(editingService.editEmployerTaxId(icrBuffer));
        editingResponses.add(editingService.editTotalPremiumAmount(icrBuffer));
        editingResponses.add(editingService.editOwnerSSN(icrBuffer));
        editingResponses.add(editingService.editPayorSSN(icrBuffer));
        editingResponses.add(editingService.editApplicantReplacementIndicator(icrBuffer));
        editingResponses.add(editingService.editChildStatementOfHealth(icrBuffer));
        editingResponses.add(editingService.editBillDayEpayType(icrBuffer));

        return editingResponses;
    }

    private List<EditingResponse> editMemberData(ICRBuffer icrBuffer) {
        var editingResponses = new ArrayList<EditingResponse>();
        editingResponses.add(editingService.editSSN(icrBuffer, icrBuffer.getDdApps().getMemberSSN(), EditingSubject.MEMBER));
        editingResponses.add(editingService.editPreviousPoliciesStatus(icrBuffer, EditingSubject.MEMBER));
        editingResponses.add(editingService.editLastName(icrBuffer, icrBuffer.getDdApps().getMemberLastName(), EditingSubject.MEMBER));
        editingResponses.add(editingService.editFirstName(icrBuffer, icrBuffer.getDdApps().getMemberFirstName(), EditingSubject.MEMBER));
        editingResponses.add(editingService.editMiddleName(icrBuffer, icrBuffer.getDdApps().getMemberMiddleInitial(), EditingSubject.MEMBER));
        if(!tempcompmapService.getTemplatesExcludedFromMedicalUnderWriting().contains(icrBuffer.getDdApps().getTemplateName())){
            editingResponses.add(editingService.editMedicalRejection(icrBuffer, icrBuffer.getDdApps().getMemberSSN(), EditingSubject.MEMBER));
            editingResponses.add(editingService.editMedicalUnderwriting(icrBuffer,
                    icrBuffer.getDdApps().getMemberSSN(),
                    icrBuffer.getDdApps().getMemberDateOfBirth(),
                    icrBuffer.getDdApps().getMemberSignDate(),
                    icrBuffer.getDdApps().getMemberCoverUnit(),
                    EditingSubject.MEMBER));
        }

        editingResponses.add(editingService.editHeightWeight(icrBuffer,
                icrBuffer.getDdApps().getMemberHeight(),
                icrBuffer.getDdApps().getMemberWeight(),
                icrBuffer.getDdApps().getMemberSex(),
                icrBuffer.getDdApps().getMemberDateOfBirth(),
                icrBuffer.getDdApps().getMemberSignDate(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editInsurerEligibility(icrBuffer));
        editingResponses.add(editingService.editInsurerDutyStatus(icrBuffer));
        editingResponses.add(editingService.editInsurerRank(icrBuffer));
        editingResponses.add(editingService.editDateOfBirth(icrBuffer,
                icrBuffer.getDdApps().getMemberDateOfBirth(),
                icrBuffer.getDdApps().getMemberSignDate(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editSex(icrBuffer,
                icrBuffer.getDdApps().getMemberSex(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editSmokerFlag(icrBuffer,
                icrBuffer.getDdApps().getMemberSmoker(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editAddressLine1(icrBuffer,
                icrBuffer.getDdApps().getMemberAddress1(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editAddressLine2(icrBuffer,
                icrBuffer.getDdApps().getMemberAddress2(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editCity(icrBuffer,
                icrBuffer.getDdApps().getMemberCity(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editState(icrBuffer,
                icrBuffer.getDdApps().getMemberState(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editZipCode(icrBuffer,
                icrBuffer.getDdApps().getMemberZip(),
                icrBuffer.getDdApps().getMemberCity(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editEmailAddress(icrBuffer,
                icrBuffer.getDdApps().getMemberEmail(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editDayPhoneSecondPhone(icrBuffer,
                icrBuffer.getDdApps().getMemberDPhone(),
                icrBuffer.getDdApps().getMemberEPhone(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editCoverageUnit(icrBuffer,
                icrBuffer.getDdApps().getMemberCoverUnit(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editPremiumAmount(icrBuffer));
        editingResponses.add(editingService.editStatementOfHealth(icrBuffer,
                icrBuffer.getDdApps().getMemberHq(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editApplicantSignFlag(icrBuffer,
                icrBuffer.getDdApps().getMemberSignFlag(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editApplicantSignDate(icrBuffer,
                icrBuffer.getDdApps().getMemberSignDate(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editMemberOccupationClass(icrBuffer));
        editingResponses.add(editingService.editMemberEmployeeClassCode(icrBuffer));
        // Relation code validation should be executed first
        // so the if its SPS the values get filled from other fields before editing.
        editingResponses.add(editingService.editBeneficialRelationCode(icrBuffer,
                icrBuffer.getDdApps().getMemberBeneficiary1Relation(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editBeneficialLastName(icrBuffer,
                icrBuffer.getDdApps().getMemberBeneficiary1LastName(),
                icrBuffer.getDdApps().getMemberBeneficiary1SSN(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editBeneficialFirstName(icrBuffer,
                icrBuffer.getDdApps().getMemberBeneficiary1FirstName(),
                icrBuffer.getDdApps().getMemberBeneficiary1SSN(),
                EditingSubject.MEMBER));
        editingResponses.add(editingService.editBeneficialSSN(icrBuffer,
                icrBuffer.getDdApps().getMemberBeneficiary1SSN(),
                EditingSubject.MEMBER));

        return editingResponses;
    }

    private List<EditingResponse> editSpouseData(ICRBuffer icrBuffer) {
        var editingResponses = new ArrayList<EditingResponse>();
        editingResponses.add(editingService.editSSN(icrBuffer, icrBuffer.getDdApps().getSpouseSSN(), EditingSubject.SPOUSE));
        editingResponses.add(editingService.editPreviousPoliciesStatus(icrBuffer, EditingSubject.SPOUSE));
        editingResponses.add(editingService.editLastName(icrBuffer, icrBuffer.getDdApps().getSpouseLastName(), EditingSubject.SPOUSE));
        editingResponses.add(editingService.editFirstName(icrBuffer, icrBuffer.getDdApps().getSpouseFirstName(), EditingSubject.SPOUSE));
        editingResponses.add(editingService.editMiddleName(icrBuffer, icrBuffer.getDdApps().getSpouseMiddleInitial(), EditingSubject.SPOUSE));
        if(!tempcompmapService.getTemplatesExcludedFromMedicalUnderWriting().contains(icrBuffer.getDdApps().getTemplateName())){
            editingResponses.add(editingService.editMedicalRejection(icrBuffer, icrBuffer.getDdApps().getSpouseSSN(), EditingSubject.SPOUSE));
            editingResponses.add(editingService.editMedicalUnderwriting(icrBuffer,
                    icrBuffer.getDdApps().getSpouseSSN(),
                    icrBuffer.getDdApps().getSpouseDateOfBirth(),
                    icrBuffer.getDdApps().getSpouseSignDate(),
                    icrBuffer.getDdApps().getSpouseCoverUnit(),
                    EditingSubject.SPOUSE));
        }
        editingResponses.add(editingService.editHeightWeight(icrBuffer,
                icrBuffer.getDdApps().getSpouseHeight(),
                icrBuffer.getDdApps().getSpouseWeight(),
                icrBuffer.getDdApps().getSpouseSex(),
                icrBuffer.getDdApps().getSpouseDateOfBirth(),
                icrBuffer.getDdApps().getSpouseSignDate(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editDateOfBirth(icrBuffer,
                icrBuffer.getDdApps().getSpouseDateOfBirth(),
                icrBuffer.getDdApps().getSpouseSignDate(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editSex(icrBuffer,
                icrBuffer.getDdApps().getSpouseSex(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editSmokerFlag(icrBuffer,
                icrBuffer.getDdApps().getSpouseSmoker(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editCoverageUnit(icrBuffer,
                icrBuffer.getDdApps().getSpouseCoverUnit(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editPremiumAmount(icrBuffer));
        editingResponses.add(editingService.editStatementOfHealth(icrBuffer,
                icrBuffer.getDdApps().getSpouseHq(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editApplicantSignFlag(icrBuffer,
                icrBuffer.getDdApps().getSpouseSignFlag(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editApplicantSignDate(icrBuffer,
                icrBuffer.getDdApps().getSpouseSignDate(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editSpouseEmployeeClassCode(icrBuffer));
        // Relation code validation should be executed first
        // so the if its SPS the values get filled from other fields before editing.
        editingResponses.add(editingService.editBeneficialRelationCode(icrBuffer,
                icrBuffer.getDdApps().getSpouseBeneficiaryRelation(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editBeneficialLastName(icrBuffer,
                icrBuffer.getDdApps().getSpouseBeneficiaryLastName(),
                icrBuffer.getDdApps().getSpouseBeneficiarySSN(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editBeneficialFirstName(icrBuffer,
                icrBuffer.getDdApps().getSpouseBeneficiaryFirstName(),
                icrBuffer.getDdApps().getSpouseBeneficiarySSN(),
                EditingSubject.SPOUSE));
        editingResponses.add(editingService.editBeneficialSSN(icrBuffer,
                icrBuffer.getDdApps().getSpouseBeneficiarySSN(),
                EditingSubject.SPOUSE));


        // Post editing rules
        // if the template is one of APPSNG0702, APPSSS0810, APPSCO0810 or APPSSS0818
        if (Template.valueOf(icrBuffer.getDdApps().getTemplateName()).isTermedtdta()) {
            if (editingResponses.stream().filter(editingResponse -> "001A".equals(editingResponse.getCommentCode())).count() == editingResponses.size()) {
                editingResponses.clear();
            }
        }
        return editingResponses;
    }

    private String getAutoIssueFlag(boolean isDataClean, boolean isAutoDecline, String payMode, Template template) {
        var autoIssue = "R";
        if (isDataClean) {
            if (
                    // P from ddapp is converted to B and L from ddapp is converted to E
                    // 3 is for allotment always.
                    List.of("B", "E", "3").contains(payMode)
            ) {
                autoIssue = "E";
            } else {
                autoIssue = "I";
            }
        } else {
            if (isAutoDecline) {
                autoIssue = "D";
            }
        }
        return autoIssue;
    }

    private void loadExistingFile(ICRBuffer icrBuffer, Long ssn, EditingSubject subject) {
        if (ssn != null && ssn.toString().length() == 9) {
            var partySearch = new PartySearch("N", "S", ssn.toString(), UUID.randomUUID().toString(), "string",
                    lifeProCoderId);
            var partySearchBaseRes = lifeProApiService.partySearchDetails(new PartySearchBaseReq(partySearch));
            if (partySearchBaseRes.getPartySearchResult() != null
                    && partySearchBaseRes.getPartySearchResult().getPartySearchRes() != null
                    && !partySearchBaseRes.getPartySearchResult().getPartySearchRes().isEmpty()) {
                var nameId = partySearchBaseRes.getPartySearchResult().getPartySearchRes().get(0).getName_id();
                var partyRelationshipReq = new PartyRelationshipReq(Integer.parseInt(nameId)
                        , UUID.randomUUID().toString(), "string", lifeProCoderId);
                var partyRelationshipsBaseRes = lifeProApiService.PartyRelationships(new PartyRelationshipBaseReq(partyRelationshipReq));
                if (partyRelationshipsBaseRes.getGetPartyRelationshipsResult() != null
                        && partyRelationshipsBaseRes.getGetPartyRelationshipsResult().getPartyRelationshipsResp() != null
                ) {
                    var memberTerminationWithEX = 0;
                    var spouseTerminationWithEX = 0;
                    int insurerLatestIteratedPolicyDate = 0;
                    int spouseLatestIteratedPolicyDate = 0;
                    int insurerOldestIteratedPolicyDate = Integer.MAX_VALUE;
                    int spouseOldestIteratedPolicyDate = Integer.MAX_VALUE;
                    int totalCoverage = 0;
                    int productCoverage = 0;

                    var filteredRelationships = partyRelationshipsBaseRes.getGetPartyRelationshipsResult().getPartyRelationshipsResp().stream()
                            .filter(p -> !p.getProductCode().matches("^(CIGNA.*|FC1|FF.*|GFRE.*|GSPWL.*|ISP.*|NG.*|SSLI.*)$")).collect(Collectors.toUnmodifiableList());
                    for (var rel : filteredRelationships) {

                        if (!"IN".equals(rel.getRelateCode())) {
                            continue;
                        }

                        GetBenefitSummaryRes benefit = getBenefit(rel.getCompanyCode(), rel.getPolicyNumber());
                        if (benefit != null) {
                            // The benefit is for insurer himself/herself.
                            if (List.of("BA", "BF").contains(benefit.getBenefit().getBenefitType())) {
                                // Add up total coverage from existing file.
                                totalCoverage += benefit.getFaceAmount();

                                // Add up product coverage from existing file.
                                if (!rel.getProductCode().matches("^(LT-121SP|LT-121FP)$")) {
                                    productCoverage += benefit.getFaceAmount();
                                }

                                if ("S".equals(benefit.getStatus().getStatusCode())
                                        && "DP".equals(benefit.getStatus().getStatusReason())) {
                                    if (EditingSubject.MEMBER.equals(subject)) {
                                        icrBuffer.setMemberSuspendedWithDPFound(true);
                                    } else {
                                        icrBuffer.setSpouseSuspendedWithDPFound(true);
                                    }
                                }
                                if ("T".equals(benefit.getStatus().getStatusCode())
                                        && "DC".equals(benefit.getStatus().getStatusReason())) {
                                    if (EditingSubject.MEMBER.equals(subject)) {
                                        icrBuffer.setMemberTerminatedWithDCFound(true);
                                    } else {
                                        icrBuffer.setSpouseTerminatedWithDCFound(true);
                                    }
                                }
                                if (rel.getProductCode().startsWith("GF")
                                        && List.of("P", "A").contains(rel.getContractCode())) {
                                    if (EditingSubject.MEMBER.equals(subject)) {
                                        icrBuffer.setMemberPendingOrApprovedGroupFreeFound(true);
                                    } else {
                                        icrBuffer.setSpousePendingOrApprovedGroupFreeFound(true);
                                    }
                                }
                                if ("T".equals(benefit.getStatus().getStatusCode())
                                        && "EX".equals(benefit.getStatus().getStatusReason())) {
                                    if (EditingSubject.MEMBER.equals(subject)) {
                                        memberTerminationWithEX++;
                                    } else {
                                        spouseTerminationWithEX++;
                                    }
                                }
                                if (EditingSubject.MEMBER.equals(subject)) {
                                    // set only for latest policy
                                    if (insurerLatestIteratedPolicyDate < rel.getIssueDate()) {
                                        insurerLatestIteratedPolicyDate = rel.getIssueDate();
                                        if (benefit.getDateOfBirth() > 0) {
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                                            icrBuffer.setMemberPreviousDOB(LocalDate.parse(String.valueOf(benefit.getDateOfBirth()), formatter));
                                        }
                                        icrBuffer.setMemberPreviousLOB(rel.getLineOfBusiness());
                                        icrBuffer.setMemberPreviousSex(benefit.getSexCode());
                                        icrBuffer.setMemberPreviousSmokerClass(benefit.getUnderwritingClass());
                                    }
                                    // extract the oldest policy
                                    if (insurerOldestIteratedPolicyDate > rel.getIssueDate()) {
                                        insurerOldestIteratedPolicyDate = rel.getIssueDate();
                                        icrBuffer.setMemberOldestPolicyId(rel.getPolicyNumber());
                                    }
                                } else {
                                    // set only for latest policy
                                    if (spouseLatestIteratedPolicyDate < rel.getIssueDate()) {
                                        spouseLatestIteratedPolicyDate = rel.getIssueDate();
                                        if (benefit.getDateOfBirth() > 0) {
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                                            icrBuffer.setSpousePreviousDOB(LocalDate.parse(String.valueOf(benefit.getDateOfBirth()), formatter));
                                        }
                                        icrBuffer.setSpousePreviousSex(benefit.getSexCode());
                                        icrBuffer.setSpousePreviousSmokerClass(benefit.getUnderwritingClass());
                                    }
                                    if (spouseOldestIteratedPolicyDate > rel.getIssueDate()) {
                                        spouseOldestIteratedPolicyDate = rel.getIssueDate();
                                        icrBuffer.setSpouseOldestPolicyId(rel.getPolicyNumber());
                                    }
                                }
                            } else if ("OR".equals(benefit.getBenefit().getBenefitType())) {
                                // for child coverage.
                                var childCoverage = lifeProApiService.getChildCoverage(rel.getCompanyCode(), rel.getPolicyNumber(), benefit);
                                if (childCoverage != null) {
                                    // numberOfunits
                                    if (EditingSubject.MEMBER.equals(subject)) {
                                        icrBuffer.setMemberExistingChildUnit(childCoverage.intValue());
                                    } else {
                                        icrBuffer.setSpouseExistingChildUnit(childCoverage.intValue());
                                    }
                                }
                            }
                        }
                    }
                    if (memberTerminationWithEX >= 2) {
                        icrBuffer.setMemberTerminatedWithEXFound(true);
                    }
                    if (spouseTerminationWithEX >= 2) {
                        icrBuffer.setSpouseTerminatedWithEXFound(true);
                    }
                    if (EditingSubject.MEMBER.equals(subject)) {
                        icrBuffer.setMemberProductCoverage(productCoverage);
                        icrBuffer.setMemberTotalCoverage(totalCoverage);
                    } else {
                        icrBuffer.setSpouseProductCoverage(productCoverage);
                        icrBuffer.setSpouseTotalCoverage(totalCoverage);
                    }
                }
            }

        }
    }

    private GetBenefitSummaryRes getBenefit(String companyCode, String policyNumber) {
        var getBenefitSummaryReq = new GetBenefitSummaryReq(companyCode, policyNumber, "Y",
                UUID.randomUUID().toString(), "string", lifeProCoderId);
        var benefitSummary = lifeProApiService.getBenefitSummary(new GetBenefitSummaryBaseReq(getBenefitSummaryReq));
        if (benefitSummary.getGetBenefitSummaryResult() != null
                && benefitSummary.getGetBenefitSummaryResult().getGetBenefitSummaryRes() != null
                && !benefitSummary.getGetBenefitSummaryResult().getGetBenefitSummaryRes().isEmpty()) {
            return benefitSummary.getGetBenefitSummaryResult().getGetBenefitSummaryRes().get(0);
        }
        return null;
    }

    private void sendFailureEmail(String transactionId, String errorMessage) {
        Optional<EmailTemplate> emailTemplateOpt = emailTemplateRepository.findByTemplateNameAndIsActive(
                ApplicationConstants.DDPROCESS_FAILURE_EMAIL_TEMPLATE_NAME, true);
        if (emailTemplateOpt.isPresent()) {

            EmailTemplate emailTemplate = emailTemplateOpt.get();
            String ccRecipients = emailTemplate.getEmailCC() == null ? "" : emailTemplate.getEmailCC();
            String bccRecipients = emailTemplate.getEmailBcc() == null ? "" : emailTemplate.getEmailBcc();

            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("transactionId", transactionId);
            templateModel.put("errorMessage", errorMessage);
            Context thymeleafContext = new Context();
            thymeleafContext.setVariables(templateModel);
            String htmlBody = thymeleafTemplateEngine.process(
                    ApplicationConstants.DDPROCESS_FAILURE_EMAIL_TEMPLATE_NAME, thymeleafContext);

            // Send Email With The Highest Priority
            emailUtility.sendHtmlMessage(emailTemplate.getEmailSubject(), emailTemplate.getEmailTo(), ccRecipients,
                    bccRecipients, htmlBody, 1);
        } else {
            log.error("Email template {} not found ",
                    ApplicationConstants.DDPROCESS_FAILURE_EMAIL_TEMPLATE_NAME);
        }
    }

    private CaseCreateReqExtended mapDocumentToPermanentCase(CaseCreateReqExtended caseReq, String documentId) {
        // find the permanent case
        var cases = caseService.findByCmAccountNumber(caseReq.getCmAccountNumber());
        for (var theCase : cases) {
            if ("01 APPLICATIONS".equals(theCase.getCmFormattedName())) {
                caseReq.setCaseId(theCase.getCaseId());
                break;
            }
        }
        if (caseReq.getCaseId() == null) {
            throw new DomainException(HttpStatus.NOT_FOUND, "EKDDDP200"
                    , String.format("Permanent case for the existing policy (%s) not found.", caseReq.getCmAccountNumber()));
        }
        // map document to the permanent case.
        var ekd310 = documentService.findByIdOrElseThrow(documentId);
        var ekd315 = EKD0315CaseDocument.builder()
                .caseId(caseReq.getCaseId())
                .documentId(documentId)
                .scanningUser(authorizationHelper.getRequestRepId())
                .build();
        ekd315.setScanningDateTime(ekd310.getScanningDateTime());
        caseDocumentService.insert(ekd315);
        return caseReq;
    }

}
