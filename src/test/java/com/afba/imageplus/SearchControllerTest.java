package com.afba.imageplus;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.SearchController;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.*;
import com.afba.imageplus.dto.req.CaseOptionsReq;
import com.afba.imageplus.dto.res.UserDetailsRes;
import com.afba.imageplus.dto.res.search.SSN;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.repository.sqlserver.*;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.*;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.*;
import com.azure.core.implementation.Option;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {SearchController.class, SearchDocumentsByCaseOrPolicyResMapper.class, SearchDocumentsByCaseByPolicyResMapper.class, SearchDocumentsByQueueResMapper.class, SearchDocumentsByCaseResMapper.class, BaseService.class, BaseMapper.class, ErrorServiceImp.class, ObjectMapper.class, DateHelper.class, RangeHelper.class, RestResponseEntityExceptionHandler.class, AuthorizationHelper.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class SearchControllerTest {

    @MockBean
    private EKD0250CaseQueueRepository caseQueueRepository;

    @MockBean
    private CaseService caseService;

    @MockBean
    private SearchService searchService;

    @MockBean
    private CaseQueueService caseQueueService;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private EKDUserService ekdUserService;

    @Autowired
    private SearchController controller;

    @MockBean
    private ErrorRepository errorRepository;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private MockMvc mockMvc;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    private List<EKD0350Case> cases;

    private List<EKD0250CaseQueue> caseQueueList;

    @PostConstruct
    public void loadData() {
        cases = Helper.buildGenericCaseListWithTestData();
        cases.get(1).setCmAccountNumber("12345678");
        caseQueueList = Helper.buildCaseQueueListWithTestData(cases);
    }

    @Test
    public void contextLoads() throws Exception {
         Assertions.assertNotNull(controller);
    }

    @Test
    public void getDocumentByCaseAPISuccess() throws Exception {
        UserDetailsRes user = new UserDetailsRes("123446788", "", "test", "user");
        Mockito.when(caseService.getCaseDocuments("2")).thenReturn(cases.get(1));
        Mockito.when(ekdUserService.getByAccountNo(any())).thenReturn(user);
        this.mockMvc.perform(get("/search")
                        .param("caseId", "2")
                        .requestAttr("alwVwc", true)
                        .requestAttr("alwAdc", true)
                        .requestAttr("alwWkc", true)
                        .requestAttr("repId", "TEST")
                        .requestAttr("repDep", "TEST")
                        .requestAttr("isAdmin", true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.policies[0].cases[0].caseId").value(2));
    }

    @Test
    public void getDocumentByCaseAPIFailureCaseNotFound() throws Exception {
        Mockito.when(caseService.getCaseDocuments(any())).thenThrow(
                new DomainException(HttpStatus.NOT_FOUND, EKDError.EKD350404.code(), "Case Not Found"));
        this.mockMvc.perform(get("/search")
                        .param("caseId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"))
        ;
    }

    @Test
    public void getDocumentByQueueAPISuccess() throws Exception {
        Mockito.when(caseQueueService.getCaseDocumentsFromQueue("AAABBBCCC")).thenReturn(caseQueueList);
        Mockito.when(caseQueueRepository.findInQueueCaseDocumentsByNativeQueryGraph("AAABBBCCC"))
                .thenReturn(caseQueueList.subList(0, 1));
        this.mockMvc.perform(get("/search")
                        .param("queueId", "AAABBBCCC")
                        .requestAttr("alwVwc", true)
                        .requestAttr("alwAdc", true)
                        .requestAttr("alwWkc", true)
                        .requestAttr("repId", "TEST")
                        .requestAttr("repDep", "TEST")
                        .requestAttr("isAdmin", true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").exists());
    }

    @Test
    public void getDocumentByQueueAPIFailureQueueNotFound() throws Exception {
        Mockito.when(caseQueueRepository.findInQueueCaseDocumentsByNativeQueryGraph(any())).thenReturn(null);
        this.mockMvc.perform(get("/search")
                        .param("queueId", "AAABBBCCC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD250404"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Queue Not Found"))
        ;
    }

    @Test
    public void getDocumentByPolicyAPISuccess() throws Exception {
        UserDetailsRes user = new UserDetailsRes("123446788", "", "test", "user");
        Mockito.when(caseService.getCaseDocumentsByPolicy("123446788")).thenReturn(cases);
        Mockito.when(ekdUserService.getByAccountNo(any())).thenReturn(user);
        this.mockMvc.perform(get("/search")
                        .param("policyNo", "123446788")
                        .requestAttr("alwVwc", true)
                        .requestAttr("alwAdc", true)
                        .requestAttr("alwWkc", true)
                        .requestAttr("repId", "TEST")
                        .requestAttr("repDep", "TEST")
                        .requestAttr("isAdmin", true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.policies[0].policyId").value("123446788"))
        ;
    }

    @Test
    public void getDocumentByPolicyAPIFailureNotFound() throws Exception {
        Mockito.when(caseService.getCaseDocumentsByPolicy("123446788")).thenThrow(
                new DomainException(HttpStatus.NOT_FOUND, EKDError.EKD350404.code(), "Policy Not Found"));
        this.mockMvc.perform(get("/search")
                        .param("policyNo", "123446788")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Policy Not Found"))
        ;
    }

    @Test
    public void getDocumentByPolicyAPIFailureNoRecordFound() throws Exception {
        Mockito.when(ekdUserService.getByAccountNo("123446788")).thenThrow(
                new DomainException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404.code(), "No record found against identifier 123446788 in EKDUSER"));
        this.mockMvc.perform(get("/search")
                        .param("policyNo", "123446788")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKDUSR404"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("No record found against identifier 123446788 in EKDUSER"))
        ;
    }

    @Test
    public void getDocumentBySnnAPISuccess() throws Exception {
        this.mockMvc.perform(get("/search")
                        .param("ssn", "123456789")
                        .requestAttr("alwVwc", true)
                        .requestAttr("alwAdc", true)
                        .requestAttr("alwWkc", true)
                        .requestAttr("repId", "TEST")
                        .requestAttr("repDep", "TEST")
                        .requestAttr("isAdmin", true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").exists())
        ;
    }

    @Test
    public void getDocumentByFirstLastNameAPISuccess() throws Exception {
//        Mockito.when(searchService.getDocumentByFirstLastName(any(), any(), new CaseOptionsReq())).thenReturn(new SSN());
        this.mockMvc.perform(get("/search")
                        .param("firstName", "test")
                        .param("lastName", "user")
                        .requestAttr("alwVwc", true)
                        .requestAttr("alwAdc", true)
                        .requestAttr("alwWkc", true)
                        .requestAttr("repId", "TEST")
                        .requestAttr("repDep", "TEST")
                        .requestAttr("isAdmin", true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").exists())
        ;
    }

    @Test
    public void getDocumentByFirstLastNameAPIFailureNoRecordFound() throws Exception {
        Mockito.when(searchService.getDocumentByFirstLastName(any(), any(), any())).thenThrow(
                new DomainException(HttpStatus.NOT_FOUND, EKDError.EKDUSR404.code(), "No record found against identifier user                          test                 in EKDUSER"));
        this.mockMvc.perform(get("/search")
                        .param("firstName", "test")
                        .param("lastName", "user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKDUSR404"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("No record found against identifier user                          test                 in EKDUSER"))
        ;
    }

    @Test
    public void getQueuedCaseDocumentsByPolicyAPISuccess() throws Exception {
        UserDetailsRes user = new UserDetailsRes("123446788", "", "test", "user");
        Mockito.when(caseService.getCaseDocumentsByPolicy("123446788")).thenReturn(cases);
        Mockito.when(ekdUserService.getByAccountNo(any())).thenReturn(user);
        this.mockMvc.perform(get("/search/work-queue")
                        .param("policyNo", "123446788")
                        .requestAttr("alwVwc", true)
                        .requestAttr("alwAdc", true)
                        .requestAttr("alwWkc", true)
                        .requestAttr("repId", "TEST")
                        .requestAttr("repDep", "TEST")
                        .requestAttr("isAdmin", true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.policies[0].policyId").value("123446788"))
        ;
    }

    @Test
    public void getQueuedCaseDocumentsBySnnAPISuccess() throws Exception {
        this.mockMvc.perform(get("/search/work-queue")
                        .param("ssn", "123456789")
                        .requestAttr("alwVwc", true)
                        .requestAttr("alwAdc", true)
                        .requestAttr("alwWkc", true)
                        .requestAttr("repId", "TEST")
                        .requestAttr("repDep", "TEST")
                        .requestAttr("isAdmin", true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").exists())
        ;
    }
}
