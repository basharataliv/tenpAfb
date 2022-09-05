package com.afba.imageplus.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.model.sqlserver.DOCMOVE;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.IMPCORRTIFServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { ErrorServiceImp.class, IMPCORRTIFServiceImpl.class, RangeHelper.class,
        AuthorizationHelper.class }, properties = { "afba.exstream.job.base.dir=src/test/resources/doc/jobs/" })
@TestInstance(Lifecycle.PER_CLASS)

class IMPCORRTIFTestService {
    @MockBean
    ErrorRepository errorRepository;
    @Autowired
    private ErrorServiceImp errorServiceImp;
    @MockBean
    private AuthorizationCacheService authorizationCacheService;
    @MockBean
    private DocumentService documentService;
    @MockBean
    private CaseDocumentService caseDocumentService;
    @MockBean
    private CaseService caseService;
    @MockBean
    private DOCMOVEService dOCMOVEService;
    @Autowired
    private IMPCORRTIFService iMPCORRTIFService;

    @Value("${afba.exstream.job.base.dir}")
    private String jobBaseDir;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    void impcorrtifProcessingTest() {
        // input data
        EKD0350Case case1 = new EKD0350Case();
        case1.setCaseId("1");
        case1.setCmAccountNumber("123456789");
        case1.setCmFormattedName("81 IMAGE ACTION");
        case1.setCurrentQueueId("IMAGE");

        Mockito.when(caseService.findByCmAccountNumberAndName(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(case1));
        Mockito.when(documentService.importDocument(Mockito.any(EKD0310Document.class)))
                .thenReturn(EKD0310Document.builder().documentId("abc").documentType("DFT").docPage(1).build());

        Mockito.when(caseDocumentService.insert(Mockito.any(EKD0315CaseDocument.class)))
                .thenReturn(new EKD0315CaseDocument());
        Mockito.when(dOCMOVEService.findById(Mockito.any())).thenReturn(Optional.of(new DOCMOVE("ACCT", "01")));

        iMPCORRTIFService.impcorrtifProcessing();
        verify(caseService, times(1)).findByCmAccountNumberAndName(Mockito.any(), Mockito.any(), Mockito.any());
        verify(documentService, times(1)).importDocument(Mockito.any(EKD0310Document.class));
        verify(caseDocumentService, times(1)).insert(Mockito.any(EKD0315CaseDocument.class));
        Assertions.assertEquals(true, Files.exists(Paths.get(jobBaseDir + "CORRTIFF1.txt")));
    }

    @Test
    void impcorrtifProcessing_WhenCaseNotFound() {
        // input data
        CaseResponse case1 = new CaseResponse();
        case1.setCaseId("1");
        case1.setCmAccountNumber("123456789");
        case1.setCmFormattedName("81 IMAGE ACTION");
        case1.setCurrentQueueId("IMAGE");
        List<EKD0350Case> cases = new ArrayList<>();
        Mockito.when(dOCMOVEService.findById(Mockito.any())).thenReturn(Optional.of(new DOCMOVE("ACCT", "01")));
        Mockito.when(caseService.findByCmAccountNumberAndName(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(cases);
        Mockito.when(caseService.createCase(Mockito.any())).thenReturn(case1);
        Mockito.when(documentService.importDocument(Mockito.any(EKD0310Document.class)))
                .thenReturn(EKD0310Document.builder().documentId("abc").documentType("DFT").docPage(1).build());

        Mockito.when(caseDocumentService.insert(Mockito.any(EKD0315CaseDocument.class)))
                .thenReturn(new EKD0315CaseDocument());
        iMPCORRTIFService.impcorrtifProcessing();
        verify(caseService, times(1)).findByCmAccountNumberAndName(Mockito.any(), Mockito.any(), Mockito.any());
        verify(caseService, times(1)).createCase(Mockito.any());
        verify(documentService, times(1)).importDocument(Mockito.any(EKD0310Document.class));
        verify(caseDocumentService, times(1)).insert(Mockito.any(EKD0315CaseDocument.class));
        Assertions.assertEquals(true, Files.exists(Paths.get(jobBaseDir + "CORRTIFF1.txt")));

    }
}
