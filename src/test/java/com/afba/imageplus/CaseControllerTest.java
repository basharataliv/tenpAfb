package com.afba.imageplus;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import com.afba.imageplus.dto.mapper.CaseCommentResMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.afba.imageplus.controller.CaseController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.CaseCreateReq;
import com.afba.imageplus.dto.req.CaseUpdateReq;
import com.afba.imageplus.dto.req.Ekd0116Req;
import com.afba.imageplus.dto.res.CaseResponse;
import com.afba.imageplus.dto.trans.Ekd0350ToCaseResTrans;
import com.afba.imageplus.model.sqlserver.EKD0050NextCase;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.model.sqlserver.EKD0150Queue;
import com.afba.imageplus.model.sqlserver.EKD0250CaseQueue;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.model.sqlserver.EKD0370PendCase;
import com.afba.imageplus.model.sqlserver.EKD0850CaseInUse;
import com.afba.imageplus.model.sqlserver.Enum.CaseStatus;
import com.afba.imageplus.repository.sqlserver.EKD0050NextCaseRepository;
import com.afba.imageplus.repository.sqlserver.EKD0315CaseDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0350CaseRepository;
import com.afba.imageplus.repository.sqlserver.EKD0370PendCaseRepository;
import com.afba.imageplus.repository.sqlserver.EKD0850CaseInUseRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.AFB0660Service;
import com.afba.imageplus.service.AuthorizationCacheService;
import com.afba.imageplus.service.BAENDORSEService;
import com.afba.imageplus.service.CaseDocumentService;
import com.afba.imageplus.service.CaseInUseService;
import com.afba.imageplus.service.CaseQueueService;
import com.afba.imageplus.service.CaseService;
import com.afba.imageplus.service.DocumentService;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.EKDUserService;
import com.afba.imageplus.service.EMSITIFFService;
import com.afba.imageplus.service.GetLPOInfoService;
import com.afba.imageplus.service.ICRFileService;
import com.afba.imageplus.service.LPAUTOISSService;
import com.afba.imageplus.service.LifeProApiService;
import com.afba.imageplus.service.MOVECASEHService;
import com.afba.imageplus.service.PNDDOCTYPService;
import com.afba.imageplus.service.PendCaseService;
import com.afba.imageplus.service.QCRUNHISService;
import com.afba.imageplus.service.QueueService;
import com.afba.imageplus.service.UserProfileService;
import com.afba.imageplus.service.impl.CaseServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.afba.imageplus.utilities.StringHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest(classes = { CaseController.class, CaseService.class, CaseServiceImpl.class, BaseMapper.class,
        ErrorServiceImp.class, DateHelper.class, RestResponseEntityExceptionHandler.class })
@AutoConfigureMockMvc
@EnableWebMvc
@EnableSpringDataWebSupport
public class CaseControllerTest {

    @Autowired
    private CaseController controller;

    @MockBean
    private EKD0350CaseRepository ekd0350CaseRepository;

    @MockBean
    private EKD0050NextCaseRepository ekd0050NextCaseRepository;

    @MockBean
    private Ekd0350ToCaseResTrans dtoTrans;

    @MockBean
    private CaseDocumentService caseDocumentService;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private EKD0850CaseInUseRepository ekd0850CaseInUseRepository;

    @MockBean
    private EKD0315CaseDocumentRepository ekd0315CaseDocumentRepository;

    @MockBean
    private CaseQueueService caseQueueService;

    @MockBean
    private QueueService queueService;

    @MockBean
    private StringHelper stringHelper;

    @MockBean
    private UserProfileService userProfileService;

    @MockBean
    private CaseInUseService caseInUseService;

    @MockBean
    private PendCaseService pendCaseService;

    @MockBean
    private LifeProApiService lifeProApiService;

    @MockBean
    private PNDDOCTYPService pnddoctypService;

    @MockBean
    private DocumentTypeService documentTypeService;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @SpyBean
    private CaseService caseService;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private BAENDORSEService baendorseService;

    @MockBean
    private EKDUserService ekdUserService;

    @MockBean
    private EMSITIFFService emsitiffService;

    @MockBean
    private AFB0660Service afb0660Service;

    @MockBean
    private MOVECASEHService movecasehService;

    @MockBean
    private LPAUTOISSService lpautoissService;

    @MockBean
    private ICRFileService icrFileService;

    @MockBean
    private DateHelper dateHelper;

    @MockBean
    private EKD0370PendCaseRepository ekd0370PendCaseRepository;

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErrorRepository errorRepository;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @MockBean
    private QCRUNHISService qcrunhisService;

    @MockBean
    private GetLPOInfoService getLPOInfoService;

    @MockBean
    private CaseCommentResMapper caseCommentResMapper;

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
    public void createCaseAPISuccess() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().addModule(new Jdk8Module()).addModule(new JavaTimeModule()).build();
        CaseCreateReq body = new CaseCreateReq();
        body.setInitialRepId("afba");
        body.setLastRepId("afba");
        body.setScanningDate(LocalDate.now());
        body.setScanningTime(LocalTime.now());
        body.setCmAccountNumber("197564982");
        body.setCmFormattedName("21 BENEFICIARY ACTION ");
        body.setChargeBackFlag("1");
        body.setFiller("filler");

        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setCaseId("1");
        caseRes.setDateLastUpdate(LocalDate.now());
        caseRes.setTimeLastUpdate(LocalTime.now());
        caseRes.setCmFormattedName("abc");
        caseRes.setCmAccountNumber("01 APPLICATIONS");
        caseRes.setStatus(CaseStatus.U);

        EKD0050NextCase nextCase = new EKD0050NextCase();
        nextCase.setId(1L);
        nextCase.setNextCase("000000001");

        EKD0150Queue queue = new EKD0150Queue();
        queue.setCaseDescription("21 BENEFICIARY ACTION");

        EKD0850CaseInUse caseInUse = new EKD0850CaseInUse();
        caseInUse.setQRepId("");

        Mockito.when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(nextCase));
        Mockito.when(ekd0850CaseInUseRepository.findByCaseId(any())).thenReturn(Optional.of(caseInUse));
        Mockito.when(stringHelper.addNumberToNumericString(any(), any())).thenReturn("000000002");
        Mockito.when(ekd0050NextCaseRepository.save(any())).thenReturn(nextCase);
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(queue));
//        Mockito.when(dateHelper.reformateDate((LocalDate) any(), any())).thenReturn("1111111111");
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(caseRes));
        Mockito.when(dateHelper.getDateTimeFormatFromStringCombination(any(), any())).thenReturn(new Date());
        Mockito.when(dtoTrans.caseEntityToCaseRes(Mockito.any(EKD0350Case.class)))
                .thenReturn(new CaseResponse("1", null, null, "1234", null, null, CaseStatus.N, null, null, "AA1234",
                        "abc", null, null, null, null, null, null, null, null));
        Mockito.when(ekd0350CaseRepository.save(any())).thenReturn(caseRes);
        this.mockMvc
                .perform(post("/cases").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.caseId").value("1"));
    }

    @Test
    public void createCaseAPIFailureQueueProfileNotFound() throws Exception {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new Jdk8Module()).addModule(new JavaTimeModule()).build();
        CaseCreateReq body = new CaseCreateReq();
        body.setInitialQueueId("Authorized");
        body.setInitialRepId("afba");
        body.setLastRepId("afba");
        body.setScanningDate(LocalDate.now());
        body.setScanningTime(LocalTime.now());
        body.setCmAccountNumber("197564982");
        body.setCmFormattedName("21 BENEFICIARY ACTION ");
        body.setChargeBackFlag("1");
        body.setFiller("filler");

        EKD0050NextCase nextCase = new EKD0050NextCase();
        nextCase.setId(1L);
        nextCase.setNextCase("000000001");

        Mockito.when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(nextCase));
        Mockito.when(stringHelper.addNumberToNumericString(any(), any())).thenReturn("000000002");
        Mockito.when(ekd0050NextCaseRepository.save(any())).thenReturn(nextCase);
        Mockito.when(queueService.findById(any())).thenReturn(Optional.empty());

        this.mockMvc
                .perform(post("/cases").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD150404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Queue profile not found against ID Authorized."));
    }

    @Test
    public void createCaseAPIFailureCaseDescriptionNotMatched() throws Exception {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new Jdk8Module()).addModule(new JavaTimeModule()).build();
        CaseCreateReq body = new CaseCreateReq();
        body.setInitialQueueId("Authorized");
        body.setInitialRepId("afba");
        body.setLastRepId("afba");
        body.setScanningDate(LocalDate.now());
        body.setScanningTime(LocalTime.now());
        body.setCmAccountNumber("197564982");
        body.setCmFormattedName("21 BENEFICIARY ACTION");
        body.setChargeBackFlag("1");
        body.setFiller("filler");

        EKD0050NextCase nextCase = new EKD0050NextCase();
        nextCase.setId(1L);
        nextCase.setNextCase("000000001");

        EKD0150Queue queue = new EKD0150Queue();
        queue.setCaseDescription("197564982");

        Mockito.when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(nextCase));
        Mockito.when(stringHelper.addNumberToNumericString(any(), any())).thenReturn("000000002");
        Mockito.when(ekd0050NextCaseRepository.save(any())).thenReturn(nextCase);
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(queue));

        this.mockMvc
                .perform(post("/cases").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD150405")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case description not matched"));
    }

    @Test
    public void createCaseAPIFailureCaseNotFound() throws Exception {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new Jdk8Module()).addModule(new JavaTimeModule()).build();
        CaseCreateReq body = new CaseCreateReq();
        body.setInitialQueueId("Authorized");
        body.setInitialRepId("afba");
        body.setLastRepId("afba");
        body.setScanningDate(LocalDate.now());
        body.setScanningTime(LocalTime.now());
        body.setCmAccountNumber("197564982");
        body.setCmFormattedName("21 BENEFICIARY ACTION ");
        body.setChargeBackFlag("1");
        body.setFiller("filler");

        EKD0050NextCase nextCase = new EKD0050NextCase();
        nextCase.setId(1L);
        nextCase.setNextCase("000000001");

        EKD0150Queue queue = new EKD0150Queue();
        queue.setCaseDescription("21 BENEFICIARY ACTION");

        Mockito.when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(nextCase));
        Mockito.when(stringHelper.addNumberToNumericString(any(), any())).thenReturn("000000002");
        Mockito.when(ekd0050NextCaseRepository.save(any())).thenReturn(nextCase);
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(queue));
        Mockito.when(dateHelper.reformateDate((LocalDate) any(), any())).thenReturn("1111111111");

        this.mockMvc
                .perform(post("/cases").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void createCaseAPIFailureInvalidCaseStatus() throws Exception {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new Jdk8Module()).addModule(new JavaTimeModule()).build();
        CaseCreateReq body = new CaseCreateReq();
        body.setInitialQueueId("Authorized");
        body.setInitialRepId("afba");
        body.setLastRepId("afba");
        body.setScanningDate(LocalDate.now());
        body.setScanningTime(LocalTime.now());
        body.setCmAccountNumber("197564982");
        body.setCmFormattedName("21 BENEFICIARY ACTION ");
        body.setChargeBackFlag("1");
        body.setFiller("filler");

        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setCaseId("1");
        caseRes.setDateLastUpdate(LocalDate.now());
        caseRes.setTimeLastUpdate(LocalTime.now());
        caseRes.setCmFormattedName("01");
        caseRes.setCmAccountNumber("01 APPLICATIONS");
        caseRes.setStatus(CaseStatus.valueOf("C"));

        EKD0050NextCase nextCase = new EKD0050NextCase();
        nextCase.setId(1L);
        nextCase.setNextCase("000000001");

        EKD0150Queue queue = new EKD0150Queue();
        queue.setCaseDescription("21 BENEFICIARY ACTION");

        Mockito.when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(nextCase));
        Mockito.when(stringHelper.addNumberToNumericString(any(), any())).thenReturn("000000002");
        Mockito.when(ekd0050NextCaseRepository.save(any())).thenReturn(nextCase);
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(queue));
        Mockito.when(dateHelper.reformateDate((LocalDate) any(), any())).thenReturn("");
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(caseRes));

        this.mockMvc
                .perform(post("/cases").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350002"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Case status not valid for ENQUEUE"));
    }

    @Test
    public void createCaseAPIFailureQueueProfileNotFoundENQUE() throws Exception {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new Jdk8Module()).addModule(new JavaTimeModule()).build();
        CaseCreateReq body = new CaseCreateReq();
        body.setInitialQueueId("Authorized");
        body.setInitialRepId("afba");
        body.setLastRepId("afba");
        body.setScanningDate(LocalDate.now());
        body.setScanningTime(LocalTime.now());
        body.setCmAccountNumber("197564982");
        body.setCmFormattedName("21 BENEFICIARY ACTION ");
        body.setChargeBackFlag("1");
        body.setFiller("filler");

        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setCaseId("1");
        caseRes.setDateLastUpdate(LocalDate.now());
        caseRes.setTimeLastUpdate(LocalTime.now());
        caseRes.setCmFormattedName("abc");
        caseRes.setCmAccountNumber("01 APPLICATIONS");
        caseRes.setStatus(CaseStatus.A);

        EKD0050NextCase nextCase = new EKD0050NextCase();
        nextCase.setId(1L);
        nextCase.setNextCase("000000001");

        EKD0150Queue queue = new EKD0150Queue();
        queue.setCaseDescription("21 BENEFICIARY ACTION");

        Mockito.when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(nextCase));
        Mockito.when(stringHelper.addNumberToNumericString(any(), any())).thenReturn("000000002");
        Mockito.when(ekd0050NextCaseRepository.save(any())).thenReturn(nextCase);
        Mockito.when(queueService.findById(any())).thenReturn(Optional.empty());
        Mockito.when(dateHelper.reformateDate((LocalDate) any(), any())).thenReturn("");
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(caseRes));

        this.mockMvc
                .perform(post("/cases").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD150404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Queue profile not found against ID Authorized."));
    }

    @Test
    public void createCaseAPIFailureQueueCannotBeParsed() throws Exception {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new Jdk8Module()).addModule(new JavaTimeModule()).build();
        CaseCreateReq body = new CaseCreateReq();
        body.setInitialQueueId("Authorized");
        body.setInitialRepId("afba");
        body.setLastRepId("afba");
        body.setScanningDate(LocalDate.now());
        body.setScanningTime(LocalTime.now());
        body.setCmAccountNumber("197564982");
        body.setCmFormattedName("21 BENEFICIARY ACTION ");
        body.setChargeBackFlag("1");
        body.setFiller("filler");

        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setCaseId("1");
        caseRes.setDateLastUpdate(LocalDate.now());
        caseRes.setTimeLastUpdate(LocalTime.now());
        caseRes.setCmFormattedName("abc");
        caseRes.setCmAccountNumber("01 APPLICATIONS");
        caseRes.setStatus(CaseStatus.A);

        EKD0050NextCase nextCase = new EKD0050NextCase();
        nextCase.setId(1L);
        nextCase.setNextCase("000000001");

        EKD0150Queue queue = new EKD0150Queue();
        queue.setCaseDescription("21 BENEFICIARY ACTION");

        Mockito.when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(nextCase));
        Mockito.when(stringHelper.addNumberToNumericString(any(), any())).thenReturn("000000002");
        Mockito.when(ekd0050NextCaseRepository.save(any())).thenReturn(nextCase);
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(queue));
        Mockito.when(dateHelper.reformateDate((LocalDate) any(), any())).thenReturn("1111111111");
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(caseRes));
        Mockito.when(dateHelper.getDateTimeFormatFromStringCombination(any(), any())).thenReturn(null);

        this.mockMvc
                .perform(post("/cases").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD250422"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("QPRIORTY VAL cannot be parsed"));
    }

    @Test
    public void createCaseAPIFailureQrepIdDoesntMatch() throws Exception {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new Jdk8Module()).addModule(new JavaTimeModule()).build();
        CaseCreateReq body = new CaseCreateReq();
        body.setInitialQueueId("Authorized");
        body.setInitialRepId("afba");
        body.setLastRepId("afba");
        body.setScanningDate(LocalDate.now());
        body.setScanningTime(LocalTime.now());
        body.setCmAccountNumber("197564982");
        body.setCmFormattedName("21 BENEFICIARY ACTION ");
        body.setChargeBackFlag("1");
        body.setFiller("filler");

        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setCaseId("1");
        caseRes.setDateLastUpdate(LocalDate.now());
        caseRes.setTimeLastUpdate(LocalTime.now());
        caseRes.setCmFormattedName("abc");
        caseRes.setCmAccountNumber("01 APPLICATIONS");
        caseRes.setStatus(CaseStatus.U);

        EKD0050NextCase nextCase = new EKD0050NextCase();
        nextCase.setId(1L);
        nextCase.setNextCase("000000001");

        EKD0150Queue queue = new EKD0150Queue();
        queue.setCaseDescription("21 BENEFICIARY ACTION");

        EKD0850CaseInUse caseInUse = new EKD0850CaseInUse();
        caseInUse.setQRepId("1");

        Mockito.when(ekd0050NextCaseRepository.findFirstBy()).thenReturn(Optional.of(nextCase));
        Mockito.when(ekd0850CaseInUseRepository.findByCaseId(any())).thenReturn(Optional.of(caseInUse));
        Mockito.when(stringHelper.addNumberToNumericString(any(), any())).thenReturn("000000002");
        Mockito.when(ekd0050NextCaseRepository.save(any())).thenReturn(nextCase);
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(queue));
        Mockito.when(dateHelper.reformateDate((LocalDate) any(), any())).thenReturn("1111111111");
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(caseRes));
        Mockito.when(dateHelper.getDateTimeFormatFromStringCombination(any(), any())).thenReturn(new Date());

        this.mockMvc
                .perform(post("/cases").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD850401"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("QrepId id doesnt match id provided"));
    }

    @Test
    public void deleteCaseAPISuccess() throws Exception {
        EKD0350Case test = new EKD0350Case();
        test.setStatus(CaseStatus.C);

        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(false);
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(test));
        Mockito.when(ekd0315CaseDocumentRepository.existsByCaseId(any())).thenReturn(false);
        this.mockMvc
                .perform(delete("/cases/{caseId}", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").value("Case is deleted"));
    }

    @Test
    public void deleteCaseAPIFailureAnotherUserWorking() throws Exception {
        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(true);
        this.mockMvc
                .perform(delete("/cases/{caseId}", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD850409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Another user is working on case. Please try later."));
    }

    @Test
    public void deleteCaseAPIFailureCaseNotFound() throws Exception {
        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(false);
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.empty());
        this.mockMvc
                .perform(delete("/cases/{caseId}", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void deleteCaseAPIFailureCaseStatus() throws Exception {
        EKD0350Case test = new EKD0350Case();
        test.setStatus(CaseStatus.N);

        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(false);
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(test));
        this.mockMvc
                .perform(delete("/cases/{caseId}", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("CaseStatus is : N"));
    }

    @Test
    public void deleteCaseAPIFailureRecordExists() throws Exception {
        EKD0350Case test = new EKD0350Case();
        test.setStatus(CaseStatus.C);

        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(false);
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(test));
        Mockito.when(ekd0315CaseDocumentRepository.existsByCaseId(any())).thenReturn(true);
        this.mockMvc
                .perform(delete("/cases/{caseId}", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Record is found"));
    }

    @Test
    public void updateCaseAPISuccess() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().addModule(new Jdk8Module()).addModule(new JavaTimeModule()).build();
        CaseUpdateReq body = new CaseUpdateReq();
        body.setStatus(CaseStatus.C);
        body.setCaseCloseDate(LocalDate.now());
        body.setCaseCloseTime(LocalTime.now());
        body.setInitialRepId("afba");
        body.setLastRepId("afba");
        body.setScanningDate(LocalDate.now());
        body.setScanningTime(LocalTime.now());
        body.setCmAccountNumber("");
        body.setCmFormattedName("");
        body.setDateLastUpdate(LocalDate.now());
        body.setTimeLastUpdate(LocalTime.now());
        body.setChargeBackFlag("1");
        body.setFiller("filler");

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(new EKD0350Case()));

        this.mockMvc
                .perform(put("/cases/{caseId}", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void updateCaseAPIFailureCaseNotFound() throws Exception {
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.empty());
        this.mockMvc
                .perform(put("/cases/{caseId}", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content("{}"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void getCaseByIdAPISuccess() throws Exception {
        CaseResponse caseRes = new CaseResponse();
        caseRes.setCaseId("1");
        caseRes.setInitialQueueId("1234");
        caseRes.setStatus(CaseStatus.N);
        caseRes.setCmAccountNumber("AA1234");
        caseRes.setCmFormattedName("abc");
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(new EKD0350Case()));
        Mockito.when(dtoTrans.caseEntityToCaseRes(any())).thenReturn(caseRes);

        this.mockMvc
                .perform(get("/cases/{caseId}", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.caseId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.initialQueueId").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("N"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cmAccountNumber").value("AA1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cmFormattedName").value("abc"));
    }

    @Test
    public void getCasesAPISuccess() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCaseId("B21356AA.AAC");
        ekd0350Case.setStatus(CaseStatus.N);
        List<EKD0350Case> list = Arrays.asList(ekd0350Case, new EKD0350Case());
        Page<EKD0350Case> page = new PageImpl<>(list, pageable, list.size());

        Map<String, Object> filters = new HashMap<>();

        Mockito.when(ekd0350CaseRepository.findAll(pageable)).thenReturn(page);
        Mockito.doReturn(page).when(caseService).findAll(pageable, filters);
        this.mockMvc
                .perform(get("/cases").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .requestAttr("filters", filters))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].caseId").value("B21356AA.AAC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].status").value("N"));
    }

    @Test
    public void getAllDocumentsFileAPISuccess() throws Exception {
        Resource fileResource = new ClassPathResource("assets/big.tif");
        MockMultipartFile tiffFile = new MockMultipartFile("doc", fileResource.getFilename(), "image/tiff",
                fileResource.getInputStream());
        Mockito.when(documentService.downloadDocumentsFile(any())).thenReturn(tiffFile.getBytes());
        EKD0315CaseDocument ekd0315CaseDocument = new EKD0315CaseDocument();
        List<EKD0315CaseDocument> list = new ArrayList<EKD0315CaseDocument>();
        list.add(ekd0315CaseDocument);
        Mockito.when(caseDocumentService.getDocumentsByCaseId(any())).thenReturn(list);

        this.mockMvc.perform(get("/cases/{caseId}/documents/content", 50).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_PDF_VALUE)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getAllDocumentsFileAPIFailure() throws Exception {
        this.mockMvc.perform(get("/cases/{caseId}/documents/content", 50).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    public void closeCaseAPISuccess() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setScanningDateTime(LocalDateTime.now());
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));

        this.mockMvc
                .perform(post("/cases/{caseId}/closeflr", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case is Closed"));
    }

    @Test
    public void closeCaseAPIFailureCaseCannotBeClosed() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.C);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));

        this.mockMvc
                .perform(post("/cases/{caseId}/closeflr", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("CaseStatus is : C"));
    }

    @Test
    public void closeCaseAPIFailureCaseNotFound() throws Exception {
        this.mockMvc
                .perform(post("/cases/{caseId}/closeflr", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void enqueueCaseAPISuccess() throws Exception {
        String request = "{" + "\"userId\":null," + "\"queueId\":\"QAQUE\"," + "\"combination\":\"020190101080000\""
                + "}";

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(new EKD0150Queue()));
        Mockito.when(dateHelper.getDateTimeFormatFromStringCombination(any(), any())).thenReturn(new Date());

        this.mockMvc
                .perform(post("/cases/{caseId}/enqflr", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.caseStatus").value("N"));
    }

    @Test
    public void enqueueCaseAPIFailureWrongBody() throws Exception {
        String request = "{" + "\"userId\":null," + "\"combination\":\"00190101080000\"" + "}";

        this.mockMvc
                .perform(post("/cases/{caseId}/enqflr", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("QueueID should not be empty.")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(containsString("queue priority value must be 15 characters")));
    }

    @Test
    public void enqueueCaseAPIFailureQueueNotFound() throws Exception {
        String request = "{" + "\"userId\":null," + "\"queueId\":\"QAQUE\"," + "\"combination\":\"020190101080000\""
                + "}";

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));

        this.mockMvc
                .perform(post("/cases/{caseId}/enqflr", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD150404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Queue profile not found against ID QAQUE."));
    }

    @Test
    public void removeCaseFromQueueAPISuccess() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));
        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(false);
        Mockito.when(caseQueueService.findByCaseId(any())).thenReturn(Optional.of(new EKD0250CaseQueue()));

        this.mockMvc
                .perform(post("/cases/{caseId}/deqflr", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.caseStatus").value("N"));
    }

    @Test
    public void removeCaseFromQueueAPIFailureCaseNotFound() throws Exception {

        this.mockMvc
                .perform(post("/cases/{caseId}/deqflr", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void removeCaseFromQueueAPIFailureCaseInUse() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));
        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(true);

        this.mockMvc
                .perform(post("/cases/{caseId}/deqflr", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD850409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Another user is working on case. Please try later."));
    }

    @Test
    public void removeCaseFromQueueAPIFailureQueueNotound() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));

        this.mockMvc
                .perform(post("/cases/{caseId}/deqflr", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD250404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Queue not found"));
    }

    @Test
    public void workCaseAPISuccess() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));
        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(false);

        this.mockMvc
                .perform(post("/cases/{caseId}/work", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.userId").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.caseId").value("50"));
    }

    @Test
    public void workCaseAPIFailureCaseStatusNotValid() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.C);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));
        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(false);

        this.mockMvc
                .perform(post("/cases/{caseId}/work", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350003"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Case status is not valid for work a case"));
    }

    @Test
    public void workCaseAPIFailure() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));
        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(true);

        this.mockMvc
                .perform(post("/cases/{caseId}/work", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD850409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Another user is working on case. Please try later."));
    }

    @Test
    public void releaseCaseAPISuccess() throws Exception {
        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setScanningDate(today);
        ekd0350Case.setScanningTime(time);

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setQueueId("QAQUE");
        ekd0850CaseInUse.setQRepId("1234");

        Mockito.when(caseInUseService.findByIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(new EKD0150Queue()));
        Mockito.when(dateHelper.getDateTimeFormatFromStringCombination(any(), any())).thenReturn(new Date());

        this.mockMvc
                .perform(post("/cases/{caseId}/release", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.userId").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.caseId").value("50"));
    }

    @Test
    public void releaseCaseAPIFailure() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setQueueId("QAQUE");
        ekd0850CaseInUse.setQRepId("123");

        Mockito.when(caseInUseService.findByIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));

        this.mockMvc
                .perform(post("/cases/{caseId}/release", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD850409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Another user is working on case. Please try later."));
    }

    @Test
    public void pendCaseWhenTransferringCaseToEMSIAPISuccess() throws Exception {
        String request = "{" + "\"returnQueueId\" : \"APPSEMSIBA\"," + "\"targetQueue\" : \"GOTOEMSIQ\"" + "}";

        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setScanningDate(today);
        ekd0350Case.setScanningTime(time);

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setQueueId("QAQUE");
        ekd0850CaseInUse.setQRepId("1234");

        Mockito.when(caseInUseService.findByIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);
        Mockito.when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(ekd0350Case));

        this.mockMvc
                .perform(post("/cases/{caseId}/pend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.returnQueueId").value("QAQUE"));
    }

    @Test
    public void pendCaseForDocTypeAPISuccess() throws Exception {
        String request = "{" + "\"returnQueueId\" : \"APPSEMSIBA\"," + "\"pendDocType\" : \"ACCT90\"" + "}";

        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setScanningDate(today);
        ekd0350Case.setScanningTime(time);

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setQueueId("QAQUE");
        ekd0850CaseInUse.setQRepId("1234");

        EKD0110DocumentType ekd0110DocumentType = new EKD0110DocumentType();
        ekd0110DocumentType.setDocumentType("ACCT90");
        ekd0110DocumentType.setDefaultQueueId("IMAGHLDQ");
        ekd0110DocumentType.setDefaultSuspendDays(321);

        Mockito.when(caseInUseService.findByIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);
        Mockito.when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(ekd0350Case));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeletedOrElseThrow(any(), any()))
                .thenReturn(ekd0110DocumentType);

        this.mockMvc
                .perform(post("/cases/{caseId}/pend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.returnQueueId").value("APPSEMSIBA"));
    }

    @Test
    public void pendCaseForDocTypeAPIFailureDocumentTypeDefaultSuspendDays() throws Exception {
        String request = "{" + "\"returnQueueId\" : \"APPSEMSIBA\"," + "\"pendDocType\" : \"ACCT90\"" + "}";

        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setScanningDate(today);
        ekd0350Case.setScanningTime(time);

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setQueueId("QAQUE");
        ekd0850CaseInUse.setQRepId("1234");

        EKD0110DocumentType ekd0110DocumentType = new EKD0110DocumentType();
        ekd0110DocumentType.setDocumentType("ACCT90");
        ekd0110DocumentType.setDefaultQueueId("IMAGHLDQ");
        ekd0110DocumentType.setDefaultSuspendDays(0);

        Mockito.when(caseInUseService.findByIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);
        Mockito.when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(ekd0350Case));
        Mockito.when(documentTypeService.findByDocumentTypeAndIsDeletedOrElseThrow(any(), any()))
                .thenReturn(ekd0110DocumentType);

        this.mockMvc
                .perform(post("/cases/{caseId}/pend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD110406"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("DocumentType Default Suspend Days must be greater than 0"));
    }

    @Test
    public void pendCaseForReleaseDateAPISuccess() throws Exception {
        String request = "{" + "\"returnQueueId\" : \"APPSEMSIBA\"," + "\"pendReleaseDate\" : \"20230220\"" + "}";

        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setScanningDate(today);
        ekd0350Case.setScanningTime(time);

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setQueueId("QAQUE");
        ekd0850CaseInUse.setQRepId("1234");

        Mockito.when(caseInUseService.findByIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);
        Mockito.when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(ekd0350Case));

        this.mockMvc
                .perform(post("/cases/{caseId}/pend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.returnQueueId").value("APPSEMSIBA"));
    }

    @Test
    public void pendCaseForDaysAPISuccess() throws Exception {
        String request = "{" + "\"returnQueueId\" : \"APPSEMSIBA\"," + "\"pendForDays\" : 10" + "}";

        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setScanningDate(today);
        ekd0350Case.setScanningTime(time);

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setQueueId("QAQUE");
        ekd0850CaseInUse.setQRepId("1234");

        Mockito.when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(ekd0350Case));
        Mockito.when(caseInUseService.findByIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);

        this.mockMvc
                .perform(post("/cases/{caseId}/pend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.returnQueueId").value("APPSEMSIBA"));
    }

    @Test
    public void pendCaseAPIFailureCaseNotFound() throws Exception {
        String request = "{" + "\"returnQueueId\" : \"APPSEMSIBA\"," + "\"pendReleaseDate\" : \"20230220\"" + "}";

        this.mockMvc
                .perform(post("/cases/{caseId}/pend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void pendCaseAPIFailureQueueProfileNotFoundUnableToPendTheCase() throws Exception {
        String request = "{" + "\"returnQueueId\" : \"APPSEMSIBA\"" + "}";

        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setScanningDate(today);
        ekd0350Case.setScanningTime(time);

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setQueueId("QAQUE");
        ekd0850CaseInUse.setQRepId("1234");

        Mockito.when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(ekd0350Case));
        Mockito.when(caseInUseService.findByIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);

        this.mockMvc
                .perform(post("/cases/{caseId}/pend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350006"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Unable to pend the case."));
    }

    @Test
    public void pendCaseAPIFailureCaseInUse() throws Exception {
        String request = "{" + "\"returnQueueId\" : \"APPSEMSIBA\"," + "\"pendReleaseDate\" : \"20230220\"" + "}";

        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setScanningDate(today);
        ekd0350Case.setScanningTime(time);

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setQueueId("QAQUE");
        ekd0850CaseInUse.setQRepId("123");

        Mockito.when(caseInUseService.findByIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);
        Mockito.when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(ekd0350Case));
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(new EKD0150Queue()));

        this.mockMvc
                .perform(post("/cases/{caseId}/pend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD850409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Another user is working on case. Please try later."));
    }

    @Test
    public void pendCaseAPIFailureWrongBody() throws Exception {
        String request = "{" + "\"pendReleaseDate\" : \"20230220\"" + "}";

        this.mockMvc
                .perform(post("/cases/{caseId}/pend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("RETURN QUEUE ID is required"));
    }

    @Test
    public void unPendCaseAPISuccess() throws Exception {
        String request = "{" + "\"callingProgram\" : \"INDEX\"," + "\"caseId\" : \"024512915\"" + "}";

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setStatus(CaseStatus.A);
        ekd0350Case.setCaseId("1");
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION");

        Mockito.when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(ekd0350Case));
        Mockito.when(pendCaseService.findById(any())).thenReturn(Optional.of(new EKD0370PendCase()));

        this.mockMvc
                .perform(
                        post("/cases/unpend").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                                .content(request).requestAttr("repId", "1234").requestAttr("emsiUser", "true"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.caseId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.status").value("A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.cmFormattedName")
                        .value("21 BENEFICIARY ACTION"));
    }

    @Test
    public void unPendCaseAPIFailureCaseNotFound() throws Exception {
        String request = "{" + "\"callingProgram\" : \"INDEX\"," + "\"caseId\" : \"024512915\"" + "}";

        this.mockMvc
                .perform(post("/cases/unpend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234")
                        .requestAttr("emsiUser", "true"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void unPendCaseAPIFailurePendCaseNotFoundAgainstId() throws Exception {
        String request = "{" + "\"callingProgram\" : \"INDEX\"," + "\"caseId\" : \"024512915\"" + "}";

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setStatus(CaseStatus.A);
        ekd0350Case.setCaseId("1");
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION");

        Mockito.when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.empty());

        this.mockMvc
                .perform(post("/cases/unpend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234")
                        .requestAttr("emsiUser", "true"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void unPendCaseAPIFailureCaseNotReleased() throws Exception {
        String request = "{" + "\"callingProgram\" : \"INDEX\"," + "\"caseId\" : \"024512915\"" + "}";

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setCaseId("1");
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION");

        Mockito.when(ekd0350CaseRepository.findByCaseIdAndIsDeletedAndStatusIn(any(), any(), any()))
                .thenReturn(Optional.of(ekd0350Case));
        Mockito.when(pendCaseService.findById(any())).thenReturn(Optional.of(new EKD0370PendCase()));

        this.mockMvc
                .perform(post("/cases/unpend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234")
                        .requestAttr("emsiUser", "true"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350007")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("Unable to release the case."));
    }

    @Test
    public void unPendCaseAPIFailureWrongBody() throws Exception {
        String request = "{" + "}";

        this.mockMvc
                .perform(post("/cases/unpend", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234")
                        .requestAttr("emsiUser", "true"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("must be any of enum class com.afba.imageplus.dto.req.Enum.UnPendCallingProgram"));
    }

    @Test
    public void baendorseAPISuccess() throws Exception {
        this.mockMvc
                .perform(post("/cases/baendorse").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").value("Success"));
    }

    @Test
    public void unPendCasesEveryNightAPISuccess() throws Exception {
        this.mockMvc
                .perform(post("/cases/unpend/job").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Batch job took")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("milliseconds to un-pend cases")));
    }

    @Test
    public void moveQueueCaseAPISuccess() throws Exception {
        String request = "{" + "\"targetQueue\" : \"1234\"" + "}";

        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setCmFormattedName("21 BENEFICIARY ACTION ");
        caseRes.setStatus(CaseStatus.N);
        caseRes.setScanningDate(today);
        caseRes.setScanningTime(time);

        EKD0250CaseQueue ekd0250CaseQueue = new EKD0250CaseQueue();
        ekd0250CaseQueue.setQueueDateTime(LocalDateTime.now());

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(caseRes));
        Mockito.when(caseQueueService.findByCaseId(any())).thenReturn(Optional.of(ekd0250CaseQueue));
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(new EKD0150Queue()));
        Mockito.when(dateHelper.getDateTimeFormatFromStringCombination(any(), any())).thenReturn(new Date());

        this.mockMvc
                .perform(post("/cases/{caseId}/movque", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").value("Success"));
    }

    @Test
    public void moveQueueCaseAPIFailureWrongRequestBody() throws Exception {
        String request = "{" + "}";

        this.mockMvc
                .perform(post("/cases/{caseId}/movque", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Target queue should not be empty."));
    }

    @Test
    public void moveQueueCaseAPIFailureCaseNotFound() throws Exception {
        String request = "{" + "\"targetQueue\" : \"1234\"" + "}";

        this.mockMvc
                .perform(post("/cases/{caseId}/movque", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void moveQueueCaseAPIFailureCaseStatusInvalid() throws Exception {
        String request = "{" + "\"targetQueue\" : \"1234\"" + "}";

        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setCmFormattedName("21 BENEFICIARY ACTION ");
        caseRes.setStatus(CaseStatus.P);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(caseRes));

        this.mockMvc
                .perform(post("/cases/{caseId}/movque", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350008"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Case null status or current queue invalid for move queue"));
    }

    @Test
    public void moveQueueCaseAPIFailureInvalidTargetQueue() throws Exception {
        String request = "{" + "\"targetQueue\" : \"DELETE\"" + "}";

        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setCmFormattedName("21 BENEFICIARY ACTION ");
        caseRes.setStatus(CaseStatus.N);

        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(caseRes));
        Mockito.when(caseQueueService.findByCaseId(any())).thenReturn(Optional.of(new EKD0250CaseQueue()));

        this.mockMvc
                .perform(post("/cases/{caseId}/movque", 50).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350009"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Invalid target queue DELETE for move queue"));
    }

    @Test
    public void reassignCaseAPISuccess() throws Exception {
        final LocalDate today = LocalDate.now();
        final LocalTime time = LocalTime.now();

        Ekd0116Req requestBody = new Ekd0116Req();
        requestBody.setTargetQueue("Anas");

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setCurrentQueueId("QUEUE");
        ekd0350Case.setScanningDate(today);
        ekd0350Case.setScanningTime(time);

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setQueueId("QAQUE");
        ekd0850CaseInUse.setQRepId("1234");

        EKD0360UserProfile ekd0360UserProfile = new EKD0360UserProfile();
        ekd0360UserProfile.setRepDep("1100");

        EKD0150Queue ekd0150Queue = new EKD0150Queue();
        ekd0150Queue.setADepartmentId("6000");

        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(ekd0150Queue));
        Mockito.when(userProfileService.findById(any())).thenReturn(Optional.of(ekd0360UserProfile));
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));
        Mockito.when(dateHelper.reformateDate((LocalDate) any(), any())).thenReturn("1111111111");
        Mockito.when(caseInUseService.findByIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);
        Mockito.when(dateHelper.getDateTimeFormatFromStringCombination(any(), any())).thenReturn(new Date());

        this.mockMvc
                .perform(post("/cases/{caseId}/reassign", "1234").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestBody)).accept(MediaType.APPLICATION_JSON)
                        .requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.afb0660.caseId").value("1234"));
    }

    @Test
    public void reassignCaseAPIFailureQueueNotFound() throws Exception {
        Ekd0116Req requestBody = new Ekd0116Req();
        requestBody.setTargetQueue("MOVE");

        this.mockMvc
                .perform(post("/cases/{caseId}/reassign", "1234").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestBody)).accept(MediaType.APPLICATION_JSON)
                        .requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void reassignCaseAPIFailureCaseNotFound() throws Exception {
        Ekd0116Req requestBody = new Ekd0116Req();
        requestBody.setTargetQueue("MOVE");
        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(new EKD0150Queue()));

        this.mockMvc
                .perform(post("/cases/{caseId}/reassign", "1234").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestBody)).accept(MediaType.APPLICATION_JSON)
                        .requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void reassignCaseAPIFailureConflict() throws Exception {
        Ekd0116Req requestBody = new Ekd0116Req();
        requestBody.setTargetQueue("MOVE");
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setCurrentQueueId("MOVE");

        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(new EKD0150Queue()));
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));

        this.mockMvc
                .perform(post("/cases/{caseId}/reassign", "1234").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestBody)).accept(MediaType.APPLICATION_JSON)
                        .requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKDUSR404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("No record found against identifier %s in EKDUSER"));

    }

    @Test
    public void reassignCaseAPIFailureInternalTransitionQueue() throws Exception {
        Ekd0116Req requestBody = new Ekd0116Req();
        requestBody.setTargetQueue("MOVE");
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setCurrentQueueId("APPSAU");

        EKD0360UserProfile ekd0360UserProfile = new EKD0360UserProfile();
        ekd0360UserProfile.setRepDep("1100");

        EKD0150Queue ekd0150Queue = new EKD0150Queue();
        ekd0150Queue.setADepartmentId("6000");

        Mockito.when(queueService.findById(any())).thenReturn(Optional.of(ekd0150Queue));
        Mockito.when(userProfileService.findById(any())).thenReturn(Optional.of(ekd0360UserProfile));
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));

        this.mockMvc
                .perform(post("/cases/{caseId}/reassign", "1234").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestBody)).accept(MediaType.APPLICATION_JSON)
                        .requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350448"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value(" You cannot reassign case to internal Transition queue"));
    }

    @Test
    public void lockCaseInQueueAPISuccess() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);

        EKD0250CaseQueue test = new EKD0250CaseQueue();
        test.setCaseId("123");

        Mockito.when(caseQueueService.getFirstCaseFromQueue(any())).thenReturn(test);
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));
        Mockito.when(caseQueueService.findByCaseId(any())).thenReturn(Optional.of(new EKD0250CaseQueue()));

        this.mockMvc
                .perform(post("/cases/{queueId}/lock", "50").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.userId").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.caseId").value("123"));
    }

    @Test
    public void lockCaseInQueueAPIFailureConflict() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.N);

        EKD0250CaseQueue test = new EKD0250CaseQueue();
        test.setCaseId("123");

        Mockito.when(caseQueueService.getFirstCaseFromQueue(any())).thenReturn(test);
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));
        Mockito.when(ekd0850CaseInUseRepository.existsByCaseId(any())).thenReturn(true);

        this.mockMvc
                .perform(post("/cases/{queueId}/lock", "50").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD850409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Another user is working on case. Please try later."));
    }

    @Test
    public void lockCaseInQueueAPIFailureInvalidCaseStatus() throws Exception {
        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION ");
        ekd0350Case.setStatus(CaseStatus.W);

        EKD0250CaseQueue test = new EKD0250CaseQueue();
        test.setCaseId("123");

        Mockito.when(caseQueueService.getFirstCaseFromQueue(any())).thenReturn(test);
        Mockito.when(ekd0350CaseRepository.findById(any())).thenReturn(Optional.of(ekd0350Case));

        this.mockMvc
                .perform(post("/cases/{queueId}/lock", "50").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350003"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Case status is not valid for work a case"));
    }

    @Test
    public void getLockedCaseByUserIdAPISuccess() throws Exception {
        String request = "{" + "\"callingProgram\" : \"INDEX\"" + "}";

        EKD0350Case ekd0350Case = new EKD0350Case();
        ekd0350Case.setCmFormattedName("21 BENEFICIARY ACTION");
        ekd0350Case.setStatus(CaseStatus.N);
        ekd0350Case.setCaseId("024459991");
        ekd0350Case.setCmAccountNumber("2124400006");

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        ekd0850CaseInUse.setEkd0350Case(ekd0350Case);

        Mockito.when(caseInUseService.getCaseInUseByQRepIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);

        this.mockMvc
                .perform(get("/cases/in-work").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.caseId").value("024459991"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.cmAccountNumber").value("2124400006"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.cmFormattedName").value("21 BENEFICIARY ACTION"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.status").value("N"));
    }

    @Test
    public void getLockedCaseByUserIdAPIFailure() throws Exception {
        String request = "{" + "\"callingProgram\" : \"INDEX\"" + "}";

        EKD0850CaseInUse ekd0850CaseInUse = new EKD0850CaseInUse();
        Mockito.when(caseInUseService.getCaseInUseByQRepIdOrElseThrow(any())).thenReturn(ekd0850CaseInUse);

        this.mockMvc
                .perform(get("/cases/in-work").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request).requestAttr("repId", "1234"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case Not Found"));
    }

    @Test
    public void releaseSpecifiedDaysOldCasesInUseAPISuccess() throws Exception {
        String request = "{" + "\"daysAgo\": 30" + "}";

        this.mockMvc
                .perform(post("/cases/release/job").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Batch job took")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("milliseconds to release")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("cases from work")));
    }

    @Test
    public void releaseSpecifiedDaysOldCasesInUseAPIFailureWrongRequestBody() throws Exception {
        String request = "{" + "\"daysAgo\": 0" + "}";

        this.mockMvc
                .perform(post("/cases/release/job").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("DAYS AGO is out of range."));
    }

}
