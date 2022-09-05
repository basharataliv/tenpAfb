package com.afba.imageplus.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.CORRTIFFFileDto;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.model.sqlserver.DOCMOVE;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.DOCMOVEService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.IMPCORRTIFService;
import com.afba.imageplus.utilities.CsvFileReaderUtil;

@Service
public class IMPCORRTIFServiceImpl implements IMPCORRTIFService {
    Logger logger = LoggerFactory.getLogger(IMPCORRTIFServiceImpl.class);

    private final DocumentService documentService;
    private final CaseService caseService;
    private final CaseDocumentService caseDocumentService;
    private final DOCMOVEService dOCMOVEService;

    public IMPCORRTIFServiceImpl(DocumentService documentService, CaseService caseService,
            CaseDocumentService caseDocumentService,
            DOCMOVEService dOCMOVEService) {

        this.documentService = documentService;
        this.caseService = caseService;
        this.caseDocumentService = caseDocumentService;
        this.dOCMOVEService = dOCMOVEService;
    }

    private final String INPUT_FILE = "CORRTIFF.txt";
    private final String OUTPUT_FILE = "CORRTIFF1.txt";
    private final String CSV_SEPARATOR = ",";
    private static final String CASE_DESC_81 = "81 IMAGE ACTION";
    @Value("${afba.exstream.job.base.dir}")
    private String jobBaseDir;

    @Override
    public void impcorrtifProcessing() {
        try {
            logger.info("CORIMGPF processing Started ");
            deleteCORRTIFF1();

            List<CORRTIFFFileDto> list = CsvFileReaderUtil.readCsv(CORRTIFFFileDto.class,
                    new FileInputStream(new File(jobBaseDir + INPUT_FILE)));

            logger.info("Input file records are " + list.size());

            for (CORRTIFFFileDto dto : list) {
                if (dto.getPolicyNo() != null) {
                    Optional<DOCMOVE> move = dOCMOVEService.findById(dto.getDocType());
                    String caseDes = "";
                    if (move.isPresent()) {
                        caseDes = move.get().getCaseNumber();
                    } else {
                        caseDes = "01";
                    }

                    Path filePath = Path.of(jobBaseDir + dto.getFileName());

                    List<EKD0350Case> cases = caseService.findByCmAccountNumberAndName(dto.getPolicyNo(), caseDes,
                            CASE_DESC_81);
                    try {
                        if (filePath != null && Files.exists(filePath)) {

                            if (!cases.isEmpty()) {

                                EKD0310Document doc = importDocument(filePath, dto, "LPLTR");
                                cases.forEach(cas -> copyaDoc(cas.getCaseId(), doc.getDocumentId()));
                                writeDataOnFile(dto, doc.getDocumentId());

                            } else {

                                CaseResponse caseRes = DEFFOLD(dto);
                                EKD0310Document doc = importDocument(filePath, dto, "LPBILL");
                                copyaDoc(caseRes.getCaseId(), doc.getDocumentId());
                                writeDataOnFile(dto, doc.getDocumentId());
                            }
                        }

                    } catch (Exception e) {
                        logger.error("error in CORIMGPF ", e);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("error in main CORIMGPF ", e);
        }
    }



    private EKD0310Document importDocument(Path path, CORRTIFFFileDto dto, String docType) {
        try {

            EKD0310Document document = EKD0310Document.builder()
                    .doc(new MockMultipartFile(path.getFileName().toString(), path.getFileName().toString(),
                            MediaType.APPLICATION_OCTET_STREAM_VALUE, new FileInputStream(path.toString())))
                    .documentType(docType).docPage(dto.getPageNo()).reqListDescription(dto.getDescription()).build();

            return documentService.importDocument(document);

        } catch (IOException e) {
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

    private CaseResponse DEFFOLD(CORRTIFFFileDto dto) {
        try {

            logger.info("new case create with ploy number " + dto.getPolicyNo());

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

    private void writeDataOnFile(CORRTIFFFileDto dto, String docId) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(jobBaseDir + OUTPUT_FILE, true))) {

            writer.append(StringUtils.rightPad(dto.getPolicyNo(), 12)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getPageNo() + "", 2)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getFileName(), 12)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getDocType(), 4)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(dto.getDescription(), 50)).append(CSV_SEPARATOR)
                    .append(StringUtils.rightPad(docId, 12)).append(System.lineSeparator());

        } catch (IOException ex) {
            logger.error("Exception in writing output file", ex);
        }
    }

    private void deleteCORRTIFF1() {
        try {
            logger.info("Deleting previously processed CORRTIFF1 file " + jobBaseDir + OUTPUT_FILE);
            Files.delete(Path.of(jobBaseDir + "CORRTIFF1.txt"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
