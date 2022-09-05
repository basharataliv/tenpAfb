package com.afba.imageplus.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.SCANFJCServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { ErrorServiceImp.class, SCANFJCServiceImp.class, RangeHelper.class,
        AuthorizationHelper.class }, properties = { "afba.scaned.doc.dir=src/test/resources/doc/scaned",
                "afba.uploaded.doc.dir=src/test/resources/doc/uploaded" })
@TestInstance(Lifecycle.PER_CLASS)

public class ScanFJCTestService {
    @MockBean
    ErrorRepository errorRepository;
    @Autowired
    private ErrorServiceImp errorServiceImp;
    @MockBean
    private AuthorizationCacheService authorizationCacheService;
    @MockBean
    private DocumentService documentService;
    @Autowired
    private SCANFJCService service;

    @Value("${afba.scaned.doc.dir}")
    private String scaningDocDir;
    @Value("${afba.uploaded.doc.dir}")
    private String uploadedDocDir;

    @BeforeAll
    void setup() {
        try {
            if (!Files.exists(Paths.get(scaningDocDir + "//small.tiff"))) {
                Files.createFile(Paths.get(scaningDocDir + "//small.tiff"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    void UploadingScenedDocTest() throws FileNotFoundException, IOException {

        Mockito.when(documentService.insert(Mockito.any(EKD0310Document.class))).thenReturn(EKD0310Document.builder()
                .doc(new MockMultipartFile("small.tiff", new FileInputStream(new File(scaningDocDir + "//small.tiff"))))
                .documentType("DFT").docPage(1).build());
        service.UploadingScenedDoc();
        File file = new File(uploadedDocDir + "/small.tiff");
        Assertions.assertEquals("small.tiff", file.getName());
        Assertions.assertEquals(true, Files.exists(Paths.get(uploadedDocDir + "/small.tiff")));
        Assertions.assertEquals(false, Files.exists(Paths.get(scaningDocDir + "/small.tiff")));
    }

}
