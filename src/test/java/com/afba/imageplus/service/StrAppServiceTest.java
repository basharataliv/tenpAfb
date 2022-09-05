package com.afba.imageplus.service;

import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.STRAPPSServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.Helper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = { ErrorServiceImp.class, STRAPPSServiceImpl.class, RangeHelper.class,
        AuthorizationHelper.class })
class StrAppServiceTest {
    @MockBean
    ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    private List<EKD0250CaseQueue> caseQueueList;
    @MockBean
    private CaseQueueService caseQueueService;
    @MockBean
    private CaseService caseService;
    @MockBean
    private CaseDocumentService caseDocumentService;
    @MockBean
    private PendCaseService pendCaseService;
    @MockBean
    private CaseCommentService caseCommentService;
    @MockBean
    private DateHelper dateHelper;
    @MockBean
    private QCRUNHISService qCRUNHISService;
    @MockBean
    private AuthorizationHelper authorizationHelper;

    @Autowired
    private STRAPPSService service;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @PostConstruct
    public void loadData() {
        caseQueueList = Helper.buildCaseQueueListWithTestDataForMove(Helper.buildGenericCaseListWithTestDataForMove());
    }

    @Test
    void StrAppsProcessTest() {
        Mockito.when(caseQueueService.getCaseDocumentsFromMultipleQueue(Arrays.asList("APPSINQ", "MEDIINQ")))
                .thenReturn(caseQueueList);
        Mockito.when(caseService.findByCmAccountNumberAndName("abc", "11")).thenReturn(null);
        Mockito.when(caseService.update("1", Helper.buildGenericCaseListWithTestDataForMove().get(0)))
                .thenReturn(Helper.buildGenericCaseListWithTestDataForMove().get(0));
        service.sTRAPPSProcessing();
        verify(caseService, times(2)).caseEnqueue(Mockito.any(), Mockito.any());

    }

    @Test
    void StrAppsProcessTest_WhenQueueDataNotFound() {
        Mockito.when(caseQueueService.getCaseDocumentsFromMultipleQueue(Arrays.asList("APPSINQ", "MEDIINQ")))
                .thenReturn(caseQueueList);
        Mockito.when(caseService.findByCmAccountNumberAndName("abc", "11"))
                .thenReturn(Helper.buildGenericCaseListWithTestDataForMove());
        service.sTRAPPSProcessing();
        verify(caseCommentService, times(0)).getCommentsSetByCaseId(Mockito.any());
        verify(qCRUNHISService, times(0)).qcRunTimeCheck(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        verify(pendCaseService, times(0)).findByCaseId(Mockito.any());

    }
}
