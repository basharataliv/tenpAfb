package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKD0260Reindexing;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.TSACORRHIS;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.StauntonServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
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

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { ErrorServiceImp.class, StauntonServiceImpl.class, RangeHelper.class,
        AuthorizationHelper.class }, properties = { "afba.staunton.base.dir=src/test/resources/doc/" })
@TestInstance(Lifecycle.PER_CLASS)

class StautonTestService {
    @MockBean
    ErrorRepository errorRepository;
    @Autowired
    private ErrorServiceImp errorServiceImp;
    @MockBean
    private AuthorizationCacheService authorizationCacheService;
    @MockBean
    private DocumentService documentService;
    @MockBean
    private ReindexingService reindexingService;
    @MockBean
    private TSACORRHISSevice tSACORRHISSevice;
    @Autowired
    private StauntonService service;

    @Value("${afba.staunton.base.dir}")
    private String stauntonBaseDir;

    @BeforeAll
    void setup() {
        try {
            if (!Files.exists(Paths.get(stauntonBaseDir + "OCRFF"))) {
                Files.createDirectories(Paths.get(stauntonBaseDir + "OCRFF"));
            }
            if (!Files.exists(Paths.get(stauntonBaseDir + "OCRFF//small.dat"))) {
                Files.createFile(Paths.get(stauntonBaseDir + "OCRFF//small.dat"));
            }
            if (!Files.exists(Paths.get(stauntonBaseDir + "OCRFF//small.tiff"))) {
                Files.createFile(Paths.get(stauntonBaseDir + "OCRFF//small.tiff"));
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

        EKD0260Reindexing reindex = EKD0260Reindexing.builder().documentId("abc").documentType("abc").identifier("123")
                .scanRepId("1245").indexFlag(false).indexRepId("12455").statusCode("1").build();
        TSACORRHIS his = TSACORRHIS.builder().documentId("abc").docType("abc").ssn("123").scanningDate(LocalDate.now())
                .build();
        Mockito.when(documentService.importDocument(Mockito.any(EKD0310Document.class)))
                .thenReturn(EKD0310Document.builder()
                        .doc(new MockMultipartFile("small.tiff",
                                new FileInputStream(new File(stauntonBaseDir + "OCRFF//small.tiff"))))
                        .documentType("DFT").docPage(1).build());
        Mockito.when(reindexingService.insert(Mockito.any(EKD0260Reindexing.class))).thenReturn(reindex);
        Mockito.when(tSACORRHISSevice.insert(Mockito.any(TSACORRHIS.class))).thenReturn(his);

        service.stauntonProcessing();
        Assertions.assertEquals(false, Files.exists(Paths.get(stauntonBaseDir + "OCRFF//small.dat")));
        Assertions.assertEquals(true, Files.exists(Paths.get(stauntonBaseDir + "OCRFF//small.tiff")));
    }

}
