package com.afba.imageplus;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.ReindexController;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.model.sqlserver.EKD0260Reindexing;
import com.afba.imageplus.repository.sqlserver.EKD0260ReindexingRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.ReindexingService;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.ArgumentMatchers.any;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ReindexController.class, BaseMapper.class, ErrorServiceImp.class, RestResponseEntityExceptionHandler.class})
@AutoConfigureMockMvc
@EnableWebMvc
@EnableSpringDataWebSupport
public class ReindexControllerTest {

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private EKD0260ReindexingRepository ekd0260ReindexingRepository;

    @MockBean
    private ReindexingService reindexingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private ReindexController controller;

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
    public void updateDocumentStatusForReIndexAPISuccess() throws Exception {
        EKD0260Reindexing ekd0260Reindexing = new EKD0260Reindexing();
        ekd0260Reindexing.setDocumentId("B21356AA.AAC");
        ekd0260Reindexing.setDocumentType("SomeDoc");
        ekd0260Reindexing.setIndexFlag(true);
        Mockito.when(reindexingService.updateDocumentForReIndexStatus(any(), any())).thenReturn(ekd0260Reindexing);
        this.mockMvc.perform(put("/re-index/documents/{documentId}/inuse/{indexFlag}", "B21356AA.AAC", true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value("B21356AA.AAC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentType").value("SomeDoc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.indexFlag").value("true"));
    }

    @Test
    public void updateDocumentStatusForReIndexAPIFailureWrongDocumentId() throws Exception {
        EKD0260Reindexing ekd0260Reindexing = new EKD0260Reindexing();
        ekd0260Reindexing.setDocumentId("B21356AA.AAC");
        ekd0260Reindexing.setDocumentType("SomeDoc");
        ekd0260Reindexing.setIndexFlag(true);
        Mockito.when(reindexingService.updateDocumentForReIndexStatus(any(), any())).thenThrow(new DomainException(HttpStatus.NOT_FOUND, EKDError.EKD260404.code(), "Document not found for re-indexing against ID: B21356AA.AAC"));
        this.mockMvc.perform(put("/re-index/documents/{documentId}/inuse/{indexFlag}", "B21356AA.AAC", true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD260404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Document not found for re-indexing against ID: B21356AA.AAC"));
    }

    @Test
    public void removeDocumentFromReIndexAPISuccess() throws Exception {
        EKD0260Reindexing ekd0260Reindexing = new EKD0260Reindexing();
        ekd0260Reindexing.setDocumentId("B21356AA.AAC");
        ekd0260Reindexing.setDocumentType("SomeDoc");
        ekd0260Reindexing.setIndexFlag(true);
        Mockito.when(reindexingService.removeDocumentFromReIndex(any())).thenReturn(ekd0260Reindexing);
        this.mockMvc.perform(delete("/re-index/documents/{documentId}", "B21356AA.AAC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value("B21356AA.AAC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentType").value("SomeDoc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.indexFlag").value("true"));
    }

    @Test
    public void removeDocumentFromReIndexAPIFailureWrongDocumentId() throws Exception {
        EKD0260Reindexing ekd0260Reindexing = new EKD0260Reindexing();
        ekd0260Reindexing.setDocumentId("B21356AA.AAC");
        ekd0260Reindexing.setDocumentType("SomeDoc");
        ekd0260Reindexing.setIndexFlag(true);
        Mockito.when(reindexingService.removeDocumentFromReIndex(any())).thenThrow(new DomainException(HttpStatus.NOT_FOUND, EKDError.EKD260404.code(), "Document with id B21356AA.AAC not found for re-indexing"));
        this.mockMvc.perform(delete("/re-index/documents/{documentId}", "B21356AA.AAC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD260404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Document with id B21356AA.AAC not found for re-indexing"));
    }

    @Test
    public void getDocumentFromReIndexAPISuccess() throws Exception {
        EKD0260Reindexing ekd0260Reindexing = new EKD0260Reindexing();
        ekd0260Reindexing.setDocumentId("B21356AA.AAC");
        ekd0260Reindexing.setDocumentType("SomeDoc");
        ekd0260Reindexing.setIndexFlag(true);
        Mockito.when(reindexingService.getDocumentFromReIndex(any())).thenReturn(ekd0260Reindexing);
        this.mockMvc.perform(get("/re-index/documents/{documentId}", "B21356AA.AAC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value("B21356AA.AAC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentType").value("SomeDoc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.indexFlag").value("true"));
    }

    @Test
    public void getDocumentFromReIndexAPIFailureWrongDocumentId() throws Exception {
        EKD0260Reindexing ekd0260Reindexing = new EKD0260Reindexing();
        ekd0260Reindexing.setDocumentId("B21356AA.AAC");
        ekd0260Reindexing.setDocumentType("SomeDoc");
        ekd0260Reindexing.setIndexFlag(true);
        Mockito.when(reindexingService.getDocumentFromReIndex(any())).thenThrow(new DomainException(HttpStatus.NOT_FOUND, EKDError.EKD260404.code(), "Document with id B21356AA.AAC not found for re-indexing"));
        this.mockMvc.perform(get("/re-index/documents/{documentId}", "B21356AA.AAC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD260404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Document with id B21356AA.AAC not found for re-indexing"));
    }

    @Test
    public void getAllDocumentFromReIndexAPISuccess() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 5);
        EKD0260Reindexing ekd0260Reindexing = new EKD0260Reindexing();
        ekd0260Reindexing.setDocumentId("B21356AA.AAC");
        ekd0260Reindexing.setDocumentType("SomeDoc");
        ekd0260Reindexing.setIndexFlag(true);
        List<EKD0260Reindexing> list = Arrays.asList(ekd0260Reindexing, new EKD0260Reindexing());
        Page<EKD0260Reindexing> page = new PageImpl<>(list, pageRequest, list.size());
        Mockito.when(reindexingService.getAllDocumentFromReIndexByDocumentType(any(), any())).thenReturn(page);
        this.mockMvc.perform(get("/re-index/documents/document-types/{documentType}", "SomeDoc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "5")
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].documentType").value("SomeDoc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].documentId").value("B21356AA.AAC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].indexFlag").value("true"));
    }

    @Test
    public void getAllDocumentsPageinationAPISuccess() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 5);
        EKD0260Reindexing ekd0260Reindexing = new EKD0260Reindexing();
        ekd0260Reindexing.setDocumentId("B21356AA.AAC");
        ekd0260Reindexing.setDocumentType("SomeDoc");
        ekd0260Reindexing.setIndexFlag(true);
        List<EKD0260Reindexing> list = Arrays.asList(ekd0260Reindexing, new EKD0260Reindexing());
        Page<EKD0260Reindexing> page = new PageImpl<>(list, pageRequest, list.size());
        Mockito.when(reindexingService.getAllReindexingDocuments(any(), any())).thenReturn(page);
        this.mockMvc.perform(get("/re-index/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .requestAttr("secRange", 3)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].documentType").value("SomeDoc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].documentId").value("B21356AA.AAC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.content[0].indexFlag").value("true"));
    }

    @Test
    public void getDocumentCountAPISuccess() throws Exception {
        Mockito.when(reindexingService.getDocumentTypeDocumentDescriptionCount()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/re-index/documents/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"));
    }
}
