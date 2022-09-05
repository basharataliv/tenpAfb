package com.afba.imageplus.service;

import com.afba.imageplus.configuration.ImageThreadingConfig;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.repository.sqlserver.EKD0010NextDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0310DocumentRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.DocumentServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.FileConvertServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.ImageConverter;
import com.afba.imageplus.utilities.MSBatchConverter;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = { DocumentServiceImpl.class, ImageConverter.class, DateHelper.class,
        ImageThreadingConfig.class, SharepointControlService.class, ErrorServiceImp.class,  RangeHelper.class,
        AuthorizationHelper.class, FileConvertServiceImpl.class, MSBatchConverter.class,ReindexingService.class})
class DocumentServiceTest {

    @MockBean
    private EKD0310DocumentRepository ekd0310DocumentRepository;
    @MockBean
    private EKD0010NextDocumentRepository ekd0010NextDocumentRepository;
    @MockBean
    private SharepointService sharepointService;
    @MockBean
    private DocumentTypeService documentTypeService;

    @Autowired
    private DocumentService documentService;

    @MockBean
    private SharepointControlService sharepointControlService;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private CaseDocumentService eKD0315service;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private IndexingService indexingService;

    @MockBean
    private ReindexingService reindexingService;


    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @Test
    void downloadDocumentFileTest() throws IOException {
        Mockito.when(ekd0310DocumentRepository.findAllByDocumentIdIn(any())).thenReturn(List.of(new EKD0310Document()));
        var tiffFile = new FileInputStream(new ClassPathResource("assets/big.tif").getFile());
        Mockito.when(sharepointService.downloadFileMultiThreaded(any(), any(), any()))
                .thenReturn(tiffFile.readAllBytes());
        var pdfBytes = documentService.downloadDocumentFiles(List.of(""));
        var pdfFile = new FileInputStream(new ClassPathResource("assets/big.pdf").getFile());
        Assertions.assertEquals(pdfBytes.length, pdfFile.readAllBytes().length);

    }

    @Test
    void changeDocumentTypeTest() throws IOException {
        Mockito.when(ekd0310DocumentRepository.findById(any())).thenReturn(Optional.of(new EKD0310Document()));
        Mockito.when(ekd0310DocumentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(EKD0110DocumentType.builder().documentType("type1").documentDescription("Doc Type 1").build()));
        Mockito.when(ekd0310DocumentRepository.existsById(any())).thenReturn(true);
        var document = documentService.changeDocumentType("doc1", "type1", "");
        Assertions.assertEquals("type1", document.getDocumentType());
        Assertions.assertEquals("Doc Type 1", document.getReqListDescription());

    }
}
