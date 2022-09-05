package com.afba.imageplus.service.impl;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.SCANFJCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class SCANFJCServiceImp implements SCANFJCService {
    Logger logger = LoggerFactory.getLogger(SCANFJCServiceImp.class);

    private final DocumentService documentService;

    public SCANFJCServiceImp(DocumentService documentService) {

        this.documentService = documentService;
    }

    @Value("${afba.scaned.doc.dir}")
    private String scaningDocDir;
    @Value("${afba.uploaded.doc.dir}")
    private String uploadedDocDir;

    @PostConstruct
    public void checkAndCreatDir() {
        try {
            if (!Files.exists(Paths.get(scaningDocDir))) {
                Files.createDirectories(Paths.get(scaningDocDir));
                logger.info("Scaning dir created");
            }
            if (!Files.exists(Paths.get(uploadedDocDir))) {
                Files.createDirectories(Paths.get(uploadedDocDir));
                logger.info("upload dir created");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void UploadingScenedDoc() {

        try {
            File[] filesList = new File(scaningDocDir).listFiles();
            for (File file : filesList) {
                if (file.isFile()) {
                    logger.info("Uploading the scaning file details " + file.getName());
                    importDocument(file);
                    moveDoc(file.getName());
                }
            }

        } catch (Exception e) {
            logger.error("exception in scane FJC program", e);
        }

    }

    private EKD0310Document importDocument(File file) {
        try {
            EKD0310Document document = EKD0310Document.builder()
                    .doc(new MockMultipartFile(file.getName().toString(), new FileInputStream(file)))
                    .documentType("*DFT").docPage(1).build();
            return documentService.insert(document);
        } catch (IOException e) {
            throw new DomainException(HttpStatus.BAD_REQUEST, "EKDFIL404", "Unable to read file ");
        }
    }

    private void moveDoc(String filename) {
        try {
            Files.move(Paths.get(scaningDocDir + "\\" + filename), Paths.get(uploadedDocDir + "\\" + filename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
