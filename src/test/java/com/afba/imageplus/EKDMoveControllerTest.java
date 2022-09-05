package com.afba.imageplus;

import com.afba.imageplus.controller.EKDMoveController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.model.sqlserver.*;
import com.afba.imageplus.repository.sqlserver.EKD0250CaseQueueRepository;
import com.afba.imageplus.repository.sqlserver.EmailTemplateRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.*;
import com.afba.imageplus.service.impl.EKDMoveServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.EmailUtility;
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
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {EKDMoveController.class, EKDMoveService.class, EKDMoveServiceImpl.class, ErrorServiceImp.class, RestResponseEntityExceptionHandler.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class EKDMoveControllerTest {

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @MockBean
    private CaseQueueService caseQueueService;

    @MockBean
    private DOCMOVEService docmoveService;

    @MockBean
    private CaseService caseService;

    @MockBean
    private CaseDocumentService caseDocumentService;

    @MockBean
    private MOVETRAILService mOVETRAILService;

    @MockBean
    private DOCTEMPService dOCTEMPService;

    @MockBean
    private SpringTemplateEngine thymeleafTemplateEngine;

    @MockBean
    private EmailTemplateRepository emailTemplateRepository;

    @MockBean
    private EmailUtility emailUtility;

    @MockBean
    private ErrorRepository errorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ErrorServiceImp errorServiceImp;

    @Autowired
    private EKDMoveServiceImpl ekdMoveServiceImpl;

    @Autowired
    private EKDMoveController controller;

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
    public void deleteAPISuccess() throws Exception {
        this.mockMvc.perform(get("/ekdMove/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").value("Successfully Completed"));
    }

    @Test
    public void moveAPISuccess() throws Exception {
        EKD0310Document document = new EKD0310Document();
        document.setDocumentId("123");
        document.setDocumentType("SomeDoc");

        EKD0350Case case1 = new EKD0350Case();
        case1.setCmAccountNumber("122");

        EKD0315CaseDocument caseDocument = new EKD0315CaseDocument();
        caseDocument.setCaseId("abc");
        caseDocument.setCases(case1);
        caseDocument.setDocument(document);

        List<EKD0315CaseDocument> documents = new ArrayList<EKD0315CaseDocument>();
        documents.add(caseDocument);

        EKD0350Case caseRes = new EKD0350Case();
        caseRes.setDocuments(documents);

        EKD0250CaseQueue queue = new EKD0250CaseQueue();
        queue.setCases(caseRes);

        List<EKD0250CaseQueue> list = new ArrayList<EKD0250CaseQueue>();
        list.add(queue);

        List<EKD0350Case> caseList = new ArrayList<>();
        caseList.add(caseRes);

        String message = "Document removed";

        Mockito.when(caseQueueService.getCaseDocumentsFromQueue(any())).thenReturn(list);
        Mockito.when(docmoveService.findById(any())).thenReturn(Optional.of(new DOCMOVE()));
        Mockito.when(caseDocumentService.RMVOBJDocument(any(), any())).thenReturn(message);
        Mockito.when(mOVETRAILService.save(any())).thenReturn(new MOVETRAIL());
        Mockito.when(caseService.findByCmAccountNumberAndName(any(), any())).thenReturn(caseList);
        Mockito.when(caseDocumentService.existsById(any())).thenReturn(false);
        this.mockMvc.perform(get("/ekdMove/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").value("Successfully Completed"));
    }

}
