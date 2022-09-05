package com.afba.imageplus;

import com.afba.imageplus.controller.DocumentTypeController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.model.sqlserver.EKD0110DocumentType;
import com.afba.imageplus.repository.sqlserver.EKD0110DocumentTypeRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.AuthorizationCacheService;
import com.afba.imageplus.service.DocumentTypeService;
import com.afba.imageplus.service.impl.DocumentTypeServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.DateHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { DocumentTypeController.class, DocumentTypeService.class, DocumentTypeServiceImpl.class,
        BaseMapper.class, ErrorServiceImp.class, DateHelper.class, RestResponseEntityExceptionHandler.class })
@AutoConfigureMockMvc
@EnableWebMvc
class DocumentTypeControllerTest {

    final String id = "DOCTYPE1";

    private static final String updateRequest = "{" + "\"documentType\": \"SomeDoc1\","
            + "\"documentDescription\": \"[edit-2] Setting document description\","
            + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
            + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true" + "}";

    EKD0110DocumentType documentType;

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @MockBean
    private AuthorizationCacheService authorizationCacheService;

    @MockBean
    private EKD0110DocumentTypeRepository ekd0110DocumentTypeRepository;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentTypeController controller;

    @Autowired
    private DocumentTypeServiceImpl documentTypeServiceImpl;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @PostConstruct
    void setup() {
        documentType = new EKD0110DocumentType();
        documentType.setDocumentType("SomeDoc1");
        documentType.setDocumentDescription("[edit-2] Setting document description");
        documentType.setDefaultQueueId("IMAGHLDQ");
        documentType.setUserLastUpdate("[edit] Afnan QA");
        documentType.setLastUpdateDateTime(LocalDateTime.now());
    }

    @Test
    void contextLoads() throws Exception {
        Assertions.assertNotNull(controller);
    }

    @Test
    void updateAPISuccess() throws Exception {
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(documentType));
        this.mockMvc
                .perform(put("/document-types/{id}", id).requestAttr("repId", "TEST")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentType").value("DOCTYPE1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.lastUpdateDateTime").isNotEmpty());
    }

    /*
     * @Test void updateAPIFailureThrowException() throws Exception {
     * Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(
     * documentType);
     * Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(
     * Optional.of(new EKD0110DocumentType()));
     * Mockito.when(objectMapper.updateValue(any(), any())).thenThrow(new
     * JsonMappingException("")); this.mockMvc.perform(put("/document-types/{id}",
     * id) .contentType(MediaType.APPLICATION_JSON)
     * .accept(MediaType.APPLICATION_JSON) .content(updateRequest))
     * .andDo(print()).andExpect(status().is4xxClientError())
     * .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed")); //
     * .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
     * ; }
     */
    @Test
    void updateAPIFailureWrongRequest() throws Exception {
        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"[edit] IMAGHLDQ\"," + "\"userLastUpdate\": \"[edit] Afnan QA\"," + "}";
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.empty());
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"));
    }

    @Test
    void updateAPIFailureWrongId() throws Exception {
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.empty());
        this.mockMvc
                .perform(put("/document-types/{id}", id).requestAttr("repId", "TEST")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("DocumentType Not Found"));
    }

    /*
     * @Test void updateAPIFailureWrongDocumentType() throws Exception {
     * Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(
     * Optional.of(EKD0110DocumentType.builder().documentType("SomeDoc1").
     * documentDescription("Doc Type 1").build()));
     * this.mockMvc.perform(put("/document-types/{id}", id)
     * .contentType(MediaType.APPLICATION_JSON) .accept(MediaType.APPLICATION_JSON)
     * .content(updateRequest))
     * .andDo(print()).andExpect(status().is4xxClientError())
     * .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed")) //
     * .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
     * .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").
     * value("Provided document type: SomeDoc1 already exist")); }
     */

    @Test
    void updateAPIFailureEmptyDocumentType() throws Exception {
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        String request = "{" + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
                + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true" + "}";
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("documentType cannot be blank"));
    }

    @Test
    void updateAPIFailureNoDocumentDescription() throws Exception {
        EKD0110DocumentType documentType = new EKD0110DocumentType();
        documentType.setDocumentType("SomeDoc1");
        documentType.setDocumentDescription("[edit-2] Setting document description");
        documentType.setDefaultQueueId("IMAGHLDQ");
        documentType.setUserLastUpdate("[edit] Afnan QA");
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        String request = "{" + "\"documentType\": \"SomeDoc1\"," + "\"defaultQueueId\": \"IMAGHLDQ\","
                + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\"," + "\"securityClass\": \"102102\","
                + "\"allowImpA\": true," + "\"isAppsDoc\": true" + "}";
        ;
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("documentDescription cannot be blank"));
    }

    @Test
    void updateAPIFailureNoDefaultQueueId() throws Exception {
        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\"," + "\"defaultSuspendDays\": 1,"
                + "\"createNewCase\": \"1\"," + "\"securityClass\": \"102102\"," + "\"allowImpA\": true,"
                + "\"isAppsDoc\": true" + "}";
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("defaultQueueId cannot be null"));
    }

    @Test
    void updateAPIFailureDefaultSuspendDaysSize() throws Exception {
        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
                + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true,"
                + "\"defaultSuspendDays\": 0" + "}";
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Length of defaultSuspendDays should at least be 1"));
    }

    @Test
    void updateAPIFailureNoInBatchSize() throws Exception {
        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
                + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true,"
                + "\"noInBatch\": 0" + "}";
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Length of noInBatch should at least be 1"));
    }

    @Test
    void updateAPIFailureRetentionPeriodSize() throws Exception {
        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
                + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true,"
                + "\"retentionPeriod\": 0" + "}";
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Length of retentionPeriod should at least be 1"));
    }

    @Test
    void updateAPIFailureSecurityClassSize() throws Exception {
        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
                + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true,"
                + "\"securityClass\": \"\"" + "}";
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("securityClass must be a number of minimum length 1"));
    }

    @Test
    void updateAPIFailureInpdTypeASize() throws Exception {
        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
                + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true,"
                + "\"inpdTypeA\": \"sss\"" + "}";
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Length of inpdTypeA can either be 0 or 2"));
    }

    @Test
    void updateAPIFailureFaxPSizeASize() throws Exception {
        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
                + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true,"
                + "\"faxPSizeA\": \"\"" + "}";
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("faxPSizeA must be a number of minimum length 1"));
    }

    @Test
    void updateAPIFailureStoreMethodSize() throws Exception {
        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
                + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true,"
                + "\"storeMethod\": 11" + "}";
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Length of storeMethod must be 1"));
    }

    @Test
    void updateAPIFailureOptSysIdSize() throws Exception {
        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
                + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true,"
                + "\"optSysId\": 11" + "}";
        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message")
                        .value("Length of optSysId can either be 0 or 1"));
    }

    @Test
    void updateAPIFailureLan3995Size() throws Exception {

        String request = "{" + "\"documentType\": \"SomeDoc1\","
                + "\"documentDescription\": \"[edit-2] Setting document description\","
                + "\"defaultQueueId\": \"IMAGHLDQ\"," + "\"defaultSuspendDays\": 1," + "\"createNewCase\": \"1\","
                + "\"securityClass\": \"102102\"," + "\"allowImpA\": true," + "\"isAppsDoc\": true," + "\"lan3995\": 11"
                + "}";

        Mockito.when(ekd0110DocumentTypeRepository.save(any())).thenReturn(documentType);
        Mockito.when(ekd0110DocumentTypeRepository.findById(any())).thenReturn(Optional.of(new EKD0110DocumentType()));
        this.mockMvc
                .perform(put("/document-types/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).content(request))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400")).andExpect(
                        MockMvcResultMatchers.jsonPath("$.responseData.message").value("Length of lan3995 must be 1"));
    }

}
