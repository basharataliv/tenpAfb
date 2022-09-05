package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.repository.sqlserver.EKD0110DocumentTypeRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.DocumentTypeServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {
        DocumentTypeServiceImpl.class,
        EKD0110DocumentType.class,
        ErrorServiceImp.class,
        RangeHelper.class,
        AuthorizationHelper.class
})
class DocumentTypeServiceTest {

    @MockBean
    private EKD0110DocumentTypeRepository ekd0110DocumentTypeRepository;

    @Autowired
    private DocumentTypeService documentTypeService;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @PostConstruct
    public void mock() {
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenAnswer(invocation -> {
            var documentType = (EKD0110DocumentType) invocation.getArgument(0);
            documentType.setDocumentType("*DFT");
            return documentType;
        });
    }

    @Test
    void documentTypeEditTest() {
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        var request = new EKD0110DocumentType();
        request.setDocumentType("*DFT");
        request.setDocumentDescription("mockSomeDescription");
        request.setDefaultSuspendDays(321);
        request.setDasdControlFlag(true);
        request.setOlsStoreFlag(true);
        request.setOutputRequired(true);
        request.setInformRetrievalDept(true);
        request.setCreateNewCase("2");
        request.setIndexScanFlag(true);
        request.setRequestSuspendFlag(true);
        request.setDefaultQueueId("IMAGHLDQ");
        request.setUserLastUpdate("[Afnan QA");
        request.setNoInBatch(321);
        request.setRequestInputFlag(true);
        request.setRetentionPeriod(321);
        request.setSecurityClass(2);
        request.setAllowImpA(true);
        request.setInpdTypeA("XZ");
        request.setFaxPSizeA("2");
        request.setAlwannflA(true);
        request.setAlwredflA(true);
        request.setAllowOcr(true);
        request.setConfirmAll(true);
        request.setWorkstationOnly(true);
        request.setMatchCp(true);
        request.setStoreMethod("1");
        request.setStoreClass("Z");
        request.setOptSysId(null);
        request.setLan3995("1");
        EKD0110DocumentType response= documentTypeService.update("*DFT", request);
        Assertions.assertEquals(request.getDocumentType(), response.getDocumentType());
        Assertions.assertEquals(request,response);

    }
    
    @Test
    void insert() {
        Mockito.when(ekd0110DocumentTypeRepository.existsById(any())).thenReturn(false);
        var request = new EKD0110DocumentType();
        request.setDocumentType("*DFT");
        request.setDocumentDescription("mockSomeDescription");
        request.setDefaultSuspendDays(321);
        request.setDasdControlFlag(true);
        request.setOlsStoreFlag(true);
        request.setOutputRequired(true);
        request.setInformRetrievalDept(true);
        request.setCreateNewCase("2");
        request.setIndexScanFlag(true);
        request.setRequestSuspendFlag(true);
        request.setDefaultQueueId("IMAGHLDQ");
        request.setUserLastUpdate("[Afnan QA");
        request.setNoInBatch(321);
        request.setRequestInputFlag(true);
        request.setRetentionPeriod(321);
        request.setSecurityClass(2);
        request.setAllowImpA(true);
        request.setInpdTypeA("XZ");
        request.setFaxPSizeA("2");
        request.setAlwannflA(true);
        request.setAlwredflA(true);
        request.setAllowOcr(true);
        request.setConfirmAll(true);
        request.setWorkstationOnly(true);
        request.setMatchCp(true);
        request.setStoreMethod("1");
        request.setStoreClass("Z");
        request.setOptSysId(null);
        request.setLan3995("1");
        EKD0110DocumentType response=documentTypeService.insert(request);
        Assertions.assertEquals("*DFT", request.getDocumentType());
        Assertions.assertEquals(request, response);

    }
}
