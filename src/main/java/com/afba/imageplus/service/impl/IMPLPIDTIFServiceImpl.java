package com.afba.imageplus.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.LPISSDOCFileDto;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.IMPLPIDTIFService;
import com.afba.imageplus.utilities.CsvFileReaderUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IMPLPIDTIFServiceImpl implements IMPLPIDTIFService {

    private final DocumentService documentService;
    private final CaseService caseService;
    private final CaseDocumentService caseDocumentService;

    public IMPLPIDTIFServiceImpl(DocumentService documentService, CaseService caseService,
            CaseDocumentService caseDocumentService) {

        this.documentService = documentService;
        this.caseService = caseService;
        this.caseDocumentService = caseDocumentService;
    }

    private static final String INPUT_FILE = "LPISSDOC.CSV";
    private static final String CASE_DESC_01 = "01 APPLICATIONS";
    private static final String CASE_DESC_81 = "81 IMAGE ACTION";
    private static final String DOC_TYPE = "ISSUEDOC";
    @Value("${afba.exstream.job.base.dir}")
    private String jobBaseDir;

    @Override
    public void implpidtifProcessing() {

        log.info("IMPLPIDTIF - processing Started");

        // 1. Reading input file for IMPLPIDTIF
        List<LPISSDOCFileDto> fileRecords = processInputFile();

        // Start processing of records
        for (LPISSDOCFileDto dto : fileRecords) {
            if (dto.getPolicyNo() != null) {
                try {

                    // 2. Check if document present in dir
                    Path filePath = Path.of(jobBaseDir + dto.getFileName());

                    // 3. Find 01 case
                    List<EKD0350Case> cases = caseService.findByCmAccountNumberAndName(dto.getPolicyNo(), CASE_DESC_01,
                            CASE_DESC_81);

                    if (filePath != null && Files.exists(filePath)) {
                        // 4. IMPORT the document to image plus
                        EKD0310Document doc = importDocument(filePath, dto, DOC_TYPE);

                        // 5. If 01 or 81 IMAGE ACTION case not found create new 81 IMAGE ACTION
                        if (cases.isEmpty()) {

                            CaseResponse caseRes = deffold(dto);
                            copyADoc(caseRes.getCaseId(), doc.getDocumentId());
                        } else {
                            cases.forEach(cas -> copyADoc(cas.getCaseId(), doc.getDocumentId()));
                        }
                    }
                } catch (Exception e) {

                    log.error("IMPLPIDTIF - Error processing record ", dto.getPolicyNo(), e);
                }
            }
        }
    }

    private EKD0310Document importDocument(Path filePath, LPISSDOCFileDto dto, String docType) {
        try {
            EKD0310Document document = EKD0310Document.builder()
                    .doc(new MockMultipartFile(filePath.getFileName().toString(), filePath.getFileName().toString(),
                            MediaType.APPLICATION_OCTET_STREAM_VALUE, new FileInputStream(filePath.toString())))
                    .documentType(docType).docPage(dto.getPageNo())
                    .reqListDescription(String.format("%s Issue Document", dto.getPolType())).build();
            return documentService.importDocument(document);
        } catch (IOException e) {
            throw new DomainException(HttpStatus.BAD_REQUEST, "EKDFIL404", "Document import failed");
        }
    }

    private EKD0315CaseDocument copyADoc(String caseId, String docId) {
        try {
            EKD0315CaseDocument document = EKD0315CaseDocument.builder().caseId(caseId).documentId(docId).build();

            return caseDocumentService.insert(document);
        } catch (Exception e) {
            throw new DomainException(HttpStatus.NOT_FOUND, "EKD315000", "Copy doc failed");
        }
    }

    private CaseResponse deffold(LPISSDOCFileDto dto) {
        try {
            log.info("IMPLPIDTIF - New case create with policy number " + dto.getPolicyNo());
            CaseCreateReq cas = CaseCreateReq.builder().cmAccountNumber(dto.getPolicyNo()).cmFormattedName(CASE_DESC_81)
                    .initialQueueId("IMAGE").build();
            cas.setScanningDateTime(LocalDateTime.now());
            cas.setLastUpdateDateTime(LocalDateTime.now());
            return caseService.createCase(cas);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DomainException(HttpStatus.NOT_FOUND, "EKD350000", "Create case failed");
        }
    }

    /**
     * @return returns list of records read from input csv. Null if error occurred
     */
    private List<LPISSDOCFileDto> processInputFile() {
        List<LPISSDOCFileDto> list = new ArrayList<>();
        try {
            list = CsvFileReaderUtil.readCsv(LPISSDOCFileDto.class,
                    new FileInputStream(new File(jobBaseDir + INPUT_FILE)));
            log.info("IMPLPIDTIF - Input file records are " + list.size());
        } catch (Exception e) {
            log.error("IMPLPIDTIF - Error in reading input file ", e);
        }
        return list;
    }
}
