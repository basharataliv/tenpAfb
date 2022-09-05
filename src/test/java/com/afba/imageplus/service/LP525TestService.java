package com.afba.imageplus.service;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

import com.afba.imageplus.dto.CaseDocumentsDto;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.LP525ServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { ErrorServiceImp.class, LP525ServiceImpl.class, RangeHelper.class,
        AuthorizationHelper.class }, properties = { "afba.exstream.job.base.dir=src/test/resources/doc/jobs/",
                "afba.exstream.job.download.doc.dir=src/test/resources/doc/jobs/out/" })
@TestInstance(Lifecycle.PER_CLASS)

class LP525TestService {
    @MockBean
    ErrorRepository errorRepository;
    @Autowired
    private ErrorServiceImp errorServiceImp;
    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private CaseService caseService;
    @MockBean
    private SharepointService sharepointService;

    @Autowired
    private LP525Service lP525Service;

    @Value("${afba.exstream.job.base.dir}")
    private String jobBaseDir;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    void lp525Test() {
        // input data
        CaseDocumentsDto casedto = new CaseDocumentsDto();
        casedto.setCaseId("1");
        casedto.setDocumentId("abc");
        casedto.setDocumentType("APPSDROP");
        casedto.setSpDocumentlibraryId("1234fsvvf");
        casedto.setSpDdocumentSitId("12234fvefvaef");
        casedto.setSpDocumentId("abcvd");

        Mockito.when(
                caseService.getCaseDocumentsByPolicyAndCmfnamesByNative(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(casedto));

        Mockito.when(sharepointService.downloadFileFromSharepoint(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new ByteArrayInputStream("abcv".getBytes()));

        lP525Service.lp525Processing();
        Assertions.assertEquals(true, Files.exists(Paths.get(jobBaseDir + "LPAPPSR.txt")));
        Assertions.assertEquals(true, Files.exists(Paths.get(jobBaseDir + "LPAPPSF.txt")));
    }

    @Test
    void lp525TestWhenBusinessValidationFailed() {
        // input data
        CaseDocumentsDto casedto = new CaseDocumentsDto();
        casedto.setCaseId("1");
        casedto.setDocumentId("abc");
        casedto.setDocumentType("APPSDRP");
        casedto.setSpDocumentlibraryId("1234fsvvf");
        casedto.setSpDdocumentSitId("12234fvefvaef");
        casedto.setSpDocumentId("abcvd");

        Mockito.when(
                caseService.getCaseDocumentsByPolicyAndCmfnamesByNative(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(casedto));

        lP525Service.lp525Processing();
        Assertions.assertEquals(false, Files.exists(Paths.get(jobBaseDir + "LPAPPSR.txt")));
        Assertions.assertEquals(false, Files.exists(Paths.get(jobBaseDir + "LPAPPSF.txt")));
    }
}
