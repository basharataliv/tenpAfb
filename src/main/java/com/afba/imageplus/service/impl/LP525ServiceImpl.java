package com.afba.imageplus.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.CaseDocumentsDto;
import com.afba.imageplus.dto.LP252InputFileDto;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.LP525Service;
import com.afba.imageplus.service.SharepointService;
import com.afba.imageplus.utilities.CsvFileReaderUtil;

@Service
public class LP525ServiceImpl implements LP525Service {
    Logger logger = LoggerFactory.getLogger(LP525ServiceImpl.class);

    private final CaseService caseService;
    private final SharepointService sharepointService;

    public LP525ServiceImpl(CaseService caseService,
            SharepointService sharepointService) {
        this.caseService = caseService;
        this.sharepointService = sharepointService;
    }

    private static final String INPUT_FILE1 = "LPRPRTDRVR.txt";
    private static final String INPUT_FILE2 = "LP_App_File.txt";
    private static final String OUTPUT_FILE1 = "LPAPPSR.txt";
    private static final String OUTPUT_FILE2 = "LPAPPSF.txt";
    private static final String CASE_DESC_01 = "01";
    private static final String CASE_DESC_11 = "11";
    private static final String APP_QUEUE = "APPS";

    private final String CSV_SEPARATOR = ",";

    @Value("${afba.exstream.job.base.dir}")
    private String jobBaseDir;
    @Value("${afba.exstream.job.download.doc.dir}")
    private String docDownloadDir;

    @Override
    public void lp525Processing() {
        try {
            logger.info("LP525 processing Started ");

            deletefile(OUTPUT_FILE1);
            deletefile(OUTPUT_FILE2);
            cleanOutputFolder();

            // file one LPRPRTDRVR.txt read
            List<LP252InputFileDto> lprList = CsvFileReaderUtil.readCsv(LP252InputFileDto.class,
                    new FileInputStream(new File(jobBaseDir + INPUT_FILE1)));

            // file two LP_App_File.txt read
            List<LP252InputFileDto> lpaList = CsvFileReaderUtil.readCsv(LP252InputFileDto.class,
                    new FileInputStream(new File(jobBaseDir + INPUT_FILE2)));

            // processing file one LPRPRTDRVR.txt
            processInputFileRecords(lprList, OUTPUT_FILE1);

            // processing file two LP_App_File.txt read
            processInputFileRecords(lpaList, OUTPUT_FILE2);
        } catch (Exception e) {
            logger.error("error in main lp525 ", e);
        }
    }

    private void processInputFileRecords(List<LP252InputFileDto> inputListList, String outFileName) {
        logger.info("Input file records are " + inputListList);
        try {
            for (LP252InputFileDto dto : inputListList) {
                try {
                    if (dto.getPolicyNo() != null) {

                        // get cases and associations data by policy
                        List<CaseDocumentsDto> cases = caseService.getCaseDocumentsByPolicyAndCmfnamesByNative(
                                dto.getPolicyNo(), CASE_DESC_01, CASE_DESC_11);

                        CaseDocumentsDto appDoc = getAppsDocuments(dto, cases);

                        if (appDoc == null || appDoc.getDocumentType() == null) {

                            throw new DomainException(HttpStatus.CONFLICT, "LP5250404",
                                    "Not App doc Found against policy : " + dto.getPolicyNo());
                        }

                        logger.info("selected Appdoc type {} selected Appdoc id {} ", appDoc.getDocumentType(),
                                appDoc.getDocumentId());

                        downloadAndwriteOutputFile(appDoc, dto.getPolicyNo(), outFileName);

                        cases.forEach(cas -> {

                            if (!cas.getDocumentType().equals(appDoc.getDocumentType())) {

                                if (docTypeAndProductCodeBusinessChecks(cas.getDocumentType(), appDoc.getScanningDate(),
                                        cas.getScanningDate())) {


                                    logger.info("selected non Appdoc type {} selected non Appdoc id {} ",
                                            cas.getDocumentType(), cas.getDocumentId());


                                    downloadAndwriteOutputFile(cas, dto.getPolicyNo(), outFileName);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Exception in processing files" + e.getLocalizedMessage());
        }
    }

    private void downloadAndwriteOutputFile(CaseDocumentsDto doc, String policy, String outFileName) {
        if (doc.getSpDdocumentSitId() != null && doc.getSpDocumentId() != null
                && doc.getSpDocumentlibraryId() != null) {
            String filepath = docDownloadDir + doc.getDocumentId() + ".tif";
            try (OutputStream output = new FileOutputStream(new File(filepath))) {

                // downloading the doc
                sharepointService.downloadFileFromSharepoint(doc.getSpDdocumentSitId(), doc.getSpDocumentlibraryId(),
                        doc.getSpDocumentId()).transferTo(output);

                // writing data on file
                writeDataOnFile(policy, filepath, outFileName);
            } catch (Exception e) {
                logger.error("unable to download document", e);
            }

        }
    }

    private CaseDocumentsDto getAppsDocuments(LP252InputFileDto dto, List<CaseDocumentsDto> cases) {
        CaseDocumentsDto appDoc = null;
        try {
            // can find this business logic in exstream jobs docs

            for (CaseDocumentsDto cas : cases) {

                if (cas.getDocumentType() != null) {

                    if (cas.getDocumentType().length() <= 7 && cas.getDocumentType().substring(0, 4).equals(APP_QUEUE)
                            && cas.getDocumentType().substring(6, 7).equals("O")) {
                        appDoc = cas;
                        break;
                    } else if (cas.getDocumentType().length() >= 8
                            && cas.getDocumentType().substring(0, 4).equals(APP_QUEUE)
                            && cas.getDocumentType().substring(6, 7).equals("O")
                            && !cas.getDocumentType().substring(7, 8).equals("T")) {
                        appDoc = cas;
                        break;

                    } else if (dto.getPolicyType() != null && dto.getPolicyType().length() >= 2
                            && dto.getPolicyType().substring(0, 2).equals("FN") && cas.getDocumentType().length() >= 4
                            && cas.getDocumentType().substring(0, 4).equals(APP_QUEUE)) {
                        appDoc = cas;
                        break;
                    } else if (cas.getDocumentType().length() <= 6
                            && cas.getDocumentType().substring(0, 4).equals(APP_QUEUE) && dto.getPolicyType() != null
                            && dto.getPolicyType().length() >= 2
                            && dto.getPolicyType().substring(0, 2).equals(cas.getDocumentType().substring(4, 6))) {
                        appDoc = cas;
                        break;
                    } else if (cas.getDocumentType().length() >= 8
                            && cas.getDocumentType().substring(0, 4).equals(APP_QUEUE)
                            && !cas.getDocumentType().substring(6, 8).equals("RS") && dto.getPolicyType() != null
                            && dto.getPolicyType().length() >= 2
                            && dto.getPolicyType().substring(0, 2).equals(cas.getDocumentType().substring(4, 6))) {
                        appDoc = cas;
                        break;
                    } else if (cas.getDocumentType().equals("APPSIP") && dto.getPolicyType() != null
                            && dto.getPolicyType().equals("ISPPRE15")) {
                        appDoc = cas;
                        break;
                    } else if (cas.getDocumentType().equals("APPSIP") && dto.getPolicyType() != null
                            && dto.getPolicyType().equals("ISPGRD15")) {
                        appDoc = cas;
                        break;
                    } else if (cas.getDocumentType().equals("APPSIP") && dto.getPolicyType() != null
                            && dto.getPolicyType().equals("ISPWL")) {
                        appDoc = cas;
                        break;
                    } else if (dto.getPolicyType() != null && dto.getPolicyType().length() >= 2
                            && dto.getPolicyType().substring(0, 2).equals("TS")
                            && cas.getDocumentType().equals("APPSIS")) {
                        appDoc = cas;
                        break;
                    } else if (dto.getPolicyType() != null && dto.getPolicyType().length() >= 2
                            && dto.getPolicyType().substring(0, 2).equals("TG")
                            && cas.getDocumentType().equals("APPSGS")) {
                        appDoc = cas;
                        break;
                    }
                }

            }

        } catch (Exception e) {
            logger.error("No docType found " + e.getLocalizedMessage());
        }
        return appDoc;
    }

    private Boolean docTypeAndProductCodeBusinessChecks(String docType, LocalDate appsDate, LocalDate docScaningDate) {
        try {
            // can find this business logic in exstream jobs docs
            if (docType != null) {

                if (docType.equals("DATAFORM")) {
                    return true;
                } else if (docType.equals("COAPPCCC")) {
                    if (docScaningDate.isAfter(appsDate) || docScaningDate.equals(appsDate)) {
                        return true;
                    }
                } else if (docType.equals("APPSPEND")) {

                    if (docScaningDate.isAfter(appsDate) || docScaningDate.equals(appsDate)) {
                        return true;
                    }
                } else if (docType.equals("REPLCORR")) {

                    if (docScaningDate.isAfter(appsDate) || docScaningDate.equals(appsDate)) {
                        return true;
                    }
                } else if (docType.equals("REPLMAIL")) {

                    if (docScaningDate.isAfter(appsDate) || docScaningDate.equals(appsDate)) {
                        return true;
                    }
                } else if (docType.length() >= 7 && docType.substring(0, 7).equals("APPSEND")) {

                    if (docScaningDate.isAfter(appsDate) || docScaningDate.equals(appsDate)) {
                        return true;
                    }
                } else if (docType.equals("APPSLIFE")) {

                    if (docScaningDate.isAfter(appsDate) || docScaningDate.equals(appsDate)) {
                        return true;
                    }
                } else if (docType.equals("APPSAFBA")) {

                    if (docScaningDate.isAfter(appsDate) || docScaningDate.equals(appsDate)) {
                        return true;
                    }
                } else if (docType.equals("APPSLIF2")) {

                    if (docScaningDate.isAfter(appsDate) || docScaningDate.equals(appsDate)) {
                        return true;
                    }
                } else if (docType.equals("APPSAFB2")) {

                    if (docScaningDate.isAfter(appsDate) || docScaningDate.equals(appsDate)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("No docType found " + e.getLocalizedMessage());
        }
        return false;
    }


    private void writeDataOnFile(String policy, String docUrl, String outFileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(jobBaseDir + outFileName, true))) {
            writer.append(StringUtils.rightPad(policy, 12)).append(CSV_SEPARATOR).append(docUrl)
                    .append(System.lineSeparator());

        } catch (IOException ex) {
            logger.error("Exception in writing output file", ex);
        }
    }

    private void deletefile(String path) {
        try {
            logger.info("Deleting previously processed file " + jobBaseDir + path);
            Files.delete(Path.of(jobBaseDir + path));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void cleanOutputFolder() {
        try {
            logger.info("Deleting previously downloaded documents " + docDownloadDir);

            for (File file : new File(docDownloadDir).listFiles())
                if (!file.isDirectory())
                    logger.info("File {} deleted successfully : {}", file.getName(), file.delete());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
