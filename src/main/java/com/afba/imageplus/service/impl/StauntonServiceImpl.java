package com.afba.imageplus.service.impl;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.TSACORRContentDto;
import com.afba.imageplus.model.sqlserver.EKD0260Reindexing;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.TSACORRHIS;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.ReindexingService;
import com.afba.imageplus.service.StauntonService;
import com.afba.imageplus.service.TSACORRHISSevice;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class StauntonServiceImpl implements StauntonService {
    Logger logger = LoggerFactory.getLogger(StauntonServiceImpl.class);

    private final DocumentService documentService;
    private final ReindexingService reindexingService;
    private final TSACORRHISSevice tSACORRHISSevice;

    public StauntonServiceImpl(DocumentService documentService, ReindexingService reindexingService,
            TSACORRHISSevice tSACORRHISSevice) {

        this.documentService = documentService;
        this.reindexingService = reindexingService;
        this.tSACORRHISSevice = tSACORRHISSevice;
    }

    @Value("${afba.staunton.base.dir}")
    private String stauntonBaseDir;

    private static final String DIR_OCRFF = "OCRFF";
    private static final String DIR_DATA = "DATA";
    private static final String DIR_TSACORR = "TSACORR";

    @Override
    public void stauntonProcessing() {
        int index = 0;
        while (index < 3) {
            try {
                String dirPath = getpath(index);
                File[] filesList = new File(dirPath).listFiles();
                for (File file : filesList) {
                    if (file.isFile() && FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("dat")) {
                        String text = new String(Files.readAllBytes(file.toPath()));
                        logger.info("DAT file found with this Content: " + text);
                        String[] listContent = text.split(System.lineSeparator());
                        for (String tsa : listContent) {
                            if (tsa.length() > 22) {
                                String fileName = tsa.substring(0, 12);
                                String ssn = tsa.substring(12, 21);
                                String type = tsa.substring(21, tsa.length() - 2).trim();
                                String pages = tsa.substring(tsa.length() - 2, tsa.length());
                                TSACORRContentDto dto = TSACORRContentDto.builder().docType(type).fileName(fileName)
                                        .ssn(ssn).pages(pages).build();
                                int i = 0;
                                while (i < 3) {
                                    File tifFile = new File(getpath(i) + "\\" + dto.getFileName());
                                    if (tifFile.isFile() && (FilenameUtils.getExtension(tifFile.getName()).equals("TIF")
                                            || FilenameUtils.getExtension(tifFile.getName()).equals("TIFF"))) {
                                        EKD0310Document doc = importDocument(tifFile, dto);
                                        reIndex(doc, dto);
                                        insertTSACORHIS(doc, dto);
                                        delete(tifFile);
                                    }
                                    i++;
                                }
                            }
                        }
                        delete(file);
                    }
                }

            } catch (Exception e) {
                logger.error("exception in Stauton", e);
            }
            index++;

        }
    }

    private void delete(File file) {
        try {
            logger.info("After processing file, going to delete file " + file.getPath());
            Files.delete(Path.of(file.getPath()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private EKD0310Document importDocument(File file, TSACORRContentDto dto) {
        try {
            EKD0310Document document = EKD0310Document.builder()
                    .doc(new MockMultipartFile(file.getName().toString(), new FileInputStream(file)))
                    .documentType(dto.getDocType()).docPage(Integer.parseInt(dto.getPages())).build();
            return documentService.importDocument(document);
        } catch (IOException e) {
            throw new DomainException(HttpStatus.BAD_REQUEST, "EKDFIL404", "Unable to read file ");
        }
    }

    private EKD0260Reindexing reIndex(EKD0310Document dto, TSACORRContentDto tsa) {
        EKD0260Reindexing reindex = EKD0260Reindexing.builder().documentId(dto.getDocumentId())
                .documentType(dto.getDocumentType()).identifier(tsa.getSsn()).
                scanRepId(dto.getScanningRepId()).indexFlag(false)
                .indexRepId(dto.getScanningRepId()).statusCode("1").build();
        reindex.setScanningDateTime(dto.getScanningDateTime());
        return reindexingService.insert(reindex);
    }

    private TSACORRHIS insertTSACORHIS(EKD0310Document dto, TSACORRContentDto tsa) {
        TSACORRHIS his = TSACORRHIS.builder().documentId(dto.getDocumentId()).docType(dto.getDocumentType())
                .ssn(tsa.getSsn()).scanningDate(LocalDate.now()).build();
        return tSACORRHISSevice.insert(his);
    }

    private String getpath(int index) {
        if (index == 0) {
            return stauntonBaseDir + DIR_OCRFF;
        } else if (index == 1) {
            return stauntonBaseDir + DIR_DATA;
        } else {
            return stauntonBaseDir + DIR_TSACORR;
        }

    }

}
