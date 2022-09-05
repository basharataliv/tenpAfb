package com.afba.imageplus;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.afba.imageplus.api.dto.req.PartySearch;
import com.afba.imageplus.api.dto.req.PartySearchBaseReq;
import com.afba.imageplus.api.dto.res.Benefit;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryBaseRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryRes;
import com.afba.imageplus.api.dto.res.GetBenefitSummaryResultRes;
import com.afba.imageplus.api.dto.res.GetPartyRelationshipsResultRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsBaseRes;
import com.afba.imageplus.api.dto.res.PartyRelationshipsRes;
import com.afba.imageplus.api.dto.res.PartySearchBaseRes;
import com.afba.imageplus.api.dto.res.PartySearchRes;
import com.afba.imageplus.api.dto.res.PartySearchResultRes;
import com.afba.imageplus.controller.IndexingController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.mapper.UserReqMapper;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.dto.trans.Ekd0350ToCaseResTrans;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.model.sqlserver.EKD0210Indexing;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKDUser;
import com.afba.imageplus.model.sqlserver.PROTRMPOL;
import com.afba.imageplus.repository.sqlserver.EKD0210IndexingRepository;
import com.afba.imageplus.repository.sqlserver.EKD0310DocumentRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.repository.sqlserver.LPAPPLifeProApplicationRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.CodesFlService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.FileConvertService;
import com.afba.imageplus.service.IndexingService;
import com.afba.imageplus.service.LifeProApiService;
import com.afba.imageplus.service.LifeProApisTokenService;
import com.afba.imageplus.service.LifeProApplicationService;
import com.afba.imageplus.service.PROTRMPOLService;
import com.afba.imageplus.service.PolicyService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.service.impl.IndexingServiceImpl;
import com.afba.imageplus.service.impl.LifeProApiServiceimpl;
import com.afba.imageplus.service.impl.LifeProApplicationServiceImpl;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.RestApiClientUtil;

@SpringBootTest(classes = { IndexingController.class, IndexingService.class, IndexingServiceImpl.class,
        LifeProApiService.class, LifeProApiServiceimpl.class, LifeProApplicationService.class,
        LifeProApplicationServiceImpl.class, BaseMapper.class, ErrorServiceImp.class,
        RestResponseEntityExceptionHandler.class })
@AutoConfigureMockMvc
@EnableWebMvc
@EnableSpringDataWebSupport
public class IndexingControllerTest {

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private EKD0210IndexingRepository ekd0210IndexingRepository;

    @MockBean
    private EKD0310DocumentRepository ekd0310DocumentRepository;

    @MockBean
    private LPAPPLifeProApplicationRepository lpappLifeProApplicationRepository;

    @MockBean
    private Ekd0350ToCaseResTrans ekd0350ToCaseResTrans;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private DocumentTypeService documentTypeService;

    @MockBean
    private PolicyService policyService;

    @MockBean
    private EKDUserService ekdUserService;

    @MockBean
    private CodesFlService codesFlService;

    @MockBean
    private CaseService caseService;

    @MockBean
    private CaseDocumentService caseDocumentService;

    @MockBean
    private QueueService queueService;

    @MockBean
    private PROTRMPOLService protrmpolService;

    @MockBean
    private FileConvertService fileConvertService;

    @MockBean
    private LifeProApiService lifeProApiService;

    @MockBean
    private UserReqMapper userReqMapper;

    @MockBean
    private RestApiClientUtil restApiClientUtil;

    @MockBean
    private LifeProApisTokenService lifeProApisTokenService;

    @MockBean
    private DateHelper dateHelper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private IndexingController controller;

    @Autowired
    private IndexingServiceImpl indexingServiceImpl;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertNotNull(controller);
    }

    @Test
    public void initiateIndexingAPISuccess() throws Exception {
        String request = "{\n" + "\"policyOrSsn\": \"SSN\",\n" + "\"documentId\": \"B21356AA.AAC\",\n"
                + "\"documentType\": \"SomeDoc1\",\n" + "\"documentDescription\": \"Doc Type 1\",\n"
                + "\"firstName\": \"Test\",\n" + "\"lastName\": \"Amna\",\n" + "\"middleInitial\": \"Y\"\n" + "}";
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(EKD0110DocumentType.builder().documentType("SomeDoc1")
                        .documentDescription("Doc Type 1").isAppsDoc(false).build()));
        Mockito.when(documentService.findById("B21356AA.AAC")).thenReturn(Optional.of(new EKD0310Document()));
        Mockito.when(codesFlService.generateNewSsn()).thenReturn("SSN");
        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.of(new EKDUser()));
        this.mockMvc
                .perform(post("/index/assign/policies").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value("B21356AA.AAC"));
    }

    @Test
    public void initiateIndexingAPIFailure() throws Exception {
        String request = "{\n" + "\"policyOrSsn\": \"\",\n" + "\"documentId\": \"\",\n" + "\"documentType\": \"\",\n"
                + "\"documentDescription\": \"Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\n"
                + "\"firstName\": \"Ttttttttttttttttttttt\",\n" + "\"lastName\": \"Aaaaaaaaaaaaaaaaaaaaa\",\n"
                + "\"middleInitial\": \"Yy\"\n" + "}";
        this.mockMvc
                .perform(post("/index/assign/policies").contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Document Id cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Policy or SSN cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Document type cannot be null")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Document description must not exceed 26 bytes")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of first name must be between 1-30")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of last name must be between 1-20")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of middle initial must be 1")));
    }

    @Test
    public void initiateIndexingAPIFailureDocumentTypeSize() throws Exception {
        String request = "{\n" + "\"policyOrSsn\": \"SSN\",\n" + "\"documentId\": \"B21356AA.AAC\",\n"
                + "\"documentType\": \"SomeDoc11\",\n" + "\"documentDescription\": \"amna\",\n"
                + "\"firstName\": \"Test\",\n" + "\"lastName\": \"Amna\",\n" + "\"middleInitial\": \"Y\"\n" + "}";
        this.mockMvc
                .perform(post("/index/assign/policies").contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Document type must not exceed 8 bytes"));
    }

    @Test
    public void deleteIndexRequestAPISuccess() throws Exception {
        String id = "B21356AA.AAC";
        Mockito.when(ekd0210IndexingRepository.findByDocumentId(any())).thenReturn(Optional.of(new EKD0210Indexing()));
        this.mockMvc
                .perform(delete("/index/document/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").value("Record removed from EKD0210"));
    }

    @Test
    public void deleteIndexRequestAPIFailureBlankId() throws Exception {
        Mockito.when(ekd0210IndexingRepository.findByDocumentId(any())).thenReturn(Optional.of(new EKD0210Indexing()));
        this.mockMvc.perform(delete("/index/document/{id}", "").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000500"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Id properties of entity and path variables does not match."))
        ;
    }

    @Test
    public void deleteIndexRequestAPIFailure() throws Exception {
        String id = "B21356AA.AAC";
        this.mockMvc
                .perform(delete("/index/document/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD210404")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("No indexing request exists"));
    }

    @Test
    public void getIndexRequestAPISuccess() throws Exception {
        String id = "B21356AA.AAC";
        EKD0210Indexing ekd0210Indexing = new EKD0210Indexing();
        ekd0210Indexing.setDocumentId("B21356AA.AAC");
        ekd0210Indexing.setDocumentType("SomeDoc");
        ekd0210Indexing.setIndexFlag(true);
        Mockito.when(ekd0210IndexingRepository.findByDocumentId(any())).thenReturn(Optional.of(ekd0210Indexing));
        this.mockMvc
                .perform(get("/index/document/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentType").value("SomeDoc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value("B21356AA.AAC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.indexFlag").value("true"));
    }

    @Test
    public void getIndexRequestAPIFailure() throws Exception {
        String id = "B21356AA.AAC";
        this.mockMvc
                .perform(get("/index/document/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD210404")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("No indexing request exists"));
    }

    @Test
    public void updateInUseStatusAPISuccess() throws Exception {
        String id = "B21356AA.AAC";
        String request = "{" + "\"indexFlag\": true" + "}";
        EKD0210Indexing ekd0210Indexing = new EKD0210Indexing();
        ekd0210Indexing.setDocumentId("B21356AA.AAC");
        ekd0210Indexing.setDocumentType("SomeDoc");
        ekd0210Indexing.setIndexFlag(false);
        Mockito.when(ekd0210IndexingRepository.findByDocumentId(any())).thenReturn(Optional.of(ekd0210Indexing));
        this.mockMvc
                .perform(put("/index/in-use/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentType").value("SomeDoc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value("B21356AA.AAC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.indexFlag").value("true"));
    }

    @Test
    public void updateInUseStatusAPIFailure() throws Exception {
        String id = "B21356AA.AAC";
        String request = "{" + "\"indexFlag\": true" + "}";
        EKD0210Indexing ekd0210Indexing = new EKD0210Indexing();
        ekd0210Indexing.setDocumentId("B21356AA.AAC");
        ekd0210Indexing.setDocumentType("SomeDoc");
        ekd0210Indexing.setIndexFlag(true);
        Mockito.when(ekd0210IndexingRepository.findByDocumentId(any())).thenReturn(Optional.of(ekd0210Indexing));
        this.mockMvc
                .perform(put("/index/in-use/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD210002"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Document B21356AA.AAC, already present in indexing with provided status."));
    }

//    @Test
//    public void saveLPAPPAPISuccess() throws Exception {
//        String request = "{" +
//                "\"policyId\":\"123456788\"" +
//                "}";
//        LPAPPLifeProApplication res = new LPAPPLifeProApplication();
//        res.setPolicyId("123456788");
//        res.setPsn1Ssn("asdasd");
//        res.setPsn1Type("123");
//        Mockito.when(lpappLifeProApplicationRepository.save(any())).thenReturn(res);
//        this.mockMvc.perform(post("/index/lpimage")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(request)
//                )
//                .andDo(print()).andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.policyId").value("123456788"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.psn1Ssn").value("asdasd"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.psn1Type").value("123"));
//    }

//    @Test
//    public void saveLPAPPAPIFailurePolicyIdSize() throws Exception {
//        String request = "{" +
//                "\"policyId\":\"12345678\"" +
//                "}";
//        this.mockMvc.perform(post("/index/lpimage")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(request)
//                )
//                .andDo(print()).andExpect(status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("policyId length can only be 9"));
//    }

    @Test
    public void getIndexAbleDocumentsAPISuccess() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);
        EKD0210Indexing ekd0210 = new EKD0210Indexing();
        ekd0210.setDocumentType("SomeDoc1");
        ekd0210.setDocumentId("B21356AA.AAC");
        List<EKD0210Indexing> list = Arrays.asList(ekd0210, new EKD0210Indexing());
        Page<EKD0210Indexing> page = new PageImpl<>(list, pageRequest, list.size());
        Mockito.when(ekd0210IndexingRepository.findByDocumentTypeAndIndexFlag(pageRequest, "SomeDoc1", false))
                .thenReturn(page);
        this.mockMvc
                .perform(get("/index/documents/document-types/{documentType}", "SomeDoc1")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).param("page", "0")
                        .param("size", "10"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].documentType").value("SomeDoc1"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.content[0].documentId").value("B21356AA.AAC"));
    }

    @Test
    public void getAllDocumentsPageinationAPISuccess() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);
        EKD0210Indexing ekd0210 = new EKD0210Indexing();
        ekd0210.setDocumentType("SomeDoc1");
        ekd0210.setDocumentId("B21356AA.AAC");
        List<EKD0210Indexing> list = Arrays.asList(ekd0210, new EKD0210Indexing());
        Page<EKD0210Indexing> page = new PageImpl<>(list, pageRequest, list.size());
        Mockito.when(ekd0210IndexingRepository.findAll(pageRequest)).thenReturn(page);
        Mockito.when(authorizationHelper.getAuthorizedDocumentTypeIds()).thenReturn(null);
        this.mockMvc
                .perform(get("/index/").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("page", "0").param("size", "10").requestAttr("secRange", 0))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].documentType").value("SomeDoc1"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.content[0].documentId").value("B21356AA.AAC"));
    }

    @Test
    public void getAllDocumentsPageinationAPISuccessWithDocumentTypeIds() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);
        EKD0210Indexing ekd0210 = new EKD0210Indexing();
        ekd0210.setDocumentType("SomeDoc1");
        ekd0210.setDocumentId("B21356AA.AAC");
        List<EKD0210Indexing> list = Arrays.asList(ekd0210, new EKD0210Indexing());
        Page<EKD0210Indexing> page = new PageImpl<>(list, pageRequest, list.size());
        Mockito.when(ekd0210IndexingRepository.findAllByDocumentTypeIsIn(any(), any())).thenReturn(page);
        this.mockMvc
                .perform(get("/index/").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .param("page", "0").param("size", "10").requestAttr("secRange", 0))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].documentType").value("SomeDoc1"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.content[0].documentId").value("B21356AA.AAC"));
    }

    @Test
    public void createCaseAndAddDocumentAPISuccess() throws Exception {
        String request = "{" + "\"policyId\": \"221400006\"," + "\"documentId\": \"B21356AA.AAC\"" + "}";
        EKD0150Queue queue = new EKD0150Queue();
        queue.setQueueId("AA1234");

        CaseResponse caseResponse = new CaseResponse();
        caseResponse.setCaseId("1");

        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any()))
                .thenReturn(Optional.of(EKD0110DocumentType.builder().documentType("SomeDoc1")
                        .documentDescription("Doc Type 1").isAppsDoc(false).build()));
        Mockito.when(documentService.findById("B21356AA.AAC")).thenReturn(Optional.of(new EKD0310Document()));
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(queue));
        Mockito.when(caseService.createCase(any())).thenReturn(caseResponse);
        this.mockMvc
                .perform(post("/index/assign/case-queue").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "Authorized"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.caseId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value("B21356AA.AAC"));
    }

    @Test
    public void createCaseAndAddDocumentAPIFailureWrongDocumentId() throws Exception {
        String request = "{" + "\"policyId\": \"221400006\"," + "\"documentId\": \"B21356AA.AAC\"" + "}";
        this.mockMvc
                .perform(post("/index/assign/case-queue").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "Authorized"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD310404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Document against given document ID (B21356AA.AAC) not found."));
    }

    @Test
    public void createCaseAndAddDocumentAPIFailureWrongRequestBody() throws Exception {
        String request = "{" + "\"policyId\": \"\"," + "\"documentId\": \"\"" + "}";
        this.mockMvc
                .perform(post("/index/assign/case-queue").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "Authorized"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Document Id cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Policy Id cannot be null")));
    }

    @Test
    public void indexBeneficiaryFormAPISuccess() throws Exception {
        String docName = "mock";
        String docId = "123456";
        String docUrl = "/mock.tiff";
        String docSiteId = "Mock Site";
        String docLibraryId = "Mock Library";

        EKD0150Queue queue = new EKD0150Queue();
        queue.setQueueId("AA1234");

        CaseResponse caseResponse = new CaseResponse();
        caseResponse.setCaseId("1");

        EKD0310Document document = new EKD0310Document();
        document.setDocumentId(docName);
        document.setSpDocumentId(docId);
        document.setSpDocumentUrl(docUrl);
        document.setSpDocumentSiteId(docSiteId);
        document.setSpDocumentLibraryId(docLibraryId);

        Resource fileResource = new ClassPathResource("assets/big.tif");
        MockMultipartFile file = new MockMultipartFile("doc", fileResource.getFilename(), "image/tiff",
                fileResource.getInputStream());

        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.of(new EKDUser()));
        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(new EKD0310Document()));
        Mockito.when(documentService.insert(any())).thenReturn(document);
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeleted(any(), any())).thenReturn(Optional
                .of(EKD0110DocumentType.builder().documentType("type1").documentDescription("Doc Type 1").build()));
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(queue));
        Mockito.when(caseService.createCase(any())).thenReturn(caseResponse);
        this.mockMvc
                .perform(MockMvcRequestBuilders.multipart("/index/beneficiary-form").file(file)
                        .param("documentType", "SomeDoc").param("policyId", "543215897").param("docPage", "1")
                        .accept(MediaType.APPLICATION_JSON).requestAttr("repId", "Authorized"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData")
                        .value("Beneficiary form indexed successfully"));
    }

    @Test
    public void indexBeneficiaryFormAPIFailureNoDocument() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.multipart("/index/beneficiary-form").param("documentType", "SomeDoc")
                        .param("policyId", "543215897").param("docPage", "1").accept(MediaType.APPLICATION_JSON)
                        .requestAttr("repId", "Authorized"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD310400")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("Document file is required."));
    }

    @Test
    public void indexBeneficiaryFormAPIFailureWrongRequestBody() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.multipart("/index/beneficiary-form").param("docPage", "0")
                        .accept(MediaType.APPLICATION_JSON).requestAttr("repId", "Authorized"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Policy Id cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Document Type cannot be null")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Document pages cannot be less than 1")));
    }

    @Test
    public void perfomMedicalUnderwritingAPISuccess() throws Exception {
        var req = new PartySearchBaseReq(
                new PartySearch("N", "S", "686000026", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "12345"));

        PartySearchRes partySearchResObj = new PartySearchRes();
        partySearchResObj.setName_id("123456");
        List<PartySearchRes> partySearchRes = Arrays.asList(partySearchResObj);

        PartyRelationshipsRes partyRelationshipsResObj = new PartyRelationshipsRes();
        partyRelationshipsResObj.setRelateCode("IN");
        partyRelationshipsResObj.setContractCode("S");
        partyRelationshipsResObj.setProductCode("AA");
        partyRelationshipsResObj.setFaceAmount(6000.0);
        List<PartyRelationshipsRes> partyRelationshipsRes = Arrays.asList(partyRelationshipsResObj);

        GetBenefitSummaryRes infoRes = new GetBenefitSummaryRes();
        infoRes.setSexCode("F");
        infoRes.setUnderwritingClass("S");
        infoRes.setFaceAmount(6000.0);
        infoRes.setBenefit(new Benefit(11, "BA", 1));

        Mockito.when(lifeProApiService.partySearchDetails(any()))
                .thenReturn(new PartySearchBaseRes(new PartySearchResultRes(partySearchRes, null, 0, "test")));
        Mockito.when(lifeProApiService.PartyRelationships(any())).thenReturn(new PartyRelationshipsBaseRes(
                new GetPartyRelationshipsResultRes(partyRelationshipsRes, null, 0, "test")));
        Mockito.when(lifeProApiService.getBenefitSummary(any())).thenReturn(
                new GetBenefitSummaryBaseRes(new GetBenefitSummaryResultRes(List.of(infoRes), "", 00, "abc")));
        String request = "{" + "\"policyId\": \"220300004\"," + "\"ssn\":\"123456789\"," + "\"policyType\":\"LT\","
                + "\"age\":12," + "\"appCoverageAmount\":5500.0," + "\"activeFlag\":1," + "\"deployFlag\":false,"
                + "\"ltEspFlag\":false," + "\"templateName\":\"564\"," + "\"overallCoverage\":6000.0,"
                + "\"totalProductCoverage\":6000.0" + "}";
        PROTRMPOL protrmpol = new PROTRMPOL();
        protrmpol.setCovAmt(22.2);
        List<PROTRMPOL> list = new ArrayList<>();
        list.add(protrmpol);
        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.of(new EKDUser()));
        Mockito.when(protrmpolService.getbyNewPolicyId(any())).thenReturn(list);
        this.mockMvc
                .perform(post("/index/medical-underwriting").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.overallFlag").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.productFlag").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.homeOfficeFlag").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.paramediFlag").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.ekgFlag").exists());
    }

    @Test
    public void perfomMedicalUnderwritingAPIFailureWrongRequestBodySizes() throws Exception {
        String request = "{" + "\"policyId\":\"220340011004\"," + "\"ssn\":\"1234567890\","
                + "\"policyType\":\"LTtrr\"," + "\"age\":100," + "\"appCoverageAmount\":55000.0," + "\"activeFlag\":2,"
                + "\"deployFlag\":false," + "\"ltEspFlag\":false," + "\"templateName\":\"56432112345\","
                + "\"overallCoverage\":6000.0," + "\"totalProductCoverage\":6000.0" + "}";
        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.of(new EKDUser()));
        this.mockMvc
                .perform(post("/index/medical-underwriting").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of policy id cannot exceed 11")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Length of ssn must be 9")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Length of policy type cannot exceed 4")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Age cannot be greater than 99")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("App coverage amount can be 4 digit integer and 3 digit decimal")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Acive flag can only be 0 or 1")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Length of template name cannot exceed 10")));
    }

    @Test
    public void perfomMedicalUnderwritingAPIFailureWrongRequestBodyBlanks() throws Exception {
        String request = "{" + "\"policyId\":\"2203400004\"," + "\"overallCoverage\":6000.0,"
                + "\"totalProductCoverage\":6000.0" + "}";
        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.of(new EKDUser()));
        this.mockMvc
                .perform(post("/index/medical-underwriting").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("SSN can not be null or empty")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Policy type can not be null or empty")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Age can not be null")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("App coverage amount can not be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Acive flag can not be null")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("Deploy flag can only be true or false")))
                .andExpect(
                        MockMvcResultMatchers.content().string(containsString("LT ESP flag can only be true or false")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("Template name can not be null or empty")));
    }

    @Test
    public void perfomMedicalUnderwritingAPIFailureWrongPolicyId() throws Exception {
        var req = new PartySearchBaseReq(
                new PartySearch("N", "S", "686000026", "UNIQUE_NUMBER_PER_TRANSACTION", "string", "12345"));

        PartySearchRes partySearchResObj = new PartySearchRes();
        partySearchResObj.setName_id("123456");
        List<PartySearchRes> partySearchRes = Arrays.asList(partySearchResObj);

        PartyRelationshipsRes partyRelationshipsResObj = new PartyRelationshipsRes();
        partyRelationshipsResObj.setRelateCode("IN");
        partyRelationshipsResObj.setContractCode("S");
        partyRelationshipsResObj.setProductCode("AA");
        partyRelationshipsResObj.setFaceAmount(6000.0);
        List<PartyRelationshipsRes> partyRelationshipsRes = Arrays.asList(partyRelationshipsResObj);

        GetBenefitSummaryRes infoRes = new GetBenefitSummaryRes();
        infoRes.setSexCode("F");
        infoRes.setUnderwritingClass("S");
        infoRes.setFaceAmount(6000.0);
        infoRes.setBenefit(new Benefit(11, "BA", 1));

        Mockito.when(lifeProApiService.partySearchDetails(any()))
                .thenReturn(new PartySearchBaseRes(new PartySearchResultRes(partySearchRes, null, 0, "test")));
        Mockito.when(lifeProApiService.PartyRelationships(any())).thenReturn(new PartyRelationshipsBaseRes(
                new GetPartyRelationshipsResultRes(partyRelationshipsRes, null, 0, "test")));
        Mockito.when(lifeProApiService.getBenefitSummary(any())).thenReturn(
                new GetBenefitSummaryBaseRes(new GetBenefitSummaryResultRes(List.of(infoRes), "", 00, "abc")));

        String request = "{" + "\"policyId\": \"220300004\"," + "\"ssn\":\"123456789\"," + "\"policyType\":\"LT\","
                + "\"age\":12," + "\"appCoverageAmount\":5500.0," + "\"activeFlag\":1," + "\"deployFlag\":false,"
                + "\"ltEspFlag\":false," + "\"templateName\":\"564\"," + "\"overallCoverage\":6000.0,"
                + "\"totalProductCoverage\":6000.0" + "}";
        this.mockMvc
                .perform(post("/index/medical-underwriting").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKDUSR404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("No record found against identifier 220300004 in EKDUSER"));
    }

    @Test
    public void getDocumentCountAPISuccess() throws Exception {
        List<String[][]> list = new ArrayList<>();
        Mockito.when(ekd0210IndexingRepository.findAllDocumentTypesWithCount()).thenReturn(list);
        Mockito.when(authorizationHelper.getAuthorizedDocumentTypeIds()).thenReturn(null);
        this.mockMvc
                .perform(get("/index/count").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"));
    }

    @Test
    public void getDocumentCountAPISuccessWithDocumentTypeIds() throws Exception {
        List<String[][]> list = new ArrayList<>();
        Mockito.when(ekd0210IndexingRepository.findDocumentTypesWithCount(any())).thenReturn(list);
        this.mockMvc
                .perform(get("/index/count").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"));
    }

}
