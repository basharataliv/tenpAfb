package com.afba.imageplus.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.DocumentServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.IMPBILLTIFServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;

@SpringBootTest(classes = { IMPBILLTIFServiceImpl.class, ErrorServiceImp.class, DocumentServiceImpl.class,
        RangeHelper.class, AuthorizationHelper.class })
class IMPBILLTIFServiceTest {
    @MockBean
    ErrorRepository errorRepository;
    @Autowired
    private ErrorServiceImp errorServiceImp;
    @Autowired
    private IMPBILLTIFService impbilltifService;
    @MockBean
    private AuthorizationCacheService authorizationCacheService;
    @MockBean
    private DocumentService documentService;
    @MockBean
    private CaseService caseService;
    @MockBean
    private CaseDocumentService caseDocumentService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    void assertThat_onProcessingBillRecords_recordIsProcessedIfDocumentFileFound() throws IOException {

        EKD0350Case ekd0350Case = EKD0350Case.builder().caseId("1111111").build();
        EKD0310Document document = EKD0310Document.builder().documentId("11111111").build();

        when(caseService.findByCmAccountNumberAndName(any(), any(), any())).thenReturn(List.of(ekd0350Case));

        when(documentService.importDocument(any())).thenReturn(document);
        when(caseDocumentService.insert(any())).thenReturn(new EKD0315CaseDocument());

        impbilltifService.impbilltifProcessing();

        verify(caseService, times(1)).findByCmAccountNumberAndName(any(), any(), any());
        verify(documentService, times(1)).importDocument(any());
        verify(caseDocumentService, times(1)).insert(any());

    }

    @Test
    void assertThat_onProcessingBillRecords_recordIsProcessed_andDeffoldCalled_IfDocumentFileFound()
            throws IOException {

        CaseResponse ekd0350Case = CaseResponse.builder().caseId("1111111").build();
        EKD0310Document document = EKD0310Document.builder().documentId("11111111").build();

        when(documentService.importDocument(any())).thenReturn(document);
        when(caseDocumentService.insert(any())).thenReturn(new EKD0315CaseDocument());
        when(caseService.createCase(any())).thenReturn(ekd0350Case);

        impbilltifService.impbilltifProcessing();

        verify(caseService, times(1)).findByCmAccountNumberAndName(any(), any(), any());
        verify(documentService, times(1)).importDocument(any());
        verify(caseService, times(1)).createCase(any());
        verify(caseDocumentService, times(1)).insert(any());

    }

}
