package com.afba.imageplus;

import com.afba.imageplus.controller.CaseCommentController;
import com.afba.imageplus.controller.exceptions.RestResponseEntityExceptionHandler;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.mapper.CaseCommentReqMapper;
import com.afba.imageplus.dto.mapper.CaseCommentResMapper;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0352CaseComment;
import com.afba.imageplus.model.sqlserver.EKD0353CaseCommentLine;
import com.afba.imageplus.repository.sqlserver.EKD0352CaseCommentRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.*;
import com.afba.imageplus.service.impl.CaseCommentServiceImpl;
import com.afba.imageplus.service.impl.CaseDocumentServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.FileHelper;
import com.afba.imageplus.utilities.ImageConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {CaseCommentController.class, CaseCommentService.class,
        CaseCommentServiceImpl.class, CaseCommentReqMapper.class,
        CaseCommentResMapper.class, BaseMapper.class, ErrorServiceImp.class,
        RestResponseEntityExceptionHandler.class, CaseDocumentServiceImpl.class})
@AutoConfigureMockMvc
@EnableWebMvc
@EnableSpringDataWebSupport
public class CaseCommentControllerTest {

    @Autowired
    private CaseCommentController controller;

    @Autowired
    private CaseCommentServiceImpl caseCommentServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EKD0352CaseCommentRepository ekd0352CaseCommentRepository;

    @MockBean
    private CaseCommentLineService caseCommentLineService;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private CaseService caseService;

    @MockBean
    private AuthorizationHelper authorizationHelper;

    @MockBean
    private ImageConverter imageConverter;

    @MockBean
    private FileHelper fileHelper;

    @MockBean
    private ErrorRepository errorRepository;

    @MockBean
    private CaseDocumentService caseDocumentService;

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
    public void commentDocToSharePointAPISuccess() throws Exception {
        var entity = new EKD0352CaseComment();
        entity.setCommentKey(2L);
        entity.setCaseId("1");
        entity.setIsDeleted(0);
        entity.setEkdCase(new EKD0350Case());
        entity.getEkdCase().setCaseId("1");
        entity.getEkdCase().setCmAccountNumber("1234568789");
        var commentLine1 = new EKD0353CaseCommentLine();
        commentLine1.setCommentLine("Comment line 1");
        var commentLine2 = new EKD0353CaseCommentLine();
        commentLine2.setCommentLine("Comment line 2");
        var linkedHashSet = new LinkedHashSet<EKD0353CaseCommentLine>();
        linkedHashSet.add(commentLine1);
        linkedHashSet.add(commentLine2);
        entity.setCommentLines(linkedHashSet);
        List<EKD0352CaseComment> caseComments = new ArrayList<>();
        caseComments.add(entity);
        Mockito.when(ekd0352CaseCommentRepository.findByCaseIdAndIsDeleted(any(), any())).thenReturn((caseComments));
        Mockito.when(imageConverter.generateTiffFromCommentList(any(), any(), any())).thenReturn("");
        Mockito.when(documentService.insert(any())).thenReturn(new EKD0310Document());
        this.mockMvc.perform(post("/cases/{id}/comments/document", "000000215")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{ }"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void commentDocToSharePointAPIFailureCaseCommentNotFound() throws Exception {
        Mockito.when(ekd0352CaseCommentRepository.findByCaseIdAndIsDeleted(any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(post("/cases/{id}/comments/document", "000000215")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{ }"))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD352404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case comment not found against ID 000000215."));
    }

    @Test
    public void deleteAPISuccess() throws Exception {
        Mockito.when(ekd0352CaseCommentRepository.findById(any())).thenReturn(Optional.of(new EKD0352CaseComment()));
        this.mockMvc.perform(delete("/cases/{caseId}/comments/{id}", "000000215", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").value("Record with ID: 123 is deleted from EKD0352CaseComment"))
                ;
    }

    @Test
    public void deleteAPIFailureCaseNotFound() throws Exception {
        this.mockMvc.perform(delete("/cases/{caseId}/comments/{id}", "000000215", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD352404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Case comment not found against ID 123."))
        ;
    }

//    @Test
//    public void commentDocToSharePointAPIFailureIOException() throws Exception {
//        //make src/test/resources/IOExceptionTest Directory and modify permissions to not allow file deletion
//        var entity = new EKD0352CaseComment();
//        entity.setCommentKey(2L);
//        entity.setCaseId("1");
//        entity.setIsDeleted(0);
//        var commentLine1 = new EKD0353CaseCommentLine();
//        commentLine1.setCommentLine("Comment line 1");
//        var commentLine2 = new EKD0353CaseCommentLine();
//        commentLine2.setCommentLine("Comment line 2");
//        var linkedHashSet = new LinkedHashSet<EKD0353CaseCommentLine>();
//        linkedHashSet.add(commentLine1);
//        linkedHashSet.add(commentLine2);
//        entity.setCommentLines(linkedHashSet);
//        List<EKD0352CaseComment> caseComments = new ArrayList<>();
//        caseComments.add(entity);
//        Mockito.when(ekd0352CaseCommentRepository.findByCaseIdAndIsDeleted(any(), any())).thenReturn((caseComments));
//        Mockito.when(imageConverter.generateTiffFromCommentList(any(),any())).thenReturn("src/test/resources/IOExceptionTest");
//        Mockito.when(documentService.insert(any())).thenReturn(new EKD0310Document());
//        this.mockMvc.perform(post("/cases/{id}/comments/document", "000000215")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content("{ }"))
//                .andDo(print()).andExpect(status().is5xxServerError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("failed"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value("EKD000500"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.message").value("Unknown I/O error listing contents of directory: src\\test\\resources\\IOExceptionTest"));
//    }
}
