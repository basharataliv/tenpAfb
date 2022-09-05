package com.afba.imageplus;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.QcRunHistoryController;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.res.QCRunTimeCheckRes;
import com.afba.imageplus.model.sqlserver.QCRUNHIS;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.repository.sqlserver.QCRUNHISRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.QCRUNHISService;
import com.afba.imageplus.service.UserProfileService;
import com.afba.imageplus.service.WRKQCRUNService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {QcRunHistoryController.class, ErrorServiceImp.class, RestResponseEntityExceptionHandler.class, BaseMapper.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class QcRunHistoryControllerTest {

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private QCRUNHISRepository qcrunhisRepository;

    @MockBean
    private UserProfileService userProfileService;

    @MockBean
    private WRKQCRUNService wrkqcrunService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @MockBean
    private QCRUNHISService qcrunhisService;

    @Autowired
    private QcRunHistoryController controller;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(controller);
    }

    @Test
    public void updateQcPassFlagAPISuccess() throws Exception {
        String request = "{" +
                "\"qcPolId\": \"1\"," +
                "\"qcQcPass\": \"N\"" +
                "}";
        QCRUNHIS qcrunhis = new QCRUNHIS("B21356AA.AAC", null, null, null, "1234", "N", LocalDate.now(), LocalTime.now());
        Mockito.when(qcrunhisService.updateQcFlag(any(), any(), any())).thenReturn(qcrunhis);
        mockMvc.perform(put("/qc-history/qc-pass/{caseId}", "B21356AA.AAC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "1234")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.qcReviewer").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.qcQcPass").value("N"));
    }

    @Test
    public void updateQcPassFlagAPIFailureWrongRequestBody() throws Exception {
        String request = "{" +
                "\"qcQcPass\": \"n\"" +
                "}";
        mockMvc.perform(put("/qc-history/qc-pass/{caseId}", "B21356AA.AAC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "1234")
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Status Code can only be Y or N")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Policy ID is a required field")));
    }

    @Test
    public void updateQcPassFlagAPIFailureWrongId() throws Exception {
        String request = "{" +
                "\"qcPolId\": \"1\"," +
                "\"qcQcPass\": \"N\"" +
                "}";
        Mockito.when(qcrunhisService.updateQcFlag(any(), any(), any())).thenThrow(
                new DomainException(HttpStatus.NOT_FOUND, EKDError.EKD000404.code(), "No Resource found against given ID: B21356AA.AAC"));
        mockMvc.perform(put("/qc-history/qc-pass/{caseId}", "B21356AA.AAC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "1234")
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("No Resource found against given ID: B21356AA.AAC"));
    }

    @Test
    public void qcRuntimeCheckAPISuccess() throws Exception {
        String request = "{" +
                "\"policyId\": \"012345677\"," +
                "\"userId\": \"afba\"," +
                "\"caseId\": \"123123\"," +
                "\"documentType\": \"APPS\"" +
                "}";
        QCRunTimeCheckRes response = new QCRunTimeCheckRes();
        response.setQcFlag("N");
        Mockito.when(qcrunhisService.qcRunTimeCheck(any(), any(), any(), any())).thenReturn(response);
        mockMvc.perform(post("/qc-history/runtime-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.qcFlag").value("N"));
    }

    @Test
    public void qcRuntimeCheckAPIFailureWrongRequestBody() throws Exception {
        mockMvc.perform(post("/qc-history/runtime-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("User id cannot be empty")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Case id cannot be empty")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Policy id cannot be empty")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Document type cannot be empty")));
    }

    @Test
    public void qcRuntimeCheckAPIFailureRequestBodySize() throws Exception {
        String request = "{" +
                "\"policyId\": \"012345677123\"," +
                "\"userId\": \"afbardsdffh\"," +
                "\"caseId\": \"123123\"," +
                "\"documentType\": \"APPS\"" +
                "}";
        mockMvc.perform(post("/qc-history/runtime-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Length of repId must be between 1-10")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Length of policy id cannot exceed 11")));
    }

}
