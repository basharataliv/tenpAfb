package com.afba.imageplus.search;

import com.afba.imageplus.controller.QueueController;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.repository.sqlserver.EKD0250CaseQueueRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.AuthorizationCacheService;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.UserProfileService;
import com.afba.imageplus.service.impl.CaseQueueServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.QueueServiceImpl;
import com.afba.imageplus.service.impl.UserProfileServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.Helper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = { ErrorServiceImp.class, CaseQueueServiceImpl.class, RangeHelper.class,
        AuthorizationHelper.class, QueueServiceImpl.class, CaseQueueService.class,QueueController.class, UserProfileServiceImpl.class})
class SearchByQueueTest {

    @MockBean
    private EKD0250CaseQueueRepository caseQueueRepository;

    @Autowired
    CaseQueueService caseQueueService;

    @MockBean
    private List<EKD0250CaseQueue> caseQueueList;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private QueueService queueService;

    @MockBean
    private CaseService caseService;

    @MockBean
    private QueueController queueController;

    @MockBean
    private  UserProfileService userProfileService;



    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    @PostConstruct
    public void loadData() {
        caseQueueList = Helper.buildCaseQueueListWithTestData(Helper.buildGenericCaseListWithTestData());
    }

    @Test
    void assertThat_exceptionIsThrown_asQueueDoesNotExists_whenGettingCaseDocumentsWithWrongQueueId() {
        Mockito.when(caseQueueRepository.findInQueueCaseDocumentsByNativeQueryGraph("XXXOOOPPP")).thenReturn(List.of());
        var throwable = assertThrows(DomainException.class, () -> {
            caseQueueService.getCaseDocumentsFromQueue("XXXOOOPPP");
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, throwable.getHttpStatus());
    }

    @Test
    void assertThat_caseIsReturnedWithDocuments_whenGettingCaseDocumentsByQueueId() {
        Mockito.when(caseQueueRepository.findInQueueCaseDocumentsByNativeQueryGraph("AAABBBCCC"))
                .thenReturn(caseQueueList.subList(0, 1));
        var response = caseQueueService.getCaseDocumentsFromQueue("AAABBBCCC");
        Assertions.assertEquals("AAABBBCCC", response.get(0).getQueueId());
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals("AAABBBCCC", response.get(0).getQueueId());
        Assertions.assertEquals(2, response.get(0).getCases().getDocuments().size());
    }
}
