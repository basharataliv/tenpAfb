package com.afba.imageplus;

import com.afba.imageplus.controller.CaseDocumentController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.req.RMVOBJReq;
import com.afba.imageplus.model.sqlserver.*;
import com.afba.imageplus.repository.sqlserver.EKD0310DocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0315CaseDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0350CaseRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.*;
import com.afba.imageplus.service.impl.CaseDocumentServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@EnableWebMvc
@SpringBootTest(classes = {CaseDocumentController.class, CaseDocumentService.class, CaseDocumentServiceImpl.class, BaseMapper.class, ErrorServiceImp.class, RestResponseEntityExceptionHandler.class})

public class CaseDocumentControllerTest {

    @Autowired
    private CaseDocumentController controller;

    @SpyBean
    private CaseDocumentService caseDocumentService;

    @MockBean
    private UserProfileService userProfileService;

    @MockBean
    private EKD0315CaseDocumentRepository ekd0315CaseDocumentRepository;

    @MockBean
    private EKD0310DocumentRepository ekd0310DocumentRepository;

    @MockBean
    private EKD0350CaseRepository ekd0350CaseRepository;

    @MockBean
    private ReindexingService reindexingService;

    @MockBean
    private EKDUserService ekdUserService;

    @MockBean
    private EKD0350Case ekd0350Case;

    @MockBean
    private CaseService caseService;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErrorRepository errorRepository;

    @Autowired
    private ErrorServiceImp errorServiceImp;

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
    public void insertCaseDocumentAPISuccess() throws Exception {
        String request = "{" +
                "\"caseId\": \"024510154\"," +
                "\"documentId\": \"B22083AA.AAW\"" +
                "}";

        EKD0350Case test = new EKD0350Case();
        Mockito.when(caseService.findById(any())).thenReturn(Optional.of(test));
        EKD0315CaseDocument ekd0315CaseDocument = new EKD0315CaseDocument();
        ekd0315CaseDocument.setCaseId("024510154");
        ekd0315CaseDocument.setDocumentId("B22083AA.AAW");
        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(new EKD0310Document()));
        Mockito.when(ekd0315CaseDocumentRepository.save(any())).thenReturn(ekd0315CaseDocument);

        this.mockMvc.perform(post("/case/document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.caseId").value("024510154"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value("B22083AA.AAW"));
    }

    @Test
    public void insertCaseDocumentAPIFailureWrongRequestBody() throws Exception {
        String request = "{" +
                "\"documentId\": \"\"" +
                "}";

        this.mockMvc.perform(post("/case/document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("documentId cannot be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("caseId cannot be null")));
    }

    @Test
    public void insertCaseDocumentAPIFailureNoCaseFoundAgainstId() throws Exception {
        String request = "{" +
                "\"caseId\": \"024510154\"," +
                "\"documentId\": \"B22083AA.AAW\"" +
                "}";

        this.mockMvc.perform(post("/case/document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315407"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("No Case found against id 024510154."));
    }

    @Test
    public void insertCaseDocumentAPIFailureNoDocumentFoundAgainstId() throws Exception {
        String request = "{" +
                "\"caseId\": \"024510154\"," +
                "\"documentId\": \"B22083AA.AAW\"" +
                "}";

        EKD0350Case test = new EKD0350Case();
        Mockito.when(caseService.findById(any())).thenReturn(Optional.of(test));

        this.mockMvc.perform(post("/case/document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315408"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("No Document found against id B22083AA.AAW."));
    }

    @Test
    public void insertCaseDocumentAPIFailureRecordIsFound() throws Exception {
        String request = "{" +
                "\"caseId\": \"024510154\"," +
                "\"documentId\": \"B22083AA.AAW\"" +
                "}";

        EKD0350Case test = new EKD0350Case();
        Mockito.when(caseService.findById(any())).thenReturn(Optional.of(test));
        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(new EKD0310Document()));
        Mockito.when(ekd0315CaseDocumentRepository.findById(any())).thenReturn(Optional.of(new EKD0315CaseDocument()));

        this.mockMvc.perform(post("/case/document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315409"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Record is found"));
    }

    @Test
    public void deleteCaseDocumentAPISuccess() throws Exception {
        RMVOBJReq body = new RMVOBJReq("B22034AA.AAB", "000000505", "Y", "2203400002", "Authorized");
        EKD0350Case test = new EKD0350Case();
        test.setCmAccountNumber("2203400003");
        Mockito.when(userProfileService.findById(any())).thenReturn(Optional.of(new EKD0360UserProfile()));
        Mockito.when(ekd0315CaseDocumentRepository.findById(any())).thenReturn(Optional.of(new EKD0315CaseDocument()));
        Mockito.when(caseService.findById(any())).thenReturn(Optional.of(test));
        Mockito.when(ekdUserService.findById(any())).thenReturn(Optional.of(new EKDUser()));
        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(new EKD0310Document()));

        this.mockMvc.perform(delete("/case/document/rmvobj")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").value("Document Reindex and Document removed"));
    }

    @Test
    public void deleteCaseDocumentAPIFailureUserNotFound() throws Exception {
        RMVOBJReq body = new RMVOBJReq("B22034AA.AAB", "000000505", "Y", "2203400002", "Authorized");
        this.mockMvc.perform(delete("/case/document/rmvobj")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD360404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("User Profile not found against REPID: Authorized"));
    }

    @Test
    public void deleteCaseDocumentAPIFailureRecordNotFound() throws Exception {
        RMVOBJReq body = new RMVOBJReq("B22034AA.AAB", "000000505", "Y", "2203400002", "Authorized");
        Mockito.when(userProfileService.findById(any())).thenReturn(Optional.of(new EKD0360UserProfile()));
        this.mockMvc.perform(delete("/case/document/rmvobj")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("record not found with caseId and DocumentId"));
    }

    @Test
    public void deleteCaseDocumentAPIFailurePolicyNoMismatch() throws Exception {
        RMVOBJReq body = new RMVOBJReq("B22034AA.AAB", "000000505", "Y", "2203400002", "Authorized");
        EKD0350Case test = new EKD0350Case();
        test.setCmAccountNumber("2203400003");
        Mockito.when(userProfileService.findById(any())).thenReturn(Optional.of(new EKD0360UserProfile()));
        Mockito.when(ekd0315CaseDocumentRepository.findById(any())).thenReturn(Optional.of(new EKD0315CaseDocument()));
        Mockito.when(caseService.findById(any())).thenReturn(Optional.of(test));
        this.mockMvc.perform(delete("/case/document/rmvobj")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                        .requestAttr("repId", "\"Authorized\"")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD350422"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("PolicyNo didn't match against Record present"));
    }

    @Test
    public void deleteCaseDocumentAPIFailurePolicyNoUndefined() throws Exception {
        RMVOBJReq body = new RMVOBJReq("B22034AA.AAB", "000000505", "Y", "", "Authorized");
        this.mockMvc.perform(delete("/case/document/rmvobj")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315422"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("PolicyNo must be defined when reindexFlag is Y"));
    }

    @Test
    public void deleteCaseDocumentAPIFailureUserRepIdEmpty() throws Exception {
        RMVOBJReq body = new RMVOBJReq("B22034AA.AAB", "000000505", "Y", "2203400002", "");
        this.mockMvc.perform(delete("/case/document/rmvobj")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                        .requestAttr("repId", "")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315423"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("UserRepId and JobRepId cannot be empty together"));
    }

    @Test
    public void deleteCaseDocumentAPIFailureFieldsEmpty() throws Exception {
        this.mockMvc.perform(delete("/case/document/rmvobj")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .requestAttr("repId", "")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("userId must not be null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("documentId cannot be empty")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("CaseId can not be empty")));
    }

    @Test
    public void moveCaseDocumentAPISuccess() throws Exception {
        String request = "{" +
                "\"existingCaseId\": \"024510154\"," +
                "\"documentId\": \"B22083AA.AAW\"," +
                "\"targetCaseId\": \"024509466\"" +
                "}";

        EKD0350Case test = new EKD0350Case();
        Mockito.when(caseService.findById(any())).thenReturn(Optional.of(test));
        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(new EKD0310Document()));
        EKD0315CaseDocument ekd0315CaseDocument = new EKD0315CaseDocument();
        ekd0315CaseDocument.setCaseId("024510154");
        ekd0315CaseDocument.setDocumentId("B22083AA.AAW");
        Mockito.when(ekd0315CaseDocumentRepository.findById(any())).thenReturn(Optional.of(ekd0315CaseDocument));
        Mockito.when(ekd0315CaseDocumentRepository.save(any())).thenReturn(ekd0315CaseDocument);
        Mockito.doReturn("Document removed").when(caseDocumentService).RMVOBJDocument(any(), any());

        this.mockMvc.perform(post("/case/document/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.caseId").value("024510154"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.documentId").value("B22083AA.AAW"));
    }

    @Test
    public void moveCaseDocumentAPIFailureWrongRequestBody() throws Exception {
        String request = "{" +
                "\"existingCaseId\": \"\"," +
                "\"documentId\": \"\"," +
                "\"targetCaseId\": \"\"" +
                "}";

        this.mockMvc.perform(post("/case/document/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000400"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("documentId cannot  be empty or null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Existing caseId can not be empty or null")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Target case id must not be empty or null")));
    }

    @Test
    public void moveCaseDocumentAPIFailureTargetCaseAndExistingCaseSame() throws Exception {
        String request = "{" +
                "\"existingCaseId\": \"024510154\"," +
                "\"documentId\": \"B22083AA.AAW\"," +
                "\"targetCaseId\": \"024510154\"" +
                "}";

        this.mockMvc.perform(post("/case/document/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315411"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Target case and existing case should not be same"));
    }

    @Test
    public void moveCaseDocumentAPIFailureNoCaseFoundAgainstExistingCaseId() throws Exception {
        String request = "{" +
                "\"existingCaseId\": \"024510154\"," +
                "\"documentId\": \"B22083AA.AAW\"," +
                "\"targetCaseId\": \"024509466\"" +
                "}";

        this.mockMvc.perform(post("/case/document/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315407"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("No Case found against id 024510154."));
    }

    @Test
    public void moveCaseDocumentAPIFailureNoCaseFoundAgainstTargetCaseId() throws Exception {
        String request = "{" +
                "\"existingCaseId\": \"024510154\"," +
                "\"documentId\": \"B22083AA.AAW\"," +
                "\"targetCaseId\": \"024509466\"" +
                "}";

        EKD0350Case test = new EKD0350Case();
        Mockito.when(caseService.findById("024510154")).thenReturn(Optional.of(test));

        this.mockMvc.perform(post("/case/document/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315407"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("No Case found against id 024509466."));
    }

    @Test
    public void moveCaseDocumentAPIFailureNoDocumentFoundAgainstId() throws Exception {
        String request = "{" +
                "\"existingCaseId\": \"024510154\"," +
                "\"documentId\": \"B22083AA.AAW\"," +
                "\"targetCaseId\": \"024509466\"" +
                "}";

        EKD0350Case test = new EKD0350Case();
        Mockito.when(caseService.findById(any())).thenReturn(Optional.of(test));

        this.mockMvc.perform(post("/case/document/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315408"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("No Document found against id B22083AA.AAW."));
    }

    @Test
    public void moveCaseDocumentAPIFailureNoDocumentAndCaseAssociationFound() throws Exception {
        String request = "{" +
                "\"existingCaseId\": \"024510154\"," +
                "\"documentId\": \"B22083AA.AAW\"," +
                "\"targetCaseId\": \"024509466\"" +
                "}";

        EKD0350Case test = new EKD0350Case();
        Mockito.when(caseService.findById(any())).thenReturn(Optional.of(test));
        Mockito.when(documentService.findById(any())).thenReturn(Optional.of(new EKD0310Document()));

        this.mockMvc.perform(post("/case/document/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                        .requestAttr("repId", "Authorized")
                )
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD315410"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("No Document and Case association found"));
    }

}
