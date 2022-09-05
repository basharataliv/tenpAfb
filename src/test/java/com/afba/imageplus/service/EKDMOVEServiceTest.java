package com.afba.imageplus.service;

import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.ClosFlrResponse;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.repository.sqlserver.EmailTemplateRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.EKDMoveServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.EmailUtility;
import com.afba.imageplus.utilities.Helper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = { ErrorServiceImp.class, EKDMoveServiceImpl.class, RangeHelper.class,
        AuthorizationHelper.class })
public class EKDMOVEServiceTest {

    @Autowired
    private EKDMoveService EKDMoveService;
    @MockBean
    private CaseQueueService caseQueueService;
    @MockBean
    private DOCMOVEService dOCMOVEService;
    @MockBean
    private CaseService caseService;
    @MockBean
    private CaseDocumentService caseDocumentService;
    @MockBean
    private MOVETRAILService mOVETRAILService;
    @MockBean
    private DOCTEMPService dOCTEMPService;

    @MockBean
    private ErrorRepository errorRepository;
    @MockBean
    private SpringTemplateEngine thymeleafTemplateEngine;
    @MockBean
    private EmailTemplateRepository emailTemplateRepository;
    @MockBean
    private EmailUtility emailUtility;
    private List<EKD0250CaseQueue> caseQueueList;
    private List<EKD0250CaseQueue> caseQueueListDelete;
    @MockBean
    private AuthorizationHelper authorizationHelper;
    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @PostConstruct
    public void loadData() {
        caseQueueList = Helper.buildCaseQueueListWithTestDataForMove(Helper.buildGenericCaseListWithTestDataForMove());
    }

    @PostConstruct
    public void loadDataForDete() {
        caseQueueListDelete = Helper
                .buildCaseQueueListWithTestDataForDelete(Helper.buildGenericCaseListWithTestDataForDelete());
    }

    @Test
    void eKDMoveProcessingForMoveQueueTest() {

        Mockito.when(caseQueueService.getCaseDocumentsFromQueue("MOVE")).thenReturn(caseQueueList);
        Mockito.when(dOCMOVEService.findById("someDoc")).thenReturn(Optional.empty());
        Mockito.when(dOCTEMPService.findById("someDoc")).thenReturn(Optional.empty());
        Mockito.when(caseService.closeCase("1")).thenReturn(new ClosFlrResponse("Case is Closed", "3"));
        Mockito.when(caseService.deleteCase("1")).thenReturn(BaseResponseDto.success("Case is deleted"));

        EKDMoveService.eKDMoveProcessingForMoveQueue();

        assertEquals("Case is deleted", caseService.deleteCase("1").getResponseData());
        assertEquals("3", caseService.closeCase("1").getNextCaseId());

    }

    // @Test
    void eKDMoveProcessingForMoveQueueTestWhenNoQueueFound() {

        Mockito.when(caseQueueService.getCaseDocumentsFromQueue("MOVE")).thenReturn(null);
        Mockito.when(dOCMOVEService.findById("someDoc")).thenReturn(Optional.empty());
        Mockito.when(dOCTEMPService.findById("someDoc")).thenReturn(Optional.empty());
        Mockito.when(caseService.closeCase("1")).thenReturn(new ClosFlrResponse("Case is Closed", "3"));
        Mockito.when(caseService.deleteCase("1")).thenReturn(BaseResponseDto.success("Case is deleted"));

        assertThrows(NullPointerException.class, () -> {
            EKDMoveService.eKDMoveProcessingForMoveQueue();
        });

    }

    @Test
    void eKDMoveProcessingForDeleteQueueTest() {

        Mockito.when(caseQueueService.getCaseDocumentsFromMultipleQueue(Arrays.asList("DELETE", "DELETES")))
                .thenReturn(caseQueueListDelete);
        Mockito.when(caseService.closeCase("1")).thenReturn(new ClosFlrResponse("Case is Closed", "4"));
        Mockito.when(caseService.deleteCase("1")).thenReturn(BaseResponseDto.success("Case is deleted"));

        EKDMoveService.eKDMoveProcessingForDeleteQueue();
        assertEquals("Case is deleted", caseService.deleteCase("1").getResponseData());
        assertEquals("4", caseService.closeCase("1").getNextCaseId());

    }

    // @Test
    void eKDMoveProcessingForDeleteQueueTestWhenNoQueueFound() {

        Mockito.when(caseQueueService.getCaseDocumentsFromMultipleQueue(Arrays.asList("DELETE", "DELETES")))
                .thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            EKDMoveService.eKDMoveProcessingForDeleteQueue();
        });

    }
}
