package com.afba.imageplus.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.BILLTIFFFileDto;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.IMPBILLTIFService;
import com.afba.imageplus.utilities.CsvFileReaderUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IMPBILLTIFServiceImpl implements IMPBILLTIFService {

    private final DocumentService documentService;
    private final CaseService caseService;
    private final CaseDocumentService caseDocumentService;

    public IMPBILLTIFServiceImpl(DocumentService documentService, CaseService caseService,
            CaseDocumentService caseDocumentService) {

        this.documentService = documentService;
        this.caseService = caseService;
        this.caseDocumentService = caseDocumentService;
    }

    private static final String INPUT_FILE = "BILLTIFF.CSV";
    private static final String OUTPUT_FILE = "BILLTFFO.CSV";
    private static final String CSV_SEPARATOR = ",";

    private static final String CASE_DESC_06 = "06 Accounting";
    private static final String CASE_DESC_81 = "81 IMAGE ACTION";
    private static final String DOC_TYPE = "LPBILL";

    @Value("${afba.exstream.job.base.dir}")
    private String jobBaseDir;

    @Override
    public void impbilltifProcessing() {
        log.info("IMPBILLTIF - processing Started");

        // Flush previous output file
        deleteOutputFile();

        // 1. Reading input file for IMPBILLTIF
        List<BILLTIFFFileDto> fileRecords = processInputFile();

        // Start processing of records
        for (BILLTIFFFileDto dto : fileRecords) {
            if (dto.getPolicyNumber() != null) {
                try {

                    // 3. Check if document present in dir
                    Path filePath = Path.of(jobBaseDir + dto.getTiffName());

                    // 4. Find 01 case
                    List<EKD0350Case> cases = caseService.findByCmAccountNumberAndName(dto.getPolicyNumber(),
                            CASE_DESC_06, CASE_DESC_81);

                    if (filePath != null && Files.exists(filePath)) {
                        // 5. IMPORT the document to image plus
                        EKD0310Document doc = importDocument(filePath, dto, DOC_TYPE);

                        // 6. If 01 or 81 IMAGE ACTION case not found create new 81 IMAGE ACTION
                        if (cases.isEmpty()) {

                            CaseResponse caseRes = deffold(dto);
                            copyADoc(caseRes.getCaseId(), doc.getDocumentId());
                        } else {
                            cases.forEach(cas -> copyADoc(cas.getCaseId(), doc.getDocumentId()));
                        }
                        // 7. Write data to output file
                        writeDataOnFile(dto, doc.getDocumentId());

                    } else {
                        log.error("IMPBILLTIF - Record document not found {} ", dto.getTiffName());
                    }
                } catch (Exception e) {

                    log.error("IMPBILLTIF - Error processing record ", dto.getPolicyNumber(), e);
                }
            }
        }
    }

    private EKD0310Document importDocument(Path filePath, BILLTIFFFileDto dto, String docType) {
        try {
            EKD0310Document document = EKD0310Document.builder()
                    .doc(new MockMultipartFile(filePath.getFileName().toString(), filePath.getFileName().toString(),
                            MediaType.APPLICATION_OCTET_STREAM_VALUE, new FileInputStream(filePath.toString())))
                    .documentType(docType).docPage(dto.getPageNumber())
                    .reqListDescription("3".equals(dto.getBillIndicator()) ? "Lapse Letter" : "LifePro Bill").build();
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

    private CaseResponse deffold(BILLTIFFFileDto dto) {
        try {
            log.info("IMPBILLTIF - New case create with policy number " + dto.getPolicyNumber());
            CaseCreateReq cas = CaseCreateReq.builder().cmAccountNumber(dto.getPolicyNumber())
                    .cmFormattedName(CASE_DESC_81).initialQueueId("IMAGE").scanningDate(LocalDate.now())
                    .scanningTime(LocalTime.now()).build();

            return caseService.createCase(cas);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DomainException(HttpStatus.NOT_FOUND, "EKD350000", "Create case failed");
        }
    }

    /**
     * @return returns true if file is written successfully
     */
    private Boolean writeDataOnFile(BILLTIFFFileDto dto, String docId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(jobBaseDir + OUTPUT_FILE, true))) {
            writer.append(StringUtils.rightPad(dto.getPolicyNumber(), 12)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getPageNumber().toString(), 1)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getTiffName(), 12)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getBillIndicator(), 1)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getCompanyCode(), 2)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getGroupBcn(), 10)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getPolicyBcn(), 10)).append(CSV_SEPARATOR)
                    .append(dto.getRecordKey().toString()).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getBatchId(), 12)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(docId, 12)).append(System.lineSeparator());
            return true;

        } catch (Exception ex) {
            log.error("Exception in writing output file", ex);
            return false;
        }
    }

    /**
     * @return returns true if is file deleted successfully
     */
    private Boolean deleteOutputFile() {
        try {
            log.info("Deleting previously processed {} file {}", OUTPUT_FILE, jobBaseDir + OUTPUT_FILE);
            return Files.deleteIfExists(Path.of(jobBaseDir + OUTPUT_FILE));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * @return returns list of records read from input csv. Null if error occurred
     */
    private List<BILLTIFFFileDto> processInputFile() {
        List<BILLTIFFFileDto> list = new ArrayList<>();
        try {
            list = CsvFileReaderUtil.readCsv(BILLTIFFFileDto.class,
                    new FileInputStream(new File(jobBaseDir + INPUT_FILE)));
            log.info("IMPBILLTIF - Input file records are " + list.size());
        } catch (Exception e) {
            log.error("IMPBILLTIF - Error in reading input file ", e);
        }
        return list;
    }
}
