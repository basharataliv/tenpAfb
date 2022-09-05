package com.afba.imageplus.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.ANNSTATEFileDto;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.IMPANNTIFService;
import com.afba.imageplus.utilities.CsvFileReaderUtil;

@Service
public class IMPANNTIFServiceImpl implements IMPANNTIFService {
    Logger logger = LoggerFactory.getLogger(IMPANNTIFServiceImpl.class);

    private final DocumentService documentService;
    private final CaseService caseService;
    private final CaseDocumentService caseDocumentService;

    public IMPANNTIFServiceImpl(DocumentService documentService,
             CaseService caseService, CaseDocumentService caseDocumentService) {

        this.documentService = documentService;
        this.caseService = caseService;
        this.caseDocumentService = caseDocumentService;

    }

    private final String INPUT_FILE = "ANNSTATE.csv";
    private final String DOC_TYPE = "ANNSTMNT";
    private final String DESCRIPTION = "LP Annual Statemnt";
    private static final String CASE_DESC_01 = "06 Accounting";
    private static final String CASE_DESC_81 = "81 IMAGE ACTION";
    @Value("${afba.exstream.job.base.dir}")
    private String jobBaseDir;

    @Override
    public void IMPANNTIFProcessing() {
        try {
            logger.info("IMPANNTIF processing Started ");

            List<ANNSTATEFileDto> list = CsvFileReaderUtil.readCsv(ANNSTATEFileDto.class,
                    new FileInputStream(new File(jobBaseDir + INPUT_FILE)));

            logger.info("Input file records are " + list.size());

            for (ANNSTATEFileDto dto : list) {

                if (dto.getPolicyNo() != null) {

                    Path filePath = Path.of(jobBaseDir + dto.getFileName());

                    List<EKD0350Case> cases = caseService.findByCmAccountNumberAndName(dto.getPolicyNo(), CASE_DESC_01,
                            CASE_DESC_81);
                    try {
                        if (filePath != null && Files.exists(filePath)) {

                            EKD0310Document doc = importDocument(filePath, dto);

                            if (!cases.isEmpty()) {

                                cases.forEach(cas -> copyaDoc(cas.getCaseId(), doc.getDocumentId()));

                            } else {

                                CaseResponse caseRes = DEFFOLD(dto);
                                copyaDoc(caseRes.getCaseId(), doc.getDocumentId());
                            }
                        }

                    } catch (Exception e) {
                        logger.error("error in IMPANNTIF ", e);
                    }
                }
            }

        } catch (

        Exception e) {
            logger.error("error in main IMPANNTIF ", e);
        }
    }

    private EKD0310Document importDocument(Path path, ANNSTATEFileDto dto) {
        try {

            EKD0310Document document = EKD0310Document.builder()
                    .doc(new MockMultipartFile(path.getFileName().toString(), path.getFileName().toString(),
                            MediaType.APPLICATION_OCTET_STREAM_VALUE, new FileInputStream(path.toString())))
                    .documentType(DOC_TYPE).docPage(dto.getPageNo()).reqListDescription(DESCRIPTION).build();
            return documentService.importDocument(document);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DomainException(HttpStatus.BAD_REQUEST, "EKDFIL404", "Unable read file");
        }
    }

    private EKD0315CaseDocument copyaDoc(String caseId, String docId) {
        try {
            EKD0315CaseDocument document = EKD0315CaseDocument.builder().caseId(caseId).documentId(docId).build();

            return caseDocumentService.insert(document);
        } catch (Exception e) {
            throw new DomainException(HttpStatus.NOT_FOUND, "EKD315000", "copy doc fail");
        }
    }

    private CaseResponse DEFFOLD(ANNSTATEFileDto dto) {
        try {

            logger.info("new case create with policy number " + dto.getPolicyNo());
            CaseCreateReq cas = CaseCreateReq.builder().cmAccountNumber(dto.getPolicyNo())
                    .cmFormattedName("81 IMAGE ACTION").initialQueueId("IMAGE").build();
            // To keep the old date and time fields populated as well
            cas.setScanningDateTime(LocalDateTime.now());
            cas.setLastUpdateDateTime(LocalDateTime.now());
            return caseService.createCase(cas);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DomainException(HttpStatus.NOT_FOUND, "EKD350000", "create case fail");
        }
    }

}
