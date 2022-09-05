package com.afba.imageplus.service;

import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.req.MoveDocumentReq;
import com.afba.imageplus.model.sqlserver.EKD0310Document;
import com.afba.imageplus.model.sqlserver.EKD0315CaseDocument;
import com.afba.imageplus.model.sqlserver.EKD0350Case;
import com.afba.imageplus.model.sqlserver.EKD0360UserProfile;
import com.afba.imageplus.model.sqlserver.id.EKD0315CaseDocumentKey;
import com.afba.imageplus.repository.sqlserver.EKD0310DocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0315CaseDocumentRepository;
import com.afba.imageplus.repository.sqlserver.EKD0350CaseRepository;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.seeder.ErrorSeeder;
import com.afba.imageplus.service.impl.CaseDocumentServiceImpl;
import com.afba.imageplus.service.impl.DocumentServiceImpl;
import com.afba.imageplus.service.impl.ErrorServiceImp;
import com.afba.imageplus.utilities.AuthorizationHelper;
import com.afba.imageplus.utilities.RangeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = { CaseDocumentServiceImpl.class, ErrorServiceImp.class, DocumentServiceImpl.class,
        RangeHelper.class, AuthorizationHelper.class })
public class CaseDocumentServiceTest {
    @MockBean
    ErrorRepository errorRepository;
    @Autowired
    private ErrorServiceImp errorServiceImp;
    @MockBean
    private AuthorizationCacheService authorizationCacheService;
    @MockBean
    private DocumentService documentService;
    @MockBean
    private EKD0315CaseDocumentRepository ekd0315CaseDocumentRepository;
    @MockBean
    private EKD0310DocumentRepository ekd0310DocumentRepository;
    @MockBean
    private EKD0350CaseRepository ekd0350CaseRepository;
    @MockBean
    private ReindexingService reindexingService;
    @MockBean
    private CaseService caseService;
    @MockBean
    private UserProfileService userProfileService;
    @MockBean
    private EKDUserService ekdUserService;
    @Autowired
    private CaseDocumentService service;

    @PostConstruct
    void mockErrorRepo() {
        Mockito.when(errorRepository.findAll())
                .thenReturn(new ArrayList<>(new ErrorSeeder(errorRepository).getEntities().values()));
        errorServiceImp.loadErrors();
    }

    @Test
    void moveDocToCaseTest() {

        Mockito.when(documentService.findById("abc")).thenReturn(
                Optional.of(EKD0310Document.builder().documentType("DFT").docPage(1).documentId("abc").build()));
        Mockito.when(caseService.findById("0123"))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("0123").cmAccountNumber("abc").build()));
        Mockito.when(caseService.findById("0124"))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("0124").cmAccountNumber("abc").build()));
        Mockito.when(ekd0315CaseDocumentRepository.findById(new EKD0315CaseDocumentKey("0123", "abc"))).thenReturn(
                Optional.of(EKD0315CaseDocument.builder().caseId("0123").documentId("abc").dasdFlag("1").build()));
        Mockito.when(service.save(EKD0315CaseDocument.builder().caseId("0124").documentId("abc").dasdFlag("1").build()))
                .thenReturn(EKD0315CaseDocument.builder().caseId("0124").documentId("abc").dasdFlag("1").build());

        Mockito.when(userProfileService.findById(any()))
                .thenReturn(Optional.of(EKD0360UserProfile.builder().repId("afba").closeFl(true).build()));
        var caseDoc = service.moveDoc(new MoveDocumentReq("abc", "0123", "0124"));
        Assertions.assertEquals("0124", caseDoc.getCaseId());
        Assertions.assertEquals("abc", caseDoc.getDocumentId());
    }

    @Test
    void moveDocToCaseTest_WhenExistingCaseNotFound() {

        Mockito.when(documentService.findById("abc")).thenReturn(
                Optional.of(EKD0310Document.builder().documentType("DFT").docPage(1).documentId("abc").build()));
        Mockito.when(caseService.findById("0123")).thenReturn(Optional.empty());
        Mockito.when(caseService.findById("0124"))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("0124").cmAccountNumber("abc").build()));
        Mockito.when(ekd0315CaseDocumentRepository.findById(new EKD0315CaseDocumentKey("0123", "abc"))).thenReturn(
                Optional.of(EKD0315CaseDocument.builder().caseId("0123").documentId("abc").dasdFlag("1").build()));
        Mockito.when(service.save(EKD0315CaseDocument.builder().caseId("0124").documentId("abc").dasdFlag("1").build()))
                .thenReturn(EKD0315CaseDocument.builder().caseId("0124").documentId("abc").dasdFlag("1").build());

        var res = assertThrows(DomainException.class, () -> {
            service.moveDoc(new MoveDocumentReq("abc", "0123", "0124"));
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void moveDocToCaseTest_WhenTargetCaseNotFound() {

        Mockito.when(documentService.findById("abc")).thenReturn(
                Optional.of(EKD0310Document.builder().documentType("DFT").docPage(1).documentId("abc").build()));
        Mockito.when(caseService.findById("0124")).thenReturn(Optional.empty());
        Mockito.when(caseService.findById("0123"))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("0123").cmAccountNumber("abc").build()));
        Mockito.when(ekd0315CaseDocumentRepository.findById(new EKD0315CaseDocumentKey("0123", "abc"))).thenReturn(
                Optional.of(EKD0315CaseDocument.builder().caseId("0123").documentId("abc").dasdFlag("1").build()));
        Mockito.when(service.save(EKD0315CaseDocument.builder().caseId("0124").documentId("abc").dasdFlag("1").build()))
                .thenReturn(EKD0315CaseDocument.builder().caseId("0124").documentId("abc").dasdFlag("1").build());

        var res = assertThrows(DomainException.class, () -> {
            service.moveDoc(new MoveDocumentReq("abc", "0123", "0124"));
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void moveDocToCaseTest_WhenDocNotFound() {

        Mockito.when(documentService.findById("abc")).thenReturn(Optional.empty());
        Mockito.when(caseService.findById("0123"))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("0123").cmAccountNumber("abc").build()));
        Mockito.when(caseService.findById("0124"))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("0124").cmAccountNumber("abc").build()));
        Mockito.when(ekd0315CaseDocumentRepository.findById(new EKD0315CaseDocumentKey("0123", "abc"))).thenReturn(
                Optional.of(EKD0315CaseDocument.builder().caseId("0123").documentId("abc").dasdFlag("1").build()));
        Mockito.when(service.save(EKD0315CaseDocument.builder().caseId("0124").documentId("abc").dasdFlag("1").build()))
                .thenReturn(EKD0315CaseDocument.builder().caseId("0124").documentId("abc").dasdFlag("1").build());

        var res = assertThrows(DomainException.class, () -> {
            service.moveDoc(new MoveDocumentReq("abc", "0123", "0124"));
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }

    @Test
    void moveDocToCaseTest_WhenDocAndCaseAssociationNotFound() {

        Mockito.when(documentService.findById("abc")).thenReturn(
                Optional.of(EKD0310Document.builder().documentType("DFT").docPage(1).documentId("abc").build()));
        Mockito.when(caseService.findById("0123"))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("0123").cmAccountNumber("abc").build()));
        Mockito.when(caseService.findById("0124"))
                .thenReturn(Optional.of(EKD0350Case.builder().caseId("0124").cmAccountNumber("abc").build()));
        Mockito.when(ekd0315CaseDocumentRepository.findById(new EKD0315CaseDocumentKey("0123", "abc")))
                .thenReturn(Optional.empty());
        Mockito.when(service.save(EKD0315CaseDocument.builder().caseId("0124").documentId("abc").dasdFlag("1").build()))
                .thenReturn(EKD0315CaseDocument.builder().caseId("0124").documentId("abc").dasdFlag("1").build());

        var res = assertThrows(DomainException.class, () -> {
            service.moveDoc(new MoveDocumentReq("abc", "0123", "0124"));
        });
        assertEquals(HttpStatus.NOT_FOUND, res.getHttpStatus());
    }
}
