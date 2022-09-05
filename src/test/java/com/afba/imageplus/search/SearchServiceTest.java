package com.afba.imageplus.search;

import com.afba.imageplus.dto.mapper.SearchDocumentsByCaseByPolicyResMapper;
import com.afba.imageplus.dto.mapper.SearchDocumentsByQueueResMapper;
import com.afba.imageplus.dto.req.CaseOptionsReq;
import com.afba.imageplus.dto.res.UserDetailsRes;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.SearchService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.SearchServiceImpl;
import com.afba.imageplus.utilities.Helper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = { ErrorServiceImp.class, SearchServiceImpl.class, Helper.class })
class SearchServiceTest {

    @MockBean
    private CaseQueueService caseQueueService;

    @MockBean
    CaseService caseService;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private EKDUserService eKDUserService;

    @MockBean
    private SearchDocumentsByCaseByPolicyResMapper policyResMapper;
    @MockBean
    ErrorService errorService;
    @MockBean
    SearchDocumentsByQueueResMapper queueResMapper;

    @MockBean
    QueueService queueService;
    @Autowired
    private SearchService searchService;
    private List<EKD0250CaseQueue> caseQueueList;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
    }

    private List<EKD0350Case> cases;

    @PostConstruct
    public void loadData() {
        cases = Helper.buildGenericCaseListWithTestData();
    }

    @PostConstruct
    public void queueLoadData() {
        caseQueueList = Helper.buildCaseQueueListWithTestData(Helper.buildGenericCaseListWithTestData());
    }

    @Test
    void assertThat_ssnOjbReturned_caseDocBySsn() {
        EKDUser ssn = new EKDUser();
        ssn.setAccountNumber("123446788");
        ssn.setIndices("123456789NICHOLOS                      HOLOCOMB            R1");
        Mockito.when(eKDUserService.getBySsn("123456789")).thenReturn(List.of(ssn));
        Mockito.when(caseService.getCaseDocumentsByPolicy("123446788")).thenReturn(cases);
        var response = searchService.getDocumentBySnn("123456789", new CaseOptionsReq());
        Assertions.assertEquals("123456789", response.getSsn());
        Assertions.assertEquals("123446788", response.getPolicies().get(0).getPolicyId());
    }

    @Test
    void assertThat_ssnOjbReturned_caseDocBySsn_whenSsnOrPolicyNotExist() {
        EKDUser ssn = new EKDUser();
        ssn.setAccountNumber("123446788");
        ssn.setIndices("123456789NICHOLOS                      HOLOCOMB            R1");
        Mockito.when(eKDUserService.getBySsn("123456789s")).thenReturn(List.of(ssn));
        Mockito.when(caseService.getCaseDocumentsByPolicy("123446788")).thenReturn(cases);
        var response = searchService.getDocumentBySnn("123456789", new CaseOptionsReq());

        assertEquals(null, response);

    }

    @Test
    void assertThat_ssnOjbReturned_caseDocByFirstLastName_whenFirstNameEmpty() {
        EKDUser ssn = new EKDUser();
        ssn.setAccountNumber("123446788");
        ssn.setIndices("123456789NICHOLOS                      HOLOCOMB            R1");
        Mockito.when(eKDUserService.getByLastFirstNme("NICHOLOS")).thenReturn(List.of(ssn));
        Mockito.when(caseService.getCaseDocumentsByPolicy("123446788")).thenReturn(cases);
        var response = searchService.getDocumentByFirstLastName("", "NICHOLOS", new CaseOptionsReq());
        Assertions.assertEquals("123456789", response.getSsn());
        Assertions.assertEquals("123446788", response.getPolicies().get(0).getPolicyId());
    }

    @Test
    void assertThat_ssnOjbReturned_caseDocByFirstLastName() {
        EKDUser ssn = new EKDUser();
        ssn.setAccountNumber("123446788");
        ssn.setIndices("123456789NICHOLOS                      HOLOCOMB            R1");
        Mockito.when(eKDUserService.getByLastFirstNme("NICHOLOS                      HOLOCOMB            "))
                .thenReturn(List.of(ssn));
        Mockito.when(caseService.getCaseDocumentsByPolicy("123446788")).thenReturn(cases);
        var response = searchService.getDocumentByFirstLastName("HOLOCOMB", "NICHOLOS", new CaseOptionsReq());
        Assertions.assertEquals("123456789", response.getSsn());
        Assertions.assertEquals("123446788", response.getPolicies().get(0).getPolicyId());
    }

    @Test
    void assertThat_caseNotFoundByQueue() {
        UserDetailsRes ssn = new UserDetailsRes();
        ssn.setPolicy("123446788");
        ssn.setSsn("123456789");
        ssn.setFirstName("HOLOCOMB");
        ssn.setLastName("NICHOLOS");
        Mockito.when(caseQueueService.getCaseDocumentsFromQueue(Mockito.any(String.class)))
                .thenReturn(caseQueueList.subList(0, 1));
        Mockito.when(eKDUserService.getByAccountNo("123446788")).thenReturn(ssn);
        var response = searchService.getByQueueId("AAABBBCCC", new CaseOptionsReq());
        Assertions.assertEquals(0, response.size());
        Assertions.assertEquals(true, response.isEmpty());

    }
}
